(ns deps-convert.core
  (:require [rum.core :as rum]
            [cljs.reader :as read]
            [clojure.pprint :as pprint]
            [zprint.core :as z]))

(defonce parsing-error (atom nil))
(defonce results (atom ""))


(defn parse-string [text-str]
  (try
    (let [results (read/read-string text-str)]
      ;; Handle quoted input gracefully
      (if (= (first results) 'quote)
        (second results)
        results))

    (catch js/Error e
      (reset! parsing-error "There was an error when parsing your input")
      (reset! results "")
      nil)))


(defn maven->clojure [input]
  (into {}
    (map (fn [[dep-name version & opt-seq]]
           (let [opts (apply hash-map opt-seq)]
             [dep-name  (-> opts
                          (assoc :version version))])))
    input))


(defn pprinted-string [data]
  (with-out-str
    (z/zprint data 160 {:map {:justify? true
                              :sort?    false}})))


(defn convert-text [text-str]
  (when-let [input (parse-string text-str)]
    (-> input
      (maven->clojure)
      (pprinted-string))))


(rum/defc app
  < rum/reactive []
  (let [input-box-id "input-box"]
    [:div
     [:form {:style     {:float :left}
             :on-submit (fn [e]
                          (.preventDefault e)
                          (let [text (-> (js/document.getElementById input-box-id) (.-value))]
                            (reset! results (convert-text text))))}
      [:textarea
       {:id          input-box-id
        :rows        "50"
        :cols        "120"
        :placeholder "Enter Dependencies here..."}]

      [:button {:type "submit"} "Convert"]]

     [:div {:style {:float :left}}
      [:label "Results"]
      [:textarea
       {:readOnly true
        :rows     "50"
        :cols     "120"
        :value    (rum/react results)}]]]))


(enable-console-print!)


(defn init! []
  (rum/mount (app) (js/document.getElementById "app")))


(defn on-reload [])
