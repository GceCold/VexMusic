package ltd.icecold.vexmusic.command.subcommands;

import com.google.gson.Gson;
import ltd.icecold.vexmusic.bean.PlayMusicMessageBean;
import ltd.icecold.vexmusic.command.BaseCommand;
import ltd.icecold.vexmusic.music.MusicHandle;
import ltd.icecold.vexmusic.music.MusicMessage;
import ltd.icecold.vexmusic.utils.PluginMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ice_cold
 */
public class Test extends BaseCommand {

    public Test() {
        super("test");
    }

    @Override
    public void onCommand(CommandSender sender, String command, ArrayList<String> args) {
        MusicMessage musicInfo = MusicHandle.getMusicInfo(args.get(0), (Player) sender);
        PlayMusicMessageBean playMusicMessageBean = new PlayMusicMessageBean();
        PlayMusicMessageBean.MessageBean messageBean = new PlayMusicMessageBean.MessageBean();
        messageBean.setMusicName(musicInfo.getMusicName());
        messageBean.setMusicArtist("测试");
        messageBean.setMusicCover("");
        messageBean.setMusicUrl(musicInfo.getMusicUrl());
        playMusicMessageBean.setMessage(messageBean);
        playMusicMessageBean.setType("play_music_now");
        PluginMessage.send((Player)sender,new Gson().toJson(playMusicMessageBean));
        System.out.println("test");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Object subCmd, ArrayList<String> args) {
        return null;
    }

}
