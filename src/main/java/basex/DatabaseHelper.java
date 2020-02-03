package basex;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.List;
import org.basex.core.cmd.Set;
import org.json.XML;
import twitter4j.JSONArray;
import twitter4j.JSONException;

public class DatabaseHelper {

    private static final Logger log = Logger.getLogger(DatabaseHelper.class.getName());
    private Properties properties = new Properties();
    private URL url = null;
    private String databaseName = null;
    private Context context = null;
    private String parserType = null;

    private DatabaseHelper() {
    }

    public DatabaseHelper(Properties properties) {
        this.properties = properties;
    }

    private void init() throws MalformedURLException, BaseXException {
        log.fine(properties.toString());
        parserType = properties.getProperty("parserType");
        url = new URL(properties.getProperty(parserType + "URL"));
        databaseName = properties.getProperty("databaseName");
        context = new Context();
        list();
    }

    private void list() throws BaseXException {
        log.fine(new List().execute(context));
    }

    private void drop() throws BaseXException {
        new Set("parser", parserType).execute(context);
        new DropDB(databaseName).execute(context);
        list();
    }

    private void create() throws BaseXException, JSONException {
        new Set("parser", parserType).execute(context);
        new CreateDB(databaseName).execute(context);
        new List().execute(context);
        list();
    }

    private void add(JSONArray tweets) throws JSONException, BaseXException {
        long id = 0L;
        String jsonStringTweet = null;
        org.json.JSONObject jsonObjectTweet = null;
        String stringXml = null;

        new Open(databaseName).execute(context);
        for (int i = 0; i < tweets.length(); i++) {
            jsonStringTweet = tweets.get(i).toString();
            jsonObjectTweet = new org.json.JSONObject(jsonStringTweet);
            stringXml = XML.toString(jsonObjectTweet);
            log.info(stringXml);
            new Add(null, stringXml).execute(context);
        }
    }

    public void dropCreateAdd(JSONArray tweets) throws MalformedURLException, BaseXException, JSONException {
        init();
        drop();
        create();
        add(tweets);
        list();
    }

}
