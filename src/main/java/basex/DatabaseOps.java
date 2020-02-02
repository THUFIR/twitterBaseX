package basex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import main.App;
import org.basex.build.xml.SAXWrapper;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.Databases;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.List;
import org.basex.core.cmd.Set;
import org.basex.core.cmd.XQuery;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.Open;
import org.basex.io.IOFile;

public class DatabaseOps {

    private static final Logger log = Logger.getLogger(DatabaseOps.class.getName());
    private Properties properties = new Properties();
    private URL url = null;
    private String databaseName = null;
    private Context context = null;
    private String parserType = null;
    private String stringQuery = "//note/body/text()";

    private DatabaseOps() {
    }

    public DatabaseOps(Properties properties) {
        this.properties = properties;
          }

    private void init() throws MalformedURLException, BaseXException, IOException {
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

    private void transform(String fileName) throws IOException {
        SAXWrapper xmlParser = org.basex.build.json.JsonParser.xmlParser(new IOFile(fileName));
//        String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        xmlParser.parse();
        String xml = xmlParser.toString();
        new Open(databaseName).execute(context);
        new Add(null, xml);
    }

    public void loadTweets(String fileName) throws IOException {
        init();
        drop();
        create();
        infoOnDatabases();
        //  transform(fileName);
        context.close();
    }

}
