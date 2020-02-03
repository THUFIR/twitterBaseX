package basex;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.List;
import org.basex.core.cmd.Set;
import org.json.XML;
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

    private void create(String fileName) throws BaseXException {
        new Set("parser", parserType).execute(context);
        log.info(databaseName);
        log.info(fileName);
        String filePath = properties.getProperty("jsonData");
        log.info(filePath);
        //    CreateDB createDB = new CreateDB(databaseName, filePath);
//        new CreateDB(databaseName, url.toString()).execute(context);
//        new CreateDB(databaseName, url.toString()).execute(context);
        CreateDB createDB = new CreateDB(databaseName, filePath);
        new List().execute(context);
        list();
    }

    private void iterate(JSONObject json) {
        String stringXml = XML.toString(json);
        long id = 0L;
        Iterator keys = json.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            String s = key.toString();
            id = Long.parseLong(s);
            log.info("long\t\t" + Long.toString(id));
        }
    }

    private void create(JSONObject json) throws BaseXException {
        new Set("parser", parserType).execute(context);
        String s = json.toString();
        CreateDB createDB = new CreateDB(databaseName, s);
        new List().execute(context);
        list();
        iterate(json);
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

    public void persist(String fileName) throws MalformedURLException, BaseXException {
        init();
        drop();
        create(fileName);
        //  log.fine(json.toString());
    }

}
