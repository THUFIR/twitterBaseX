package basex;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Command;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.List;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.Set;
import org.basex.core.cmd.XQuery;
import org.basex.io.in.ArrayInput;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

public class DatabaseHelper {

    private static final Logger log = Logger.getLogger(DatabaseHelper.class.getName());
    private Properties properties = new Properties();
    private URL url = null;
    private String databaseName = null;
    private Context context = null;
    private String parserType = null;

    private DatabaseHelper() {
    }

    public DatabaseHelper(Properties properties) {
        this.properties = properties;
    }

    private String wrap(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuilder.append("<root>");
        stringBuilder.append(string);
        stringBuilder.append("</root>");
        return stringBuilder.toString();
    }

    private void init() throws MalformedURLException, BaseXException {
        log.fine(properties.toString());
        parserType = properties.getProperty("parserType");
        url = new URL(properties.getProperty(parserType + "URL"));
        databaseName = properties.getProperty("databaseName");
        context = new Context();
        list();
    }

    private void list() throws BaseXException {
        log.fine(new List().execute(context));
    }

    private void drop() throws BaseXException {
        new Set("parser", parserType).execute(context);
        new DropDB(databaseName).execute(context);
        list();
    }

    private void create() throws BaseXException, JSONException {
        new Set("parser", parserType).execute(context);
        new CreateDB(databaseName).execute(context);
        new List().execute(context);
        list();
    }

    private void add(JSONArray tweets) throws JSONException, BaseXException, IOException {
        log.fine(tweets.toString());
        JSONObject tweet = null;
        long id = 0L;
        XQuery xquery = new XQuery(".");
        new Open(databaseName).execute(context);
        new Set("parser", "json").execute(context);
        Command add = null;

        for (int i = 0; i < tweets.length(); i++) {
            tweet = new JSONObject(tweets.get(i).toString());
            id = Long.parseLong(tweet.get("id_str").toString());
            add = new Add(id + ".xml");
            add.setInput(new ArrayInput(tweet.toString()));
            add.execute(context);
        }
        log.fine(xquery.execute(context).toString());
    }

    public void dropCreateAdd(JSONArray tweets) throws MalformedURLException, BaseXException, JSONException, IOException {
        init();
        // drop();
        //  create();
        add(tweets);
        list();
    }

    private void write(String tweet, String fileName) throws IOException {
        log.fine(tweet);
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(tweet);
        fileWriter.close();
    }

    private String read(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        return content;
    }
}
