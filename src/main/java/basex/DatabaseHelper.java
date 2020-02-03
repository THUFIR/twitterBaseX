package basex;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
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
import twitter4j.JSONException;
import twitter4j.JSONObject;

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

    private void drop() throws BaseXException {
        new Set("parser", parserType).execute(context);
        new DropDB(databaseName).execute(context);
        list();
    }

    private void add(JSONObject tweets) throws JSONException, BaseXException {
        String stringXml = XML.toString(tweets);
        long id = 0L;
        Iterator keys = tweets.keys();
        String xmlStringTweet = null;
        new Open(databaseName).execute(context);
        while (keys.hasNext()) {
            id = Long.parseLong(keys.next().toString());
            JSONObject tweet = tweets.getJSONObject(Long.toString(id));
            xmlStringTweet = XML.toString(tweet);
            log.info(stringXml);
            new Add(databaseName, xmlStringTweet).execute(context);
        }
    }

    private void create() throws BaseXException, JSONException {
        new Set("parser", parserType).execute(context);
        new CreateDB(databaseName).execute(context);
        new List().execute(context);
        list();
    }

    private void list() throws BaseXException {
        log.info(new List().execute(context));
    }

    public void persist(JSONObject tweets) throws MalformedURLException, BaseXException, JSONException {
        init();
        drop();
        create();
        add(tweets);
    }

}
