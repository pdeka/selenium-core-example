Steps to set up your project
----------------------------

Download and Install ant 1.7.1. Download from http://archive.apache.org/dist/ant/binaries/

Move to the project home directory. For example, c:\dev\selenium-core-example

Copy and rename sample-build.properties to build.properties and specify your database username/password accordingly

Download and install Ruby 1.8.2

From the command line execute:

c:/>gem install Selenium
c:/>gem install rubySelenium
 
Run 'ant -Denv=dev all' to build and run all tests on the project. 

**Please close all firefox windows the first time you run the build. This is because the build will try to create a new firefox profile
called 'webdriver'. It will fail to do so if there is another firefox with another profile running.**

Also note that the first time you run the build, you will be challenged with the following ....
"A script from "http://localhost:8080" is requesting enhanced abilities that are UNSAFE and could be used to
compromise your machine or data:
Read and upload local files
Allow these abilities only if you trust this source to be free of viruses or malicious programs."

... you need to allow this permanently. This is because the tests would need to upload files, which it cannot do if
it does not have this permission.

PLEASE NOTE THAT ITS ADVISABLE NOT TO BROWSE THE INTERNET USING THE 'WEBDRIVER' PROFILE.

Run <project root>\<tomcat home>\bin\startup.bat to start the tomcat server.

Go to http://localhost:8080/selenium/TestRunner.html?test=/selenium-tests/TestSuite.html to use the selenium dashboard.

FAQ
---

1. I am challenged with "A script from "http://localhost:8080" is requesting enhanced ..." all the time. Why is that?

Note that when the build is run, it copies the 'webdriver' profile to a temp directory and then starts firefox in this
profile. This means that when you answer the above security question, it remembers the answer in the copied profile and not
in the original one. To make it remember the answer in the original 'webdriver' profile, please start firefox in webdriver
profile manually and run the tests using http://localhost:8080/selenium/TestRunner.html?test=/selenium-tests/TestSuite.html

2. File upload is not possible using regular Selenium Core. How was it achieved in the project?

Getting file upload using javascript will always be an issue. We don't think Selenium would ever put in a solution to that.
Have a read through http://cakebaker.42dh.com/2006/03/29/file-upload-with-selenium/
Just like the guy says, we had to do 2 things. We had to change the “Selenium.prototype.doType” in the file “selenium-api.js”.
We added netscape.security.PrivilegeManager.enablePrivilege('UniversalFileRead');
The rest is taken care of by the webdriver profile for firefox. You don't have to worry about that.


 