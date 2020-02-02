package basex;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.basex.build.json.JsonParser;
import org.basex.core.MainOptions;
import org.basex.io.IOFile;
import org.json.JSONObject;
import org.json.XML;

public class JsonToXmlTransformer {

    private static final Logger log = Logger.getLogger(JsonToXmlTransformer.class.getName());

    public JsonToXmlTransformer() {
    }

    private void baseXparseJsonFile(String fileName) throws IOException {
        JsonParser jsonParser = new JsonParser(new IOFile(fileName), new MainOptions());
    }

    public void transform(String fileName) {
        File file = new File(fileName);
        JSONObject json = new JSONObject(file);
        String xml = XML.toString(json);
        log.fine(xml);
    }

}
