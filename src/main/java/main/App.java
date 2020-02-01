package main;

import basex.DatabaseOps;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import twitterBaseX.TwitterOps;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    private void init() throws BaseXException, IOException, MalformedURLException {
        //    TwitterOps to = new TwitterOps(loadProperties("twitter"));
        Properties databaseProperties = new Properties();
        databaseProperties.loadFromXML(App.class.getResourceAsStream("/basex.xml"));
        DatabaseOps db = new DatabaseOps(databaseProperties);
        db.fetch();
    }

    private void initb() throws IOException {

        TwitterOps to = new TwitterOps();
    }

    public static void main(String... args) throws BaseXException, IOException, MalformedURLException {
        new App().init();
    }

}
