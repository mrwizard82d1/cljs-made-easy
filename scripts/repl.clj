(require 'cljs.repl)
(require 'cljs.build.api)
(require 'cljs.repl.node)

;; Compile all scripts in the "src" directory and write the result to "out/cljs_made_easy.js."
(cljs.build.api/build "src"
                      {:main 'cljs-made-easy.core
                       :output-to "out/cljs_made_easy.js"
                       :output-dir "out"
                       :verbose true})

;; Invoke the node.js REPL.
(cljs.repl/repl (cljs.repl.node/repl-env)
                :watch "src"
                :output-dir "out")
