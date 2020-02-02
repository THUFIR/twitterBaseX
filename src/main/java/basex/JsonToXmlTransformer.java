package basex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;
import org.basex.core.MainOptions;
import org.basex.io.IOFile;

public class JsonToXmlTransformer {

    private static final Logger log = Logger.getLogger(JsonToXmlTransformer.class.getName());

    public JsonToXmlTransformer() {
    }

    private void baseXparseJsonFile(String fileName) throws IOException   {
        org.basex.build.json.JsonParser jsonParser = new org.basex.build.json.JsonParser(new IOFile(fileName), new MainOptions());
        //where is the xml?
    }

    public void transform(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        org.json.JSONObject json = new org.json.JSONObject(content);
        log.info(org.json.XML.toString(json));
    }
}
