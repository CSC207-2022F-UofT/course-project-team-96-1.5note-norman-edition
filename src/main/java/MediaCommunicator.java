

public class MediaCommunicator {
    private Page page;
    private Storage storage;
    public Page getPage(){
        return this.page;
    }

    public byte[] readFile(String path){
        return this.storage.readFile(path);
    }

    public void writeFile(String path, Byte[] Data){
        this.storage.writeFile(path, Data);
    }
}