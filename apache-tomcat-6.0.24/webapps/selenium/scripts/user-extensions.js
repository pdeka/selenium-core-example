/*
 * By default, Selenium looks for a file called "user-extensions.js", and loads and javascript
 * code found in that file. This file is a sample of what that file could look like.
 *
 * user-extensions.js provides a convenient location for adding extensions to Selenium, like
 * new actions, checks and locator-strategies.
 * By default, this file does not exist. Users can create this file and place their extension code
 * in this common location, removing the need to modify the Selenium sources, and hopefully assisting
 * with the upgrade process.
 */

Selenium.prototype.assertElementEnabled = function(locator) {
    // All locator-strategies are automatically handled by "findElement"
    var element = this.page().findElement(locator);
    if (element.disabled == true) {
        Assert.fail("Element " + locator + " is not enabled.");
    }
};

Selenium.prototype.assertElementDisabled = function(locator) {
    // All locator-strategies are automatically handled by "findElement"
    var element = this.page().findElement(locator);
    if (element.disabled == false) {
        Assert.fail("Element " + locator + " is not disabled.");
    }
};


