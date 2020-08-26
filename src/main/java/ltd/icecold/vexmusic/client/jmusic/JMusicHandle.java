package ltd.icecold.vexmusic.client.jmusic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ice_cold
 * @date Create in 8:46 2020/8/4
 */
public class JMusicHandle {
    public static List<JPlayer> playerList = new ArrayList<>();
    public static void PluginMessageHandle(String message){
        String type = new JsonParser().parse(message).getAsJsonObject().get("type").getAsString();
        if ("bgm".equals(type)){
            JsonObject message1 = new JsonParser().parse(message).getAsJsonObject().get("message").getAsJsonObject();
            String type1 = message1.get("type").getAsString();
            String music = message1.get("music").getAsString();
            boolean loop = message1.get("loop").getAsBoolean();
            if("web".equals(type1)){
            }
        }
    }
}
