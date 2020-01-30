
import basex.DatabaseOps;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    private Properties loadProperties(String filePrefix) throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(App.class.getResourceAsStream("/" + filePrefix + ".xml"));

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

    private void init() throws IOException {
    //    TwitterOps to = new TwitterOps(loadProperties("twitter"));
        Properties databaseProperties = new Properties();
        databaseProperties.loadFromXML(App.class.getResourceAsStream("/basex.xml"));
        DatabaseOps db = new DatabaseOps(databaseProperties);
    }

    public static void main(String... args) throws IOException {
        new App().init();
    }

}
