package twitterBaseX;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.LoadProps;
import org.basex.core.BaseXException;
import twitter4j.JSONArray;
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
    private JSONArray tweets = new JSONArray();

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

    private JSONObject getTweetFromStatus(Status status) throws JSONException {
        String string = TwitterObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
        return json;
    }

    private void populateTweets(Status status) throws JSONException {
        JSONObject tweet = getTweetFromStatus(status);
        long l = (long) tweet.get("id");
//        tweets.put(Long.toString(l), tweet);
        tweets.put(tweet);
    }

    public JSONArray consumeTweets(String user, String fileName) throws TwitterException, JSONException, NumberFormatException, IOException {
        Twitter twitter = configTwitterFactory().getInstance();

        Query query = new Query(user);
        QueryResult result = twitter.search(query);

        for (Status status : result.getTweets()) {
            populateTweets(status);
        }
        return tweets;
    }

    private void jsonOps(Status status) throws JSONException {
        String string = TwitterObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
      //  String foo = XML.toString(json);
    //    XMLObject x = null;
        // XMLObject xx = new XMLObject(json);
    }

    private JSONObject jsonOps2(Status status) throws JSONException, BaseXException {
        String string = TwitterObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
        String language = json.getString("lang");
        log.fine(language);
        return json;
    }

    public void writeJsonToFile(String fileName, List<JSONObject> tweets) {
        try (Writer fileWriter = new FileWriter(fileName)) {
            fileWriter.write(tweets.toString());
        } catch (IOException ex) {
            Logger.getLogger(TwitterConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
