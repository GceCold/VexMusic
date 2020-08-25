package ltd.icecold.vexmusic.events;

import com.google.gson.Gson;
import ltd.icecold.vexmusic.config.WorldBgm;
import ltd.icecold.vexmusic.utils.PluginMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayerListener implements Listener {
    @EventHandler
    public void playerChangeWorld (PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if ((Boolean) WorldBgm.get("world.enable")){
            String worldName = player.getLocation().getWorld().getName();
            boolean overlay = (Boolean) WorldBgm.get("world.overlay");
            List<String> worldList = WorldBgm.getWorldList();
            for (String world:worldList){
                if (world.equals(worldName)){
                    ConfigurationSection worldSection = WorldBgm.yamlConfiguration.getConfigurationSection("world." + world);
                    String music = worldSection.getString("music");
                    boolean loop = worldSection.getBoolean("loop");
                    Map<String,Object> json = new HashMap<>();
                    Map<String,Object> data = new HashMap<>();
                    json.put("type","bgm");
                    String[] split = music.split("]");
                    data.put("type",split[0].replace("[",""));
                    data.put("msuic",split[1]);
                    data.put("loop",loop);
                    json.put("message",data);
                    PluginMessage.send(player,new Gson().toJson(json));
                }
            }
        }
    }

}
