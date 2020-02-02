package twitterBaseX;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.crypto.dsig.XMLObject;
import main.LoadProps;
import org.basex.core.BaseXException;
import org.json.XML;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConsumer {

    private static final Logger log = Logger.getLogger(TwitterConsumer.class.getName());
//    private List<JSONObject> tweets = new ArrayList<>();

    public TwitterConsumer() {
    }

    private TwitterFactory configTwitterFactory() throws IOException {
        LoadProps loadTwitterProps = new LoadProps("twitter");
        Properties properties = loadTwitterProps.loadProperties();
        log.fine(properties.toString());
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder.setDebugEnabled(true)
                .setJSONStoreEnabled(true)
                .setOAuthConsumerKey(properties.getProperty("oAuthConsumerKey"))
                .setOAuthConsumerSecret(properties.getProperty("oAuthConsumerSecret"))
                .setOAuthAccessToken(properties.getProperty("oAuthAccessToken"))
                .setOAuthAccessTokenSecret(properties.getProperty("oAuthAccessTokenSecret"));

        return new TwitterFactory(configurationBuilder.build());
    }

    public void consumeTweets(String user, String fileName) throws TwitterException, JSONException, NumberFormatException, IOException {
        Twitter twitter = configTwitterFactory().getInstance();

        List<JSONObject> tweets = new ArrayList<>();
        Query query = new Query(user);
        QueryResult result = twitter.search(query);
        String string = null;
        JSONObject json = null;
        //  long longID = 1224083010015956992L;
        String stringID = null;

        for (Status status : result.getTweets()) {
            string = TwitterObjectFactory.getRawJSON(status);
            json = new JSONObject(string);
//            stringID = (String) json.get("id");
            long l = (long) json.get("id");
            // Integer i = Integer.parseInt(stringID);
            //    log.info(i.toString());
            log.info(Long.toString(l));
        }
    }

    private void jsonOps(Status status) throws JSONException {
        String string = TwitterObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
        String foo = XML.toString(json);
        XMLObject x = null;
        // XMLObject xx = new XMLObject(json);
    }

    private JSONObject jsonOps2(Status status) throws JSONException, BaseXException {
        String string = TwitterObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
        String language = json.getString("lang");
        log.fine(language);
        return json;
    }

    private void xml(JSONObject tweet) {
        String foo = XML.toString(tweet);
    }

    public void writeJsonToFile(String fileName, List<JSONObject> tweets) {
        try (Writer fileWriter = new FileWriter(fileName)) {
            fileWriter.write(tweets.toString());
        } catch (IOException ex) {
            Logger.getLogger(TwitterConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
