package basex;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.Databases;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.List;
import org.basex.core.cmd.Set;
import org.basex.core.cmd.XQuery;
import twitterBaseX.TwitterOps;

public class DatabaseOps {

    private static final Logger LOG = Logger.getLogger(DatabaseOps.class.getName());
    private Properties properties = new Properties();
    private URL url = null;
    private String databaseName = null;
    private Context context = null;
    private String parserType = null;

    private DatabaseOps() {
    }

    public DatabaseOps(Properties properties) {
        this.properties = properties;
        LOG.info(properties.toString());
    }

    public void init() throws MalformedURLException {
        parserType = properties.getProperty("parserType");
        url = new URL(properties.getProperty(parserType + "URL"));
        databaseName = properties.getProperty("databaseName");
        context = new Context();
    }

    private void list() {
        try {
            LOG.fine(new List().execute(context));
        } catch (BaseXException ex) {
            Logger.getLogger(DatabaseOps.class.getName()).log(Level.FINE, null, ex);
        }
    }

    private void drop() {
        try {
            new Set("parser", parserType).execute(context);
            new DropDB(databaseName).execute(context);
        } catch (BaseXException ex) {
            Logger.getLogger(DatabaseOps.class.getName()).log(Level.INFO, null, ex);
        }
        list();
    }

    private void create() {
        try {
            new Set("parser", parserType).execute(context);
            new CreateDB(databaseName, url.toString()).execute(context);
            new List().execute(context);
        } catch (BaseXException ex) {
            Logger.getLogger(DatabaseOps.class.getName()).log(Level.INFO, null, ex);
        }
        list();
    }

    private void infoOnDatabases() {
        Databases databases = context.databases;
        Iterator<String> databaseIterator = databases.list().iterator();

        String currentDatabaseName = null;
        while (databaseIterator.hasNext()) {
            currentDatabaseName = databaseIterator.next();
            LOG.info(currentDatabaseName);
            //xQuery here..
        }

    }

    private void query(final String query) {
        try {
            LOG.fine(new XQuery(query).execute(context));
        } catch (BaseXException ex) {
            Logger.getLogger(DatabaseOps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fetch() {
        drop();
        create();
        infoOnDatabases();
        list();
        query("//note/body/text()");
        context.close();
    }

}
