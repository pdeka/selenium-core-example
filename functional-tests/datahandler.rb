class DataHandler

	# This allows you to put data in a csv file and access it in your scripts for
	# a data driven approach.
	#
	# e.g.
	# require 'datahandler'
	# d = DataHandler.new("data.csv")
	# loginId = d.data[0].LoginId		--> line 1 of the csv file and column LoginId
	# pin = d.data[0].Pin				--> line 1 of the cvs file and column Pin
	#
	def initialize(datafile)
		@datafile = datafile
	end
	
	def data
		csv_data = File.readlines(@datafile)
		header = csv_data[0].split(",")
		header.each{|z| z.chomp!}
		data = Struct.new("Data", *header)
		csv_data.shift
		data_array = []
		csv_data.each do |line|
			line.each{|z| line.chomp!}
			data_array << data.new(*line.split(","))
		end
	return data_array
	end
	
end


# you can extend ruby selenium to handle newer selenium commands here:
class RubySelenium

	def method_missing(method, *args)
	  args.map! { |arg| arg.to_s }
      @test_script_commands << SeleniumCommand.new( method.to_s._h(*args) )
	end

end

class String
    def to_ymd_fmt
        split('/').reverse.join('-')
    end
end
