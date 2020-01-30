
import basex.DatabaseOps;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import twitterBaseX.TwitterOps;

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

    private static void main(String... args) throws IOException {
        new App().init();
    }

    private void init() throws IOException {
        TwitterOps to = new TwitterOps(loadProperties("twitter"));
        // DatabaseOps db = new DatabaseOps(new Properties.loadFromXML(App.class.getResourceAsStream("basex.xml")));
        Properties databaseProperties = null;
        databaseProperties.loadFromXML(App.class.getResourceAsStream("foo.xml"));
        DatabaseOps  db = new DatabaseOps(databaseProperties);
    }

}
