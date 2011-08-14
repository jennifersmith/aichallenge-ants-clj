
task :midje do 
	sh "lein midje"
end

task :test_bot do
	sh "./test_bot.sh \"java -cp #{Dir.pwd}/lib/clojure-1.2.1.jar:/#{Dir.pwd}/src:. clojure.main #{Dir.pwd}/src/jbot/MyBot.clj\""
end

task :pack do
	File.delete "entry.zip" if File.exists? "entry.zip"
	sh "cd src/jbot"
	sh "zip entry.zip *.clj"
	sh "cd ../../"
end
