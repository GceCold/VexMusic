package ltd.icecold.vexmusic.command.subcommands;

import com.google.gson.Gson;
import ltd.icecold.vexmusic.command.BaseCommand;
import ltd.icecold.vexmusic.config.Language;
import ltd.icecold.vexmusic.music.MusicHandle;
import ltd.icecold.vexmusic.music.MusicMessage;
import ltd.icecold.vexmusic.utils.PluginMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Accept extends BaseCommand {

    public static Map<String, MusicMessage> playerResponse = new HashMap<>();

    public Accept() {
        super("accept");
    }

    @Override
    public void onCommand(CommandSender sender, String command, ArrayList<String> args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("[VexMusic] > "+ChatColor.DARK_RED+"本指令请在游戏中使用");
            return;
        }
        Player player = (Player)sender;
        if (!playerResponse.containsKey(player.getName())){
            player.sendMessage(Language.getLang("language.lang24"));
            return;
        }
        MusicMessage musicMessage = playerResponse.get(player.getName());
        PluginMessage.send((Player)sender,new Gson().toJson(MusicHandle.musicInfo2Bean(musicMessage)));
        player.sendMessage(Language.getLang("language.lang25").replace("${MUSIC_NAME}",musicMessage.getMusicName()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Object subCmd, ArrayList<String> args) {
        return null;
    }
}
