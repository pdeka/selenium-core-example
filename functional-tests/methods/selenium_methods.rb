# Selenium helper methods written specifically for this project
#
# This is a collection if useful user-defined functions that can be used when writing rubySelenium tests.

require 'datahandler'
require 'methods/project_dsl'

class SeleniumMethods

  public
  
  include ProjectDsl

  def wait time_in_microseconds
    storeEval("var startTime = new Date(); var endTime = null; do { endTime = new Date(); } while ((endTime - startTime) < " +time_in_microseconds+ "); ", "avokaWaitImpl");
  end
  
  def select selector, value
    @s.select selector, value
  end

  def assertEntireTableAndHeading(tableId, data)
    data.each_with_index do |row, i|
      row.each_with_index do |cell, j|
        @s.assertTable("#{tableId}.#{i}.#{j}", cell.to_s)
      end
    end
  end

  def assertEntireTable(tableId, data)
    data.each_with_index do |row, i|
      row.each_with_index do |cell, j|
        @s.assertTable("#{tableId}.#{i+1}.#{j}", cell.to_s)
      end
    end
  end

  def openQueryLocation(location, args)
    args.each do |hash|
      queryString = "#{location}?"
      hash.each_pair do |key, value|
        queryString += "#{key}=#{value.to_s}&"
      end
      openLocation queryString
    end
  end


  protected

  # Supply a selenium object when you instantiate this class
  # example:
  #
  # require 'methods/seleniumMethods'
  #
  # Create new SeleniumMethods and RubySelenium instances
  # t = SeleniumMethods.new(s = RubySelenium.new("testname"))
  #
  def initialize(selenium)
    @s = selenium
  end

  # Template method that should be implemented in subclasses
  def run_test
    raise "Subclasses must provide an implementation for the run_test() method!\n" +
            "(Don't call super() either unless you want this msg...)"
  end

  # This is a dynamic delegator to delegate unknown methods to the selenium instance (if possible)
  # See the RubySelenium class docs for the available methods.
  def method_missing(method, *args)
#	  puts "****************** #{method.to_s}: #{args.join(", ")}"
    @s.send method, *args
  end

  def type(name, value="&nbsp;")
    @s.type name, value
  end


  public
  DEFAULT_PAGE_SIZE = 5
  attr_reader :message

end
