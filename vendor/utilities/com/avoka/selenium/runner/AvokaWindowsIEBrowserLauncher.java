package com.avoka.selenium.runner;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;


public class AvokaWindowsIEBrowserLauncher implements AvokaBrowserLauncher {
    public AvokaWindowsIEBrowserLauncher() {
    }

    public void launch(String url, boolean visible) {
        explorer = new ActiveXComponent("clsid:0002DF01-0000-0000-C000-000000000046");
        Object ieObject = explorer.getObject();
        Dispatch.put(ieObject, "Visible", new Variant(visible));
        Dispatch.put(ieObject, "AddressBar", new Variant(true));
        Dispatch.put(ieObject, "StatusText", new Variant("Selenium Testing..."));
        Dispatch.call(ieObject, "Navigate", new Variant(url));
    }

    public void close() {
        explorer.invoke("Quit", new Variant[0]);
    }


    ActiveXComponent explorer;

}
