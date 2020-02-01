package main;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class LoadProps {

    private static final Logger log = Logger.getLogger(LoadProps.class.getName());
    private String filePrefix = "";

    private LoadProps() {
    }

   public LoadProps(String filePrefix) {
        this.filePrefix = filePrefix;
    }

   
   /*
   
    private void propsOps() {
        Set<Object> keySet = properties.keySet();
        String key = null;
        String value = null;

        for (Object obj : keySet) {
            key = obj.toString();
            value = System.getenv(key);
            properties.setProperty(key, value);
        }
    }
   */
   
   public Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(LoadProps.class.getResourceAsStream("/" + filePrefix + ".xml"));

        Set<Object> keySet = properties.keySet();
        String key = null;
        String value = null;

        for (Object obj : keySet) {
            key = obj.toString();
            value = System.getenv(key);
            properties.setProperty(key, value);
        }
        return properties;
    }

}
