package basex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;
import org.basex.build.xml.SAXWrapper;
import org.basex.core.MainOptions;
import org.basex.io.IOFile;

public class JsonToXmlTransformer {

    private static final Logger log = Logger.getLogger(JsonToXmlTransformer.class.getName());

    public JsonToXmlTransformer() {
    }

    private void baseXparseJsonFile(String fileName) throws IOException {
        org.basex.build.json.JsonParser jsonParser = new org.basex.build.json.JsonParser(new IOFile(fileName), new MainOptions());

        SAXWrapper foo = org.basex.build.json.JsonParser.xmlParser(new IOFile(fileName));
        foo.parse();
        String bar = foo.toString();
        log.fine(bar);
    }

    public void transform(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        org.json.JSONObject json = new org.json.JSONObject(content);
        log.fine(org.json.XML.toString(json));
    }
}
