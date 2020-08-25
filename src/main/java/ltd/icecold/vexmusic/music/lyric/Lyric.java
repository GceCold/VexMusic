package ltd.icecold.vexmusic.music.lyric;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import ltd.icecold.vexmusic.VexMusic;
import ltd.icecold.vexmusic.config.Login;
import ltd.icecold.vexmusic.interfaceservice.MessageService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Lyric {
    public static void showLyc(Player player,String musicId){
        stopLyc(player);
        if ("".equals(VexMusic.getUseCode())) {
            player.sendMessage("§7[VexMusic] >§c服务器内部发生错误！");
            return;
        }
        MessageService messageService = (MessageService) VexMusic.nettyClient.getBean(VexMusic.getInstance().getClass().getClassLoader(),MessageService.class,"#VEXMUSIC_ICECOLD#LYRIC");
        Map<String,String> message = new HashMap<>();
        message.put("userName", Login.getUsername());
        message.put("musicId",musicId);
        message.put("useCode",VexMusic.getUseCode());
        String result = messageService.message(new Gson().toJson(message));
        int code = new JsonParser().parse(result).getAsJsonObject().get("code").getAsInt();
        if (code == 200){
            String lyric = new JsonParser().parse(result).getAsJsonObject().get("lyric").getAsString();
            LineAsyncRunnable runnable = new LineAsyncRunnable(player, lyric);
            LineAsyncRunnable.runnableList.put(player.getName(), runnable);
            runnable.start();
        }
    }
    public static void stopLyc(Player player){
        if (LineAsyncRunnable.runnableList.containsKey(player.getName())){
            LineAsyncRunnable lineAsyncRunnable = LineAsyncRunnable.runnableList.get(player.getName());
            lineAsyncRunnable.stop();
            lineAsyncRunnable.interrupt();
        }
    }
}
