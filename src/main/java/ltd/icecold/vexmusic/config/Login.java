package ltd.icecold.vexmusic.config;

import ltd.icecold.vexmusic.VexMusic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


/**
 * @author ice_cold
 */
public class Login {
    private static String username;
    private static String code;
    private static String wyPhone;
    private static String wyUserName;
    private static String wyPassword;

    public static void init(){
        File loginFile = new File(VexMusic.getInstance().getDataFolder() , "login.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(loginFile);
        username = yamlConfiguration.getString("vexmusic.username");
        code = yamlConfiguration.getString("vexmusic.code");
        wyPhone = yamlConfiguration.getString("netease.phone");
        wyUserName = yamlConfiguration.getString("netease.email");
        wyPassword = yamlConfiguration.getString("netease.md5Password");
    }

    public static String getUsername() {
        return username;
    }

    public static String getCode() {
        return code;
    }

    public static String getWyUserName() {
        return wyUserName;
    }

    public static String getWyPassword() {
        return wyPassword;
    }

    public static String getWyPhone() {
        return wyPhone;
    }
}
