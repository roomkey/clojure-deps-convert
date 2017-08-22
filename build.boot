(set-env!
  :dependencies '[[org.clojure/clojure "1.9.0-alpha17" :scope "provided"]
                  [org.clojure/clojurescript "1.9.562"]
                  [adzerk/boot-cljs "LATEST" :scope "test"]
                  [powerlaces/boot-figreload "LATEST" :scope "test"]
                  [pandeiro/boot-http "0.7.6" :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]

                  [adzerk/boot-cljs-repl   "0.3.3"] ;; latest release
                  [com.cemerick/piggieback "0.2.1"  :scope "test"]
                  [weasel                  "0.7.0"  :scope "test"]]
  :resource-paths #{"resources"}
  :source-paths #{"src"})

(require
  '[adzerk.boot-cljs          :refer [cljs]]
  '[adzerk.boot-cljs-repl     :refer [cljs-repl]]
  '[powerlaces.boot-figreload :refer [reload]]
  '[pandeiro.boot-http        :refer [serve]])


(deftask dev []
  (comp
    (serve)
    (watch)
    (reload)
    (cljs-repl)
    (cljs
      :source-map true
      :optimizations :none)))
