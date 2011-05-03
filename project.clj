;; # Project file.
;;
;; Please feel free to adjust settings below to match your project,
;; but it's important that when submitting you:
;; 1. Don't change neither `:disable-implicit-clean`, nor `:disable-deps-clean`;
;; 2. Run `lein deps` before you zip the bot for contest.
;;
;; Thanks to this the ZIP package will contain all deps, so they won't
;; be fetched during build (which may not be possible in the sandbox).
(defproject aichallenge-ants-clj "0.0.3"
  :description "AI Challenge 'Ants' -- Winter/Spring 2011"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]]
  :dev-dependencies [[marginalia "0.5.0"]]
  :disable-deps-clean true
  :disable-implicit-clean true
  :main my-bot.core
  :aot [my-bot.ants my-bot.ai my-bot.core]
  :jar-name "MyBot-light.jar"
  :uberjar-name "MyBot.jar"
  ; feel free to change this for deployment
  :warn-on-reflection true)

