package ltd.icecold.vexmusic.client.jmusic;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class Download implements Runnable{

    private String url;
    private String saveDir;
    private String fileName;

    public Download(String url, String saveDir, String fileName) {
        this.url = url;
        this.saveDir = saveDir;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            FileUtils.copyURLToFile(new URL(url), new File(saveDir, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}