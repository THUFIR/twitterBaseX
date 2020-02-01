package main;

import basex.DatabaseOps;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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

    private void baseX(List<JSONObject> tweets) throws MalformedURLException, IOException {
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

    private void twitterToBaseX() throws UnsupportedEncodingException, IOException {
        List<JSONObject> tweets = getTweets();
        log.fine(tweets.toString());
        convertJsonToXml("tweets.json", tweets);
        baseX(tweets);

    }

    private void convertJsonToXml(String fileName, List<JSONObject> tweets) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter BufferedWriter = null;
        fileOutputStream = new FileOutputStream(fileName);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
        BufferedWriter = new BufferedWriter(outputStreamWriter);
        BufferedWriter.write(tweets.toString());
    }

    public static void main(String... args) throws IOException {
        new App().twitterToBaseX();
    }

}
