require 'zip/zip'

task :midje do 
	sh "lein midje"
end

def run_str 
	"\"java -cp #{Dir.pwd}/lib/clojure-1.2.1.jar:/#{Dir.pwd}/entry:. clojure.main #{Dir.pwd}/entry/MyBot.clj\""
end

def dump_log
	sh "cat game_logs/0.replay |pbcopy"
end
task :test_bot => :pack do
	sh "./tools/test_bot.sh #{run_str}"
	dump_log
end

task :archive => :pack do
	File.delete "entry.zip" if File.exists? "entry.zip"
	Zip::ZipFile.open("entry.zip", Zip::ZipFile::CREATE) do |zipfile|
		Dir.glob("entry/*.clj").each  do |file|
			zipfile.get_output_stream(File.basename(file)) {|f| f.puts File.read(file)}
		end
	end
end

task :play => :pack do
	hunter = "\"python tools/sample_bots/python/HunterBot.py\"" 
	lefty = "\"python tools/sample_bots/python/LeftyBot.py\"" 
	sh "./tools/playgame.py --player_seed 42 --end_wait=0.25 --verbose --log_dir game_logs --turns 1000 --map_file tools/maps/symmetric_maps/symmetric_10.map #{run_str} #{hunter} #{lefty} #{hunter}" 
	dump_log
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
