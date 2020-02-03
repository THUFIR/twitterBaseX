package basex;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.List;
import org.basex.core.cmd.Set;
import twitter4j.JSONArray;
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

    private void add(JSONArray tweets) throws JSONException, BaseXException {
        long id = 0L;
        String xmlStringTweet = null;
        new Open(databaseName).execute(context);
        twitter4j.JSONObject jsonTweet = null;
        org.json.JSONObject foo = null;
        String jsonStringTweet = null;
        JSONArray jsonArray = null;

       // int myJsonArraySize = tweets.size();

        for (int i = 0; i < tweets.length(); i++) {
            jsonStringTweet = tweets.get(i).toString();
            log.info(jsonStringTweet);
        }
    }

    private void add(JSONObject tweets) throws JSONException, BaseXException {
        /*
        long id = 0L;
        Iterator keys = tweets.keys();
        String xmlStringTweet = null;
        new Open(databaseName).execute(context);
        twitter4j.JSONObject jsonTweet = null;
        org.json.JSONObject foo = null;
        String jsonStringTweet = null;
        JSONArray jsonArray = null;

        while (keys.hasNext()) {
            id = Long.parseLong(keys.next().toString());
            log.info(Long.toString(id));
            jsonTweet = tweets.getJSONObject(Long.toString(id));
            jsonStringTweet = jsonTweet.toString();
            log.fine(jsonStringTweet);
            //    foo = new org.json.JSONObject("jsonStringTweet");
            xmlStringTweet = XML.toString(jsonTweet);
            log.fine(jsonTweet.toString());
            log.fine(xmlStringTweet);

            jsonArray = new JSONArray();
            jsonArray.put(jsonTweet);
            log.info(jsonArray.toString());
//            new Add(null, xmlStringTweet).execute(context);
        }

         */
    }

    private void create() throws BaseXException, JSONException {
        new Set("parser", parserType).execute(context);
        new CreateDB(databaseName).execute(context);
        new List().execute(context);
        list();
    }

    private void list() throws BaseXException {
        log.fine(new List().execute(context));
    }

    public void dropCreateAdd(JSONArray tweets) throws MalformedURLException, BaseXException, JSONException {
        init();
        drop();
        create();
        add(tweets);
        list();
    }

}
