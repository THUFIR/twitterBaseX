package basex;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.Databases;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.List;
import org.basex.core.cmd.Set;
import org.basex.core.cmd.XQuery;
import twitter4j.JSONObject;
import org.basex.io.parse.json.JsonConverter;
import org.basex.build.json.JsonParser;

public class DatabaseOps {

    private static final Logger log = Logger.getLogger(DatabaseOps.class.getName());
    private Properties properties = new Properties();
    private URL url = null;
    private String databaseName = null;
    private Context context = null;
    private String parserType = null;

    private DatabaseOps() {
    }

    public DatabaseOps(Properties properties) {
        this.properties = properties;
        log.fine(properties.toString());
    }

    public void init() throws MalformedURLException, BaseXException {
        parserType = properties.getProperty("parserType");
        url = new URL(properties.getProperty(parserType + "URL"));
        databaseName = properties.getProperty("databaseName");
        context = new Context();
        list();
    }

    private void list() throws BaseXException {
        log.info(new List().execute(context));
    }

    private void drop() throws BaseXException {
        new Set("parser", parserType).execute(context);
        new DropDB(databaseName).execute(context);
        list();
    }

    private void create() throws BaseXException {
        new Set("parser", parserType).execute(context);
        new CreateDB(databaseName, url.toString()).execute(context);
        new List().execute(context);
        list();
    }

    private void infoOnDatabases() {
        Databases databases = context.databases;
        Iterator<String> databaseIterator = databases.list().iterator();

        String currentDatabaseName = null;
        while (databaseIterator.hasNext()) {
            currentDatabaseName = databaseIterator.next();
            log.fine(currentDatabaseName);
            //xQuery here..
        }
    }

    private void query(final String query) throws BaseXException {
        log.fine(new XQuery(query).execute(context));
    }

    public void fetch() throws BaseXException, MalformedURLException {
        init();
        drop();
        create();
        infoOnDatabases();
        list();
        query("//note/body/text()");
        context.close();
    }

    private void addTweetFromList(java.util.List<JSONObject> tweets) {

        JsonConverter jc;
        JsonParser jp;

    }

    public void addTweets(java.util.List<JSONObject> tweets) throws MalformedURLException, BaseXException {
        init();
        drop();
        create();
        infoOnDatabases();
        context.close();
    }

}


/*

               // create session
               final BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin");
               System.out.println(session.execute("info"));
*/