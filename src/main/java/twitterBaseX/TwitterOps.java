package twitterBaseX;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import main.LoadProps;
import org.basex.core.BaseXException;
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

public class TwitterOps {

    private static final Logger log = Logger.getLogger(TwitterOps.class.getName());
    private List<JSONObject> tweets = new ArrayList<>();

    public TwitterOps() {
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

    public void consumeTweets(String user, String fileName) throws TwitterException, IOException, JSONException {
        Twitter twitter = configTwitterFactory().getInstance();

        Query query = new Query(user);
        QueryResult result = twitter.search(query);
        String string = null;
        JSONObject tweet = null;

        for (Status status : result.getTweets()) {
            tweet = jsonOps(status);
            tweets.add(tweet);
        }
        writeJsonToFile(fileName);
    }

    private JSONObject jsonOps(Status status) throws JSONException, BaseXException {
        String string = TwitterObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
        String language = json.getString("lang");
        log.fine(language);
        return json;
    }

    public void writeJsonToFile(String fileName) throws FileNotFoundException, UnsupportedEncodingException, IOException {
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
}
