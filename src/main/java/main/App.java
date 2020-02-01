package main;

import basex.DatabaseOps;
import java.io.BufferedWriter;
import java.io.File;
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
import org.basex.core.BaseXException;
import org.basex.core.MainOptions;
import org.basex.io.IOFile;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.TwitterException;
import twitterBaseX.TwitterOps;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    private void getTweets(String user, String fileName) throws TwitterException, IOException, JSONException {
        TwitterOps to = new TwitterOps();
        to.consumeTweets(user, fileName);
    }

    private void baseX(String fileName) throws MalformedURLException, BaseXException, IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.loadFromXML(App.class.getResourceAsStream("/basex.xml"));
        DatabaseOps db = new DatabaseOps(databaseProperties);
        JsonParser jsonParser = new JsonParser(new IOFile(fileName), new MainOptions());
    }

    private void twitterToBaseX() throws TwitterException, IOException, JSONException {
        getTweets("lizardbill", "tweets.json");
        baseX("tweets.json");
    }

    public static void main(String... args) throws IOException, UnsupportedEncodingException, TwitterException, JSONException {
        new App().twitterToBaseX();
    }
}
