package ltd.icecold.vexmusic.config;

import ltd.icecold.vexmusic.VexMusic;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author ice_cold
 * @description
 * @date Create in 10:25 2020/8/3
 */
public class Config {
    private static String version;
    private static Boolean eula;
    private static YamlConfiguration yamlConfiguration;
    public static void init(){
        File configFile = new File(VexMusic.getInstance().getDataFolder(),"config.yml");
        yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
        version = yamlConfiguration.getString("plugin.version");
        eula = yamlConfiguration.getBoolean("plugin.eula");
    }

    public static Object get(String path){
        return yamlConfiguration.get(path);
    }

    public static String getVersion() {
        return version;
    }

    public static Boolean getEula() {
        return eula;
    }
}
