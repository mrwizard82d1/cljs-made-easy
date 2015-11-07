(ns cljs-made-easy.core
  (:require [cljs-made-easy.line-by-line :as lbl]
            [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(defn got-it [line]
  (println "Got the line <" line ">")
  )

;; I currently use this function to test library functions.
(defn -main [& args]
  (let [o #js {:push #(println "push" %)}]
    ;(set! o.l lbl/transform)
    ;(.l o (js/Buffer. "abcd\nefgh") nil #(println "transform done" %1 %2))
    ;(set! o.f lbl/flush-buffer)
    ;(.f o #(println "flush done"))
    (js* "debugger")
    (lbl/read-file-cb "tests/four_score.md" got-it)
    ))

(set! *main-cli-fn* -main)
