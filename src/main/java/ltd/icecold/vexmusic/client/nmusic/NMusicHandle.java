package ltd.icecold.vexmusic.client.nmusic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ltd.icecold.vexmusic.client.ClientProxy;
import ltd.icecold.vexmusic.common.CommonProxy;
import ltd.icecold.vexmusic.server.ClientManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ice_cold
 * @date Create in 8:46 2020/8/4
 */
public class NMusicHandle {
    public static List<NMusicPlayer> musicList = new ArrayList<>();

    public static void PluginMessageHandle(String message) throws Exception {
        String type = new JsonParser().parse(message).getAsJsonObject().get("type").getAsString();
        if ("play_music_now".equals(type)){
            JsonObject jsonMessage = new JsonParser().parse(message).getAsJsonObject().get("message").getAsJsonObject();
            NMusicPlayer nMusicPlayer = new NMusicPlayer(
                    jsonMessage.get("musicCover").getAsString(),
                    jsonMessage.get("musicName").getAsString(),
                    jsonMessage.get("musicArtist").getAsString(),
                    jsonMessage.get("musicUrl").getAsString()
            );
            if (ClientProxy.server != null){
                ClientProxy.server.getService().getClientManager().sendAll("Player#PlayNow_"+new Gson().toJson(nMusicPlayer));
            }
        }
        if (type.startsWith("stop")){
            ClientProxy.server.getService().getClientManager().sendAll("Player#StopPlay");
        }
    }
    public static void PlayerMessageHandle(String message){
        if (message.startsWith("MusicEnded_")){
            String musicUrl = message.replace("MusicEnded_","");
            for (NMusicPlayer music:musicList){
                if (music.getMusicUrl().equals(musicUrl)){
                    Map<String,String> msg = new HashMap<>();
                    msg.put("type","music_end");
                    //msg.put("message" )
                }
            }
        }else if (message.startsWith("MusicInfo_")){

        }
    }
}
