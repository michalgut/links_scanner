package com.jamfnow.qa.links;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CLIProperties {
    private static Properties props = null;

    public static synchronized String getVersion() {
        if (props == null) {
            try {
                props = new Properties();
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream is = classloader.getResourceAsStream("cli.properties");
                props.load(is);
            }
            catch (IOException e) {
                props = new Properties();
                props.put("versionNumber", "unknown");
            }
        }
        return (String) props.get("versionNumber");
    }
}
