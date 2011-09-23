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
task :test_bot, [:map_file] => :pack do |t, args|
	args.with_defaults(:map_file => "tools/submission_test/test.map")
	sh "tools/playgame.py --engine_seed 142 --player_seed 42 --food none --end_wait=0.25 --verbose --log_dir game_logs --turns 30 --map_file #{args.map_file} #{run_str}  \"python tools/submission_test/TestBot.py\" -e --strict --capture_errors --loadtime 10000"
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
	sh "./tools/playgame.py --player_seed 42 --end_wait=0.25 --verbose --log_dir game_logs --turns 400 --map_file tools/maps/symmetric_maps/symmetric_10.map #{run_str} #{hunter} #{lefty} #{hunter}" 
	dump_log
end

task :show_archive do
	sh "unzip -l \"$@\" entry.zip"
end

task :pack do
	sh "rm -rdf entry" if File.directory? "entry"
	mkdir_p "entry"
	sh "cp src/*.clj entry"
	sh "cp MyBot.clj entry"
end
