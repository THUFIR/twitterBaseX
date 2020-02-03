package basex;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.List;
import org.basex.core.cmd.Set;
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

    public DatabaseHelper(Properties properties) {
        this.properties = properties;
    }

    private void init() throws MalformedURLException, BaseXException {
        log.info(properties.toString());
        parserType = properties.getProperty("parserType");
        url = new URL(properties.getProperty(parserType + "URL"));
        databaseName = properties.getProperty("databaseName");
        context = new Context();
        list();
    }

    private void drop() throws BaseXException {
        new Set("parser", parserType).execute(context);
        new DropDB(databaseName).execute(context);
        list();
    }

    private void create(JSONObject json) throws BaseXException {
        new Set("parser", parserType).execute(context);
        String s = json.toString();
        CreateDB createDB = new CreateDB(databaseName, s);
        new List().execute(context);
        list();
    }

    private void list() throws BaseXException {
        log.info(new List().execute(context));
    }

    public void persist(JSONObject json) throws MalformedURLException, BaseXException {
        init();
        drop();
        create(json);
        log.fine(json.toString());
    }
}
