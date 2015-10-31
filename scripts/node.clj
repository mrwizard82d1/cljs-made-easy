(require 'cljs.build.api)

(let [start (System/nanoTime)]
  (cljs.build.api/build "src"
                        {:main       'cljs-made-easy.core
                         :output-to  "out/cljs_made_easy.js"
                         :output-dir "out"
                         :target     :nodejs})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
