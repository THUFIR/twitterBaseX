package main;

import basex.DatabaseOps;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
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

    private List<JSONObject> getTweets() throws TwitterException, IOException, JSONException {
        List<JSONObject> tweets = new TwitterOps().getTweets();
        return tweets;
    }

    private void init() {
        List<JSONObject> tweets = new ArrayList<>();
        try {
            tweets = getTweets();
        } catch (TwitterException | IOException | JSONException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info(tweets.toString());
    }

    public static void main(String... args)  {
        new App().init();
    }

}
