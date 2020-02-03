package main;

import basex.DatabaseHelper;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import twitter4j.JSONException;
import twitter4j.TwitterException;
import twitterBaseX.TwitterConsumer;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());
    private final String user = "lizardbill";
    private final String fileName = "tweets.json";

    private void twitterToBaseX() throws TwitterException, IOException, JSONException {
        TwitterConsumer twitterConsumer = new TwitterConsumer();
        twitter4j.JSONObject foo = twitterConsumer.consumeTweets(user, fileName);

        Properties baseXprops = new Properties();
        baseXprops.loadFromXML(App.class.getResourceAsStream("/basex.xml"));
        DatabaseHelper db = new DatabaseHelper(baseXprops);
        db.persist(foo);
    }

    public static void main(String... args) throws TwitterException, IOException, JSONException {
        new App().twitterToBaseX();
    }
}
