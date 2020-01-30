package twitterBaseX;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
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
    private Properties properties = new Properties();

    private TwitterOps() {
    }

    TwitterOps(Properties properties) {
        this.properties = properties;
    }

    private void propsOps() {
        Set<Object> keySet = properties.keySet();
        String key = null;
        String value = null;

        for (Object obj : keySet) {
            key = obj.toString();
            value = System.getenv(key);
            properties.setProperty(key, value);
        }
    }

    private TwitterFactory configTwitterFactory() throws IOException {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder.setDebugEnabled(true)
                .setJSONStoreEnabled(true)
                .setOAuthConsumerKey(properties.getProperty("oAuthConsumerKey"))
                .setOAuthConsumerSecret(properties.getProperty("oAuthConsumerSecret"))
                .setOAuthAccessToken(properties.getProperty("oAuthAccessToken"))
                .setOAuthAccessTokenSecret(properties.getProperty("oAuthAccessTokenSecret"));

        return new TwitterFactory(configurationBuilder.build());
    }

    private void twitterQueryResult() throws TwitterException, IOException, JSONException {
        Twitter twitter = configTwitterFactory().getInstance();

        Query query = new Query("lizardbill");
        QueryResult result = twitter.search(query);
        String string = null;
        JSONObject JSON_complete = null;
        for (Status status : result.getTweets()) {
            jsonOps(status);
        }

    }

    private void jsonOps(Status status) throws JSONException, BaseXException {
        String string = TwitterObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
        String language = json.getString("lang");
        log.info(language);

        Context context = new Context();

        new CreateDB("DBExample", "src/main/resources/xml/input.xml").execute(context);

        //    XMLResource res = (XMLResource) col.createResource(id, XMLResource.RESOURCE_TYPE);
    }

}
