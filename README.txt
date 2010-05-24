Steps to set up your project
----------------------------

Download and Install tortoise SVN. Download from http://tortoisesvn.tigris.org  

svn checkout https://subversion.avoka.local:8443/svn/Clients/Telstra/All-4-Biz/trunk all4biz. 'all4biz' can be
checkout anywhere in your machine. For example, c:\dev\all4biz

Download and Install ant 1.7.1. Download from http://archive.apache.org/dist/ant/binaries/

Move to the project home directory. For example, c:\dev\all4biz

Copy and rename sample-build.properties to build.properties and specify your database username/password accordingly

Download and install Ruby 1.8.2 or you can copy from S:\products\ruby\

From the command line execute:

c:/>gem install Selenium
c:/>gem install rubySelenium
 
Roles that are needed in formcenter
-----------------------------------
PLEASE NOTE: When creating roles be sure to check the "Organization Assignable" checkbox

A4B Global Admin    	-> Admin with all all4biz

Loyalty Fund Access 	-> All4biz Loyalty Refund
                       		All4biz Loyalty Transact

Merchant Manager 		-> All4biz Loyalty Access
						   All4biz Loyalty Refund
						   All4biz Loyalty Transact
						   All4biz Merchant Manager Rights
	                       All4biz Order Access
	                       All4biz Order Edit
						   All4biz User Admin

Order Access 	    	-> All4biz Order Edit

A4B Account Executives	-> All4biz Loyalty Access
						   All4biz Loyalty Refund
						   All4biz Loyalty Transact
						   All4biz Merchant Manager Rights
	                       All4biz Order Access
	                       All4biz Order Edit

Users that are needed in formcenter (Password for all users is 'secret1')
-----------------------------------
User                Role
----                ----
a4b-admin 	        A4B Global Admin
abcmanager1 	    MerchantManger
abcuser1 	        Loyalty Fund Access,Order Access
abcuser2 	        Loyalty Fund Access,Order Access
accountexemanager1 	MerchantManger
accountexeuser1 	Loyalty Fund Access,Order Access
accountexeuser2 	Loyalty Fund Access,Order Access
dlifemanager1 	    MerchantManger
dlifeuser1 	        Loyalty Fund Access,Order Access
dlifeuser2 	        Loyalty Fund Access,Order Access

Change the Context path for your portal to http://localhost:8080/all4biz/ in your formcenter. This can be found under Systems>Portals

Run 'ant -Denv=dev 1-build-and-testall' to build and run all tests on the project. Use this before checking code.

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

Run 'ant -Denv=dev 2-hotdeploy-resources' to deploy templates, javascript, css, images and configuration files to the webapp

Run 'ant -Denv=dev 3-build-all' to build and deploy the all4biz webapp and the selenium tests.

Run c:\dev\all4biz\apache-tomcat-6.0.24\bin\startup.bat to start the tomcat server.

Run 'ant -Denv=dev 4-run-custom-sql' to run your custom sql script to insert your custom data back again into the DB.

Go to http://localhost:8080/selenium/TestRunner.html?test=/selenium-tests/TestSuite.html to use the selenium dashboard.


FAQ
---

1. I am challenged with "A script from "http://localhost:8080" is requesting enhanced ..." all the time. Why is that?

Note that when the build is run, it copies the 'webdriver' profile to a temp directory and then starts firefox in this
profile. This means that when you answer the above security question, it remembers the answer in the copied profile and not
in the original one. To make it remember the answer in the original 'webdriver' profile, please start firefox in webdriver
profile manually and run the tests using http://localhost:8080/selenium/TestRunner.html?test=/selenium-tests/TestSuite.html

2. I get a 'Permission denied .. Location.toString' error everytime I run the a few tests?

The tests attempts to submit the PDF form to the formcenter and uses the portal url mentioned in formcenter; in
System > Portals. You should have the following entry there.

Telstra All-4-Biz Online	http://localhost:8080/all4biz/	The Telstra All-4-Biz & Loyalty Bonus Online Portal

Please note that the 'localhost' should be mention as such and the machine name should not be used.

3. File upload is not possible using regular Selenium Core. How was it achieved in the project?

Getting file upload using javascript will always be an issue. We don't think Selenium would ever put in a solution to that.
Have a read through http://cakebaker.42dh.com/2006/03/29/file-upload-with-selenium/
Just like the guy says, we had to do 2 things. We had to change the “Selenium.prototype.doType” in the file “selenium-api.js”.
We added netscape.security.PrivilegeManager.enablePrivilege('UniversalFileRead');
The rest is taken care of by the webdriver profile for firefox. You don't have to worry about that.


 