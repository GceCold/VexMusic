package ltd.icecold.vexmusic.music;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ltd.icecold.vexmusic.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MusicMessage {
    private String json;
    private boolean goodData = true;
    public MusicMessage(String json, CommandSender from){
        this.json = json;
        if (json.isEmpty()){
            goodData = false;
            return;
        }
        String code = new JsonParser().parse(json).getAsJsonObject().get("code").getAsString();
        if (!code.equals("200")){
            String errorType = new JsonParser().parse(json).getAsJsonObject().get("type").getAsString();
            if ("SEARCH_ERROR".equals(errorType) || "SEARCH_0".equals(errorType) || "URL_ERROR".equals(errorType)){
                from.sendMessage(Language.getLang("language.lang23"));
            }
            if (code.equals("502")){
                for (OfflinePlayer offlinePlayer:Bukkit.getOperators()){
                    offlinePlayer.getPlayer().sendMessage("[VexMusic] >§c请检查插件使用期或是否使用正版插件！");
                }
            }
            goodData = false;
        }

    }
    public String getMusicName() {
        if (goodData){
            JsonObject musicData = new JsonParser().parse(json).getAsJsonObject().get("data").getAsJsonObject();
            return musicData.get("musicName").getAsString();
        }
        return "";
    }

    public String getMusicId() {
        if (goodData){
            JsonObject musicData = new JsonParser().parse(json).getAsJsonObject().get("data").getAsJsonObject();
            return musicData.get("musicID").getAsString();
        }
        return "";
    }

    public String getMusicArtists() {
        if (goodData){
            JsonObject musicData = new JsonParser().parse(json).getAsJsonObject().get("data").getAsJsonObject();
            return musicData.get("musicArtists").getAsString();
        }
        return "";
    }

    public String getMusicUrl() {
        if (goodData){
            JsonObject musicData = new JsonParser().parse(json).getAsJsonObject().get("data").getAsJsonObject();
            return musicData.get("musicURL").getAsString();
        }
        return "";
    }

    public String getMusicPic() {
        if (goodData){
            JsonObject musicData = new JsonParser().parse(json).getAsJsonObject().get("data").getAsJsonObject();
            return musicData.get("musicPic").getAsString();
        }
        return "";
    }

    public String getLrc() {
        return "";
    }
}
