import app.media.Page;
import storage.Storage;

public class MediaCommunicator {
    private Page page;
    private Storage storage;
    public Page getPage(){
        return this.page;
    }

    public byte[] readFile(String[] extensions){
        return this.storage.readFile(extensions, "");
    }

    public void writeFile(String path, byte[] Data){
        this.storage.writeFile(path, Data);
    }
}
