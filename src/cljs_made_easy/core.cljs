(ns cljs-made-easy.core
  (:require [cljs-made-easy.line-by-line :as lbl]
            [cljs.nodejs :as nodejs]))

(enable-console-print!)
(println "Hello world!")

; (defn -main [& args]
;   (js/breakdebug)
;   (lbl/transform (js/Buffer. "abcd\nefgh\nijkl\n") nil #(println "Done" %1 %2)))
;
; (set! *main-cli-fn* -main)
