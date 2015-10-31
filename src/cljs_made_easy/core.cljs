(ns cljs-made-easy.core
  (:require [cljs-made-easy.line-by-line :as lbl]
            [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(defn -main [& args]
  ; (lbl/transform (js/Buffer. "abcd\nefgh\nijkl\n") nil #(println "Done" %1 %2))
  (println "Hello world!")
  )

(set! *main-cli-fn* -main)
