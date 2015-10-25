(require '[cljs.build.api])

(cljs.build.api/watch "src"
                      {:main 'cljs-made-easy.core
                       :output-to "out/cljs_made_easy.js"
                       :output-dir "out"
                       :target :nodejs})
