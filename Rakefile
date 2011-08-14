require 'zip/zip'

task :midje do 
	sh "lein midje"
end

task :test_bot do
	sh "./test_bot.sh \"java -cp #{Dir.pwd}/lib/clojure-1.2.1.jar:/#{Dir.pwd}/src:. clojure.main #{Dir.pwd}/src/jbot/MyBot.clj\""
end

task :pack do
	File.delete "entry.zip" if File.exists? "entry.zip"
	Zip::ZipFile.open("entry.zip", Zip::ZipFile::CREATE) do |zipfile|
		Dir.glob("src/jbot/*.clj").each  do |file|
			zipfile.get_output_stream(File.basename(file)) {|f| f.puts File.read(file)}
		end
	end
end
