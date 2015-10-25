(require 'cljs.build.api)

(let [start (System/nanoTime)]
  (cljs.build.api/build "src"
                        {:main       'time-calc.core
                         :output-to  "out/time_calc.js"
                         :output-dir "out"
                         :target     :nodejs})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))

