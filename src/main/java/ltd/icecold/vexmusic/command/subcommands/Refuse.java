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
import java.util.List;

public class Refuse extends BaseCommand {

    public Refuse() {
        super("refuse");
    }

    @Override
    public void onCommand(CommandSender sender, String command, ArrayList<String> args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("[VexMusic] > "+ ChatColor.DARK_RED+"本指令请在游戏中使用");
            return;
        }
        Player player = (Player)sender;
        if (!Accept.playerResponse.containsKey(player.getName())){
            player.sendMessage(Language.getLang("language.lang24"));
        }else {
            Accept.playerResponse.remove(player.getName());
            player.sendMessage(Language.getLang("language.lang26"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Object subCmd, ArrayList<String> args) {
        return null;
    }
}
