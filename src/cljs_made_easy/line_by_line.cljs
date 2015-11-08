(ns cljs-made-easy.line-by-line
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:use [clojure.string :only [split, split-lines]
        [cljs.core.async :only [>!]]]))

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
    (let [data (if (.-_lastLineData this)
                 (str (.-_lastLineData this) chunk)
                 (str chunk))
          lines (clojure.string/split data (js/RegExp. eol "g"))]
      (set! (.-_lastLineData this) (last lines))
      (doseq [line (butlast lines)]
        (.push this line))
      (done-fn)
      )))

(defn flush-buffer [done-fn]
  (this-as this
    (if (.-_lastLineData this)
      (.push this (.-_lastLineData this)))
    (set! (.-_lastLineData this) nil)
    (done-fn)))

(defn read-file-cb [file-name per-line-fn]
  (let [line-reader (.Transform stream #js {:objectMode true})
        source (.createReadStream fs file-name)]
    ;; Set the `_transform` and `_flush` properties of the Transform instance (line-reader).
    (set! (.-_transform line-reader) transform)
    (set! (.-_flush line-reader) flush-buffer)
    ;; Connect `source` to `line-reader` via a pipe
    (.pipe source line-reader)
    ;; When the reader is ready to be read
    (.on line-reader "readable"
         (fn []
           ;; When a line is available, call `per-line-fn`
           (when-let [line (.read line-reader)]
             (per-line-fn (str line))
             ;; Loop awaiting the next line
             (recur))))
    nil))

(defn read-file-chan [file-name output-channel]
  (let [line-reader (.Transform stream #js {:objectMode true})
        source (.createReadStream fs file-name)]
    ;; Set the `_transform` and `_flush` properties of the Transform instance (line-reade).
    (set! (.-_transform line-reader) transform)
    (set! (.-_flush line-reader) flush-buffer)
    ;; Connect `source` to `line-reader` via a pipe
    (.pipe source line-reader)
    ;; When the ready is ready
    (.on line-reader "readable"
         (fn []
           ;; Start a "go block" to write to `output-channel`
           (go
             (loop []
               ;; When a line is available, push it to `output-channel`
               (when-let [line (.read line-reader)]
                 (>! output-channel (str line))
                 (recur))))))
    nil))


