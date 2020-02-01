package twitterBaseX;

import java.io.IOException;
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

    public List<JSONObject> getTweets() throws TwitterException, IOException, JSONException {
        Twitter twitter = configTwitterFactory().getInstance();

        Query query = new Query("lizardbill");
        QueryResult result = twitter.search(query);
        String string = null;
        JSONObject tweet = null;
        List<JSONObject> tweets = new ArrayList<>();

        for (Status status : result.getTweets()) {
            tweet = jsonOps(status);
            tweets.add(tweet);
        }
        return tweets;
    }

    private JSONObject jsonOps(Status status) throws JSONException, BaseXException {
        String string = TwitterObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
        String language = json.getString("lang");
        log.fine(language);
        return json;
    }
    
    /*
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(fileName);
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write(rawJSON);
    */

}
