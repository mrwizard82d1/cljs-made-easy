(ns cljs-made-easy.core
  (:require [cljs-made-easy.line-by-line :as lbl]
            [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(defn -main [& args]
  (let [o #js {:push #(println %)}]
    (set! o.m lbl/transform)
    (.m o (js/Buffer. "abcd\nefgh") nil #(println "Done" %1 %2))
    )
  ; (println "Hello world!")
  )

(set! *main-cli-fn* -main)
