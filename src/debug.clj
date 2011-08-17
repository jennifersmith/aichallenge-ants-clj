(ns debug)
;; too lazy to split into files!
(defn dump [& data](spit "dump.log" (apply str (cons "\n" (interpose " " data))) :append true))
(defmacro dbg[x] `(let [x# ~x] (dump '~x "=" x#) x#))


