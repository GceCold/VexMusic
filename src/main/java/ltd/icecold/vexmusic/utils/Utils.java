package ltd.icecold.vexmusic.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class Utils {
    public static List<Player> getOnlinePlayers() {
        List<Player> players = Lists.newArrayList();
        List<World> worlds = Lists.newArrayList();
        worlds.addAll(Bukkit.getWorlds());
        for (int i = 0; i < worlds.size(); i++) {
            if (!worlds.get(i).getPlayers().isEmpty()) {
                players.addAll(worlds.get(i).getPlayers());
            }
        }
        return players;
    }
    public static Boolean isSpigot() {
        try {
            Class.forName("org.bukkit.entity.Player$Spigot");
        }catch (ClassNotFoundException e){
            return false;
        }
        return true;
    }
}
