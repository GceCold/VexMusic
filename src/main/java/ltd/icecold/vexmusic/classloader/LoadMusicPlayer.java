package ltd.icecold.vexmusic.classloader;

import ltd.icecold.vexmusic.utils.IOUtil;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author ice_cold
 * @date Create in 11:22 2020/8/5
 */
public class LoadMusicPlayer {
    public static Class<?> classController;
    public static void start(String port) throws Exception {
        IOUtil.saveResource("PlayerWindow-1.0.jar",false);
        File playerJar = new File("PlayerWindow-1.0.jar");
        if (playerJar.exists()){
            EngineClassLoader loader1 = new EngineClassLoader();
            loader1.addURL(new File("PlayerWindow-1.0.jar").toURI().toURL());
            Class<?> classMain = loader1.loadClass("ltd.icecold.vexmusic.Main");
            Method mainMethod = classMain.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{new String[]{port}});
            classController = loader1.loadClass("ltd.icecold.vexmusic.gui.Controller");
        }
    }
}
