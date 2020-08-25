package ltd.icecold.vexmusic.events;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ltd.icecold.vexmusic.config.Language;
import ltd.icecold.vexmusic.utils.PluginMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ice_cold
 * @date Create in 11:03 2020/8/8
 */
public class MessageListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        String jsonMsg = PluginMessage.read(message);
        if (!validate(jsonMsg)) {
            return;
        }
        String type = new JsonParser().parse(jsonMsg).getAsJsonObject().get("type").getAsString();
        if ("musicEnd".equals(type)){
            String music = new JsonParser().parse(jsonMsg).getAsJsonObject().get("message").getAsString();
            player.sendMessage(Language.getLang("language.lang20").replace("${MUSIC_NAME}",music));
        }
    }

    /**
     * 判断是否为json数据，防止异常
     * @param json json数据
     * @return 是否为json数据格式
     */
    public static boolean validate(String json) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(json);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        return jsonElement.isJsonObject();
    }
}
