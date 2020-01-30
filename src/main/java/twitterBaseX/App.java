package twitterBaseX;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
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

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    private Properties loadProperties(String filePrefix) throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(App.class.getResourceAsStream("/" + filePrefix + ".xml"));

        Set<Object> keySet = properties.keySet();
        String key = null;
        String value = null;

        for (Object obj : keySet) {
            key = obj.toString();
            value = System.getenv(key);
            properties.setProperty(key, value);
        }
        return properties;
    }

    private TwitterFactory configTwitterFactory(Properties properties) throws IOException {
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
        Twitter twitter = configTwitterFactory(loadProperties("twitter")).getInstance();
        Query query = new Query("lizardbill");
        QueryResult result = twitter.search(query);
        String string = null;
        JSONObject JSON_complete = null;
        for (Status status : result.getTweets()) {
            insertStatus(status);
        }

    }


    /*
    
      // Content of the new document
      String doc = "<xml>Hello World!</xml>";

      System.out.println("\n* Create new resource.");

      // Create a new XML resource with the specified ID
      XMLResource res = (XMLResource) col.createResource(id,
          XMLResource.RESOURCE_TYPE);

      // Set the content of the XML resource as the document
      res.setContent(doc);

      System.out.println("\n* Store new resource.");

      // Store the resource into the database
      col.storeResource(res);
     */
 /*
    TwitterFactory tf = new TwitterFactory(cb.build());
Twitter twitter = tf.getInstance();
String jsonStatus; //Content read from a file
Status status = TwitterObjectFactory.createStatus(jsonStatus); //your status
User u2 = t.showUser(status.getUser().getScreenName()); // you use the twitter object once
String jsonUser = TwitterObjectFactory.getRawJSON(status.getUser()); //now you won't get the error
System.out.print(jsonUser); //your happy json
     */
    private void insertStatus(Status status) throws JSONException {

        String string = TwitterObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
        String language = json.getString("lang");
        log.info(json.toString());

        /*
        String string = DataObjectFactory.getRawJSON(status);
        JSONObject json = new JSONObject(string);
        String language = json.getString("lang");
        log.info(json.toString());
         */
        //    XMLResource res = (XMLResource) col.createResource(id, XMLResource.RESOURCE_TYPE);
    }

    public static void main(String[] args) throws TwitterException, IOException, JSONException {
        new App().twitterQueryResult();
    }
}
