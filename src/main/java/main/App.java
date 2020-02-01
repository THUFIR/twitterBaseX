package main;

import basex.DatabaseOps;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.TwitterException;
import twitterBaseX.TwitterOps;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    private void basex() throws BaseXException, IOException, MalformedURLException {
        //    TwitterOps to = new TwitterOps(loadProperties("twitter"));
        Properties databaseProperties = new Properties();
        databaseProperties.loadFromXML(App.class.getResourceAsStream("/basex.xml"));
        DatabaseOps db = new DatabaseOps(databaseProperties);
        db.fetch();
    }

    private void twitter() throws TwitterException, IOException, JSONException {
        List<JSONObject> tweets = new TwitterOps().getTweets();
    }

    private void init() {
    }

    public static void main(String... args) throws BaseXException, IOException, MalformedURLException {
        new App().init();
    }

}
