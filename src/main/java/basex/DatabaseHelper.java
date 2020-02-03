package basex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import org.basex.core.Context;
import twitter4j.JSONObject;

public class DatabaseHelper {
    
    private static final Logger log = Logger.getLogger(DatabaseHelper.class.getName());
    private Properties properties = new Properties();
    private URL url = null;
    private String databaseName = null;
    private Context context = null;
    private String parserType = null;
    private String stringQuery = "//note/body/text()";
    
    private DatabaseHelper() {
    }
    
    public DatabaseHelper(Properties properties) throws MalformedURLException {
        this.properties = properties;
        parserType = properties.getProperty("parserType");
        url = new URL(properties.getProperty(parserType + "URL"));
        databaseName = properties.getProperty("databaseName");
        log.info(properties.toString());
    }
    
    public void persist(JSONObject foo) {
        log.info(foo.toString());
    }
    
}
