(ns cljs-made-easy.line-by-line
  (:require [cljs.nodejs :as nodejs])
  (:use [clojure.string :only [split, split-lines]]))

;; Require the node file system and stream modules
(def fs (js/require "fs"))
(def stream (js/require "stream"))

;; Define the os-specific end-of-line marker
(def ^:const eol (.-EOL (js/require "os")))


;; Define functions to transform data in the pipeline and....
;;
;; I originally attempted to test this function "stand-alone." It failed. But I learn from this failure.
;;
;; Since ClojureScript functions compile to Javascript functions, they have an implicit this member. If you invoke this
;; function and invoke `(println this)`, it will print `nil` because `this` is bound to `nil`. However, if you
;; - Create an empty Javascript object (`#js {}`)
;; - Set the member m to the function you previously created (`(set! o.m foo)`)
;; - Invoke the member function `(.m o)`
;; It will still print `nil`.
;;
;; However, if you change the function using the `this-as` macro and repeat the steps above, you will observe that
;; `this` is now bound to the empty Javascript object that you created.
;;
;; (See the web page, http://dev.clojure.org/display/design/this, for information.)
;;
;; Therefore to test this function, one must create an object, bind this function to a member of the object, and then
;; invoke the object member.
;;
(defn transform [chunk encoding done-fn]
  (this-as this
    ;; if we have a last line, append the chunk to this last line; otherwise, start with the chunk.
    ;; notice also that invoking str on chunk, a data buffer, converts chunk to a string.
    (let [data (if (.-lastLineData this)
                 (str (.-lastLineData this) chunk)
                 (str chunk))
          lines (clojure.string/split data (js/RegExp. eol "g"))]
      (set! (.-lastLineData this) (last lines))
      (doseq [line (butlast lines)]
        (.push this line))
      (done-fn nil chunk)
      )))
