(ns cljs-made-easy.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-made-easy.line-by-line :as lbl]
            [cljs.nodejs :as nodejs])
  (:use [cljs.core.async :only [chan]]))

(nodejs/enable-util-print!)4

(defn get-it [line]
  (println "Get it <" line ">"))

(defn got-it [chan]
  (go (while true
        (println "Got it <" (<! chan) ">"))))

;; I currently use this function to test library functions.
(defn -main [& args]
  (let [o #js {:push #(println "push" %)}]
    ;(set! o.l lbl/transform)
    ;(.l o (js/Buffer. "abcd\nefgh") nil #(println "transform done" %1 %2))
    ;(set! o.f lbl/flush-buffer)
    ;(.f o #(println "flush done"))
    #_(lbl/read-file-cb "tests/four_score.md" get-it)
    (let [good (chan)]
      (got-it good)
      (lbl/read-file-chan "tests/four_score.md" good))
    ))

(set! *main-cli-fn* -main)
