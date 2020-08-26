package ltd.icecold.vexmusic.classloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class EngineClassLoader extends URLClassLoader {

    public EngineClassLoader() {
        this(getSystemClassLoader());
    }

    public EngineClassLoader(ClassLoader parent) {
        super(new URL[] {}, parent);
    }

    public void addURL(URL... urls) {
        if (urls != null) {
            for (URL url : urls) {
                super.addURL(url);
            }
        }
    }

    public void addFile(File... files) throws IOException {
        if (files != null) {
            for (File file : files) {
                if (file != null) {
                    super.addURL(file.toURI().toURL());
                }
            }
        }
    }
}
