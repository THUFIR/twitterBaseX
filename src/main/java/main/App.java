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
import java.util.logging.Logger;
import org.basex.build.json.JsonParser;
import org.basex.build.xml.SAXWrapper;
import org.basex.core.MainOptions;
import org.basex.io.IOFile;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.TwitterException;
import twitterBaseX.TwitterOps;


public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    private void baseX(List<JSONObject> tweets) throws MalformedURLException, IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.loadFromXML(App.class.getResourceAsStream("/basex.xml"));
        DatabaseOps db = new DatabaseOps(databaseProperties);
        db.addTweets(tweets);
    }

    private List<JSONObject> getTweets() throws TwitterException, IOException, JSONException {
        List<JSONObject> tweets = new ArrayList<>();
        tweets = new TwitterOps().getTweets();
        log.fine("tweets\t\t" + tweets.size());
        return tweets;
    }

    private void twitterToBaseX() throws UnsupportedEncodingException, IOException, TwitterException, JSONException {
        List<JSONObject> tweets = getTweets();
        String fileName = "tweets.json";
        writeJsonToFile(fileName, tweets);
        baseX(fileName);
    }

    private void writeJsonToFile(String fileName, List<JSONObject> tweets) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        fileOutputStream = new FileOutputStream(fileName);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
        bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write(tweets.toString());
        bufferedWriter.close();
        outputStreamWriter.close();
        fileOutputStream.close();
    }

    private void baseX(String fileName) throws IOException {
        IOFile iof = new IOFile(fileName);
        MainOptions opts = new MainOptions();
        JsonParser jsonParser = new JsonParser(iof, opts);
        log.info("file has been read...");
        SAXWrapper saxWrapper = JsonParser.xmlParser(iof);
        log.info("parsed...I guess...");
    }

    public static void main(String... args) throws IOException, UnsupportedEncodingException, TwitterException, JSONException {
        new App().twitterToBaseX();
    }

}
