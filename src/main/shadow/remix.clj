(ns shadow.remix
  (:require
   [clojure.java.io :as io]
   [cljs.compiler :as cljs-comp]
   [clojure.string :as str]))

(defn all-vars [state]
  (for [[ns ns-info] (get-in state [:compiler-env :cljs.analyzer/namespaces])
        ns-def (-> ns-info :defs vals)]
    ns-def))

(defn create-pages
  {:shadow.build/stage :flush}
  [state]
  (doseq [ns-def (all-vars state)
          :when (get-in ns-def [:meta :remix/page])]

    (let [{:remix/keys [page loader]} (:meta ns-def)

          page-ns
          (-> ns-def :name namespace cljs-comp/munge)

          page-var
          (-> ns-def :name name cljs-comp/munge)

          loader-var
          (when loader (-> loader name cljs-comp/munge))

          content
          (str/join
           "\n"
           [(str "export {" page-var " as default} from \"../cljs/" page-ns ".js\";")
            (when loader-var
              (str "export {" loader-var " as loader} from \"../cljs/" page-ns ".js\";"))])

          out-dir
          (io/file "remix" "app" "routes")

          out-file
          (io/file out-dir (str page ".js"))]

      (io/make-parents out-file)
      (spit out-file content)))
  state)
