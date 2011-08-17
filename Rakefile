require 'zip/zip'

task :midje do 
	sh "lein midje"
end

task :test_bot => :pack do
	sh "./test_bot.sh \"java -cp #{Dir.pwd}/lib/clojure-1.2.1.jar:/#{Dir.pwd}/entry:. clojure.main #{Dir.pwd}/entry/MyBot.clj\""
	sh "cat game_logs/0.replay |pbcopy"
end

task :archive => :pack do
	File.delete "entry.zip" if File.exists? "entry.zip"
	Zip::ZipFile.open("entry.zip", Zip::ZipFile::CREATE) do |zipfile|
		Dir.glob("entry/*.clj").each  do |file|
			zipfile.get_output_stream(File.basename(file)) {|f| f.puts File.read(file)}
		end
	end
end

task :show_archive do
	sh "unzip -l \"$@\" entry.zip"
end

task :pack do
	"rm -rdf entry" if File.exists? "entry"
	mkdir_p "entry"
	sh "cp src/*.clj entry"
	sh "cp MyBot.clj entry"
end
