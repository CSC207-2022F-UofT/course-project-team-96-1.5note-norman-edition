import app.media.Page;
import storage.Storage;

import java.util.HashMap;

public class MediaCommunicator {
    private Page page;
    private Storage storage;
    public Page getPage(){
        return this.page;
    }

    public HashMap<String, byte[]> readFile(String[] extensions) throws Exception{
        return this.storage.readFile(extensions, "");
    }

    public void writeFile(String path, byte[] Data) throws  Exception{
        this.storage.writeFile(path, Data, "");
    }
}
