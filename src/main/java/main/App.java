package main;

import basex.DatabaseOps;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import twitter4j.JSONException;
import twitter4j.TwitterException;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());
    private final String user = "lizardbill";
    private final String fileName = "tweets.json";

    private void twitterToBaseX() throws TwitterException, IOException, JSONException {
        //  TwitterConsumer twitterConsumer = new TwitterConsumer();
        //   twitterConsumer.consumeTweets(user, fileName);

        Properties baseXprops = new Properties();
        baseXprops.loadFromXML(App.class.getResourceAsStream("/basex.xml"));

        DatabaseOps db = new DatabaseOps(baseXprops);
        db.loadTweets(fileName);
    }

    public static void main(String... args) throws TwitterException, IOException, JSONException {
        new App().twitterToBaseX();
    }
}
