(ns cljs-made-easy.core
  (:require [cljs-made-easy.line-by-line :as lbl]
            [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

;; I currently use this function to test library functions.
(defn -main [& args]
  (let [o #js {:push #(println "push" %)}]
    (set! o.l lbl/transform)
    (.l o (js/Buffer. "abcd\nefgh") nil #(println "transform done" %1 %2))
    (set! o.f lbl/flush-buffer)
    (.f o #(println "flush done")))
  ; (println "Hello world!")
  )

(set! *main-cli-fn* -main)
