package ltd.icecold.vexmusic.config;

import ltd.icecold.vexmusic.VexMusic;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Language {
    public static YamlConfiguration yamlConfiguration;
    public static void init(){
        File languageFile = new File(VexMusic.getInstance().getDataFolder(),"language.yml");
        yamlConfiguration = YamlConfiguration.loadConfiguration(languageFile);
    }
    public static String getLang(String path){
        return yamlConfiguration.getString(path);
    }
}
