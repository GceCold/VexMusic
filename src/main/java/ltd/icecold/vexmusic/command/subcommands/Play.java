package ltd.icecold.vexmusic.command.subcommands;

import com.google.gson.Gson;
import ltd.icecold.vexmusic.bean.PlayMusicMessageBean;
import ltd.icecold.vexmusic.command.BaseCommand;
import ltd.icecold.vexmusic.config.Config;
import ltd.icecold.vexmusic.config.Language;
import ltd.icecold.vexmusic.music.MusicHandle;
import ltd.icecold.vexmusic.music.MusicMessage;
import ltd.icecold.vexmusic.music.lyric.Lyric;
import ltd.icecold.vexmusic.utils.PluginMessage;
import ltd.icecold.vexmusic.utils.VaultHandle;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ice_cold
 */
public class Play extends BaseCommand {

    public Play() {
        super("play");
    }

    @Override
    public void onCommand(CommandSender sender, String command, ArrayList<String> args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("[VexMusic] > "+ ChatColor.DARK_RED +"请在游戏内使用本指令");
            return;
        }
        if (args.size() == 0){
            sender.sendMessage("[VexMusic] > "+ ChatColor.DARK_RED +"请输入歌曲名或ID");
            return;
        }
        Player player = (Player)sender;
        StringBuilder stringBuilder = new StringBuilder();
        for (String name:args){
            stringBuilder.append(name).append(" ");
        }
        playMusic(player,stringBuilder.toString());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Object subCmd, ArrayList<String> args) {
        return null;
    }

    public static void playMusic(Player player,String music){
        MusicMessage musicInfo = MusicHandle.getMusicInfo(music, player);

        if (musicInfo.getMusicName() == null || musicInfo.getMusicName().isEmpty()){
            player.sendMessage(Language.getLang("language.lang12").replace("${MUSIC_NAME}",music));
            return;
        }
        if ((musicInfo.getMusicName() != null || !musicInfo.getMusicName().isEmpty()) && (musicInfo.getMusicUrl().isEmpty() || musicInfo.getMusicUrl() == null)){
            player.sendMessage(Language.getLang("language.lang2").replace("${MUSIC_NAME}",musicInfo.getMusicName()));
            return;
        }
        PluginMessage.send(player,new Gson().toJson(MusicHandle.musicInfo2Bean(musicInfo)));
        if ((Boolean) Config.get("play.paid.enable")) {
            Integer cost = (Integer) Config.get("play.paid.cost");
            if (!VaultHandle.hasMoney(player.getName(), cost)) {
                player.sendMessage(Language.getLang("language.lang12").replace("${MUSIC_NAME}", musicInfo.getMusicName()).replace("${MONEY}", String.valueOf(cost)).replace("${NOW}", VaultHandle.getMoney(player.getName()).toString()));
                return;
            }
            VaultHandle.delMoney(player.getName(), cost);
        }
        player.sendMessage(Language.getLang("language.lang1").replace("${MUSIC_NAME}",musicInfo.getMusicName()+" - "+musicInfo.getMusicArtists()));
        if (player.hasPermission("music.lrc")){
            Lyric.showLyc(player,musicInfo.getMusicId());
        }
    }

}
