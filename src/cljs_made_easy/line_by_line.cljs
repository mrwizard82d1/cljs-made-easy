(ns cljs-made-easy.line-by-line
  (:require [cljs.nodejs :as nodejs])
  (:use [clojure.string :only [split, split-lines]]))

;; Require the node file system and stream modules
(def fs (js/require "fs"))
(def stream (js/require "stream"))

;; Define the os-specific end-of-line marker
(def ^:const eol (.-EOL (js/require "os")))


;; Define functions to transform data in the pipeline and....
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
      (done-fn nil chunk))))
