package main;

import basex.DatabaseOps;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.TwitterException;
import twitterBaseX.TwitterOps;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    private void baseX(List<JSONObject> tweets) throws MalformedURLException, IOException  {
        //    TwitterOps to = new TwitterOps(loadProperties("twitter"));
        Properties databaseProperties = new Properties();
        databaseProperties.loadFromXML(App.class.getResourceAsStream("/basex.xml"));
        DatabaseOps db = new DatabaseOps(databaseProperties);
      //  db.fetch();
      db.addTweets(tweets);
    }

    private List<JSONObject> getTweets() {
        List<JSONObject> tweets = new ArrayList<>();
        try {
            tweets = new TwitterOps().getTweets();
        } catch (TwitterException | IOException | JSONException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("tweets\t\t" + tweets.size());
        return tweets;
    }

    private void twitterToBaseX() {
        
        List<JSONObject> tweets = getTweets();
        
        try {
            baseX(tweets);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String... args) {
        new App().twitterToBaseX();
    }

}
