    require 'rubygems'
require 'RubySelenium'
require 'methods/selenium_methods'
require 'stringio'
require 'find'

class Batch_Generator

    # override to create TestSuite.html, which is the FitRunner default
    def create_test_suite_file_for_all_files
        File.open("#{@generate_location}/TestSuite.html", "w"){|f| f.print generate_test_suite_for_all_files}
    end

    # override to get test files within dirs under specified dir
    def build_tests_list
        if ! @test_list.nil?
          return @test_list
        end
        @test_list = []
        Find.find(@ruby_tests_location) do |path|
          if FileTest.directory?(path)
            if File.basename(path)[0] == "."
              Find.prune       # ignore .  .. and .svn
            else
              next
            end
          elsif File.extname(path) == ".test"
            puts "Processing: " + path
            @test_list << path
          end
        end

        if @test_list.size == 0
            raise MissingRubyTestFiles, "Can't find any rubySelenium tests in directory #{@ruby_tests_location}"
        else
            return @test_list.sort!
        end
    end

    # we decorate all the .test files with the RubySelenium startup code
    def build_tests_array
        tests_array = []
        build_tests_list.each do |test|

pre_test_code = <<CODE
require 'RubySelenium'
require 'methods/selenium_methods'

class SeleniumTest < SeleniumMethods
  def run_test
    # test code goes here
CODE

post_test_code = <<CODE
  end
end

testname = \'#{File.basename(test, '.test')}\'
s = RubySelenium.new(testname)
t = SeleniumTest.new(s)
t.run_test
s.complete
s.go
CODE

            code_array = StringIO.new(pre_test_code).readlines +
                         File.readlines(test) +
                         StringIO.new(post_test_code).readlines
            tests_array << code_array
#	    code_array.each_with_index { |line,line_num| puts "#{line_num+1} #{line}\n" }
        end
        tests_array
    end

    def generate_test_suite_for_all_files
        test_suite_content = []
        build_tests_list.each do |filename|
            test_name = File.basename(filename, ".test")
            test_suite_content << "<tr><td><a href=\"#{test_name}.html\">#{test_name}</a></td></tr>\n"
        end
        Templates.new("test_suite", "test_suite", test_suite_content).selenium_test_suite
    end

end

Batch_Generator.new("../build/test/web/ruby-selenium-tests").create_all_test_files
