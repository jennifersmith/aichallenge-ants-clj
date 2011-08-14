task :midje do 
	sh "lein midje"
end

task :test_bot => :pack do
	sh "./test_bot.sh \"java -cp #{Dir.pwd}/lib/clojure-1.2.1.jar:/#{Dir.pwd}/entry:. clojure.main #{Dir.pwd}/entry/MyBot.clj\""
end

task :archive => :pack do
	File.delete "entry.zip" if File.exists? "entry.zip"
	sh "zip entry.zip entry/*.clj"
end

task :pack do
	"rm -rdf entry" if File.exists? "entry"
	mkdir_p "entry"
	sh "cp src/*.clj entry"
	sh "cp MyBot.clj entry"
end
