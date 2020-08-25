package ltd.icecold.vexmusic.gui;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexCheckBox;
import lk.vexview.gui.components.VexScrollingList;
import lk.vexview.gui.components.VexText;
import ltd.icecold.vexmusic.VexMusic;
import ltd.icecold.vexmusic.command.subcommands.Play;
import ltd.icecold.vexmusic.config.Login;
import ltd.icecold.vexmusic.config.gui.MusicResult;
import ltd.icecold.vexmusic.config.gui.Search;
import ltd.icecold.vexmusic.gui.bean.SearchMusicResultBean;
import ltd.icecold.vexmusic.music.MusicHandle;
import ltd.icecold.vexmusic.music.MusicMessage;
import ltd.icecold.vexmusic.utils.PluginMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultMusicGui {
    public static void openSearchMusicGui(Player player, String musicName) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Map<String, Integer> musicList = getMusicInfo(player, musicName);
                if (musicList == null) {
                    player.sendMessage("§7[VexMusic] >§c未搜索到歌曲：" + musicName);
                    cancel();
                    return;
                }
                VexGui vexgui = new VexGui(
                        MusicResult.yamlConfiguration.getString("gui.gui"),
                        MusicResult.yamlConfiguration.getInt("gui.x"),
                        MusicResult.yamlConfiguration.getInt("gui.y"),
                        MusicResult.yamlConfiguration.getInt("gui.width"),
                        MusicResult.yamlConfiguration.getInt("gui.high")
                );
                VexScrollingList vexScrollingList = new VexScrollingList(
                        MusicResult.yamlConfiguration.getInt("scrollingList.list.x"),
                        MusicResult.yamlConfiguration.getInt("scrollingList.list.y"),
                        MusicResult.yamlConfiguration.getInt("scrollingList.list.width"),
                        MusicResult.yamlConfiguration.getInt("scrollingList.list.high"),
                        MusicResult.yamlConfiguration.getInt("button.list.high") * (musicList.size() + 3)
                );

                VexCheckBox checkBoxServer = new VexCheckBox(
                        74541,
                        MusicResult.yamlConfiguration.getString("checkBox.server.url1"),
                        MusicResult.yamlConfiguration.getString("checkBox.server.url2"),
                        MusicResult.yamlConfiguration.getInt("checkBox.server.x"),
                        MusicResult.yamlConfiguration.getInt("checkBox.server.y"),
                        MusicResult.yamlConfiguration.getInt("checkBox.server.width"),
                        MusicResult.yamlConfiguration.getInt("checkBox.server.high"),
                        true
                );
                VexCheckBox checkBoxSend = new VexCheckBox(
                        74581,
                        MusicResult.yamlConfiguration.getString("checkBox.send.url1"),
                        MusicResult.yamlConfiguration.getString("checkBox.send.url2"),
                        MusicResult.yamlConfiguration.getInt("checkBox.send.x"),
                        MusicResult.yamlConfiguration.getInt("checkBox.send.y"),
                        MusicResult.yamlConfiguration.getInt("checkBox.send.width"),
                        MusicResult.yamlConfiguration.getInt("checkBox.send.high"),
                        false
                );

                int i = 0;
                for (Map.Entry<String, Integer> entry : musicList.entrySet()) {
                    String msuicName = entry.getKey();
                    Integer musicId = entry.getValue();
                    VexButton vexButton = new VexButton(
                            520 + musicId,
                            msuicName,
                            MusicResult.yamlConfiguration.getString("button.list.url1"),
                            MusicResult.yamlConfiguration.getString("button.list.url2"),
                            0,
                            i * (MusicResult.yamlConfiguration.getInt("button.list.high") + 2),
                            MusicResult.yamlConfiguration.getInt("button.list.width"),
                            MusicResult.yamlConfiguration.getInt("button.list.high")
                    );
                    vexButton.setFunction(player1 -> {
                        int musicId1 = Integer.parseInt(vexButton.getId().toString()) - 520;
                        MusicMessage musicInfo = MusicHandle.getMusicInfo(String.valueOf(musicId1), player1);
                        if (checkBoxServer.isChecked()){
                            MusicHandle.serverMusic(player1, musicInfo);
                        }else if (checkBoxSend.isChecked()){
                            SelectPlayerGui.openGui(player1, musicInfo);
                        }else {
                            Play.playMusic(player1, String.valueOf(musicId1));
                        }
                    });


                    vexScrollingList.addComponent(vexButton);
                    i++;
                }
                List<String> text = MusicResult.yamlConfiguration.getStringList("text.title.message");
                List<String> textNew = new ArrayList<>();
                for (String n1 : text) {
                    textNew.add(n1.replace("%name%", musicName).replace("%amount%", String.valueOf(musicList.size())));
                }
                if (MusicResult.yamlConfiguration.getBoolean("text.title.enable")) {
                    VexText vexText = new VexText(
                            MusicResult.yamlConfiguration.getInt("text.title.x"),
                            MusicResult.yamlConfiguration.getInt("text.title.y"),
                            textNew
                    );
                    vexgui.addComponent(vexText);
                }
                vexgui.addComponent(vexScrollingList);
                vexgui.addComponent(checkBoxSend);
                vexgui.addComponent(checkBoxServer);
                VexViewAPI.openGui(player, vexgui);
            }
        }.runTaskAsynchronously(VexMusic.getInstance());
    }

    public static Map<String, Integer> getMusicInfo(Player player, String musicName) {
        Map<String, Integer> msuicInfo = new HashMap<>();
        if ("".equals(VexMusic.getUseCode())) {
            player.sendMessage("§7[VexMusic] >§c服务器内部发生错误！错误代码:§l§eERROR001");
            return null;
        }

        Map<String, String> message = new HashMap<>();
        message.put("userName", Login.getUsername());
        message.put("name", musicName);
        message.put("useCode", VexMusic.getUseCode());
        String result = PluginMessage.sendMsgToServer("MUSIC_SEARCH", message);

        if (result == null) {
            player.sendMessage("§7[VexMusic] >§c歌曲信息查询失败");
            return null;
        }
        String code = new JsonParser().parse(result).getAsJsonObject().get("code").getAsString();
        if (!"200".equals(code)) {
            player.sendMessage("§7[VexMusic] >§c歌曲信息查询失败，未找到歌曲");
            return null;
        }
        SearchMusicResultBean searchMusicResultBean = new Gson().fromJson(result, SearchMusicResultBean.class);
        List<SearchMusicResultBean.DataBean> dataBeanList = searchMusicResultBean.getData();
        for (SearchMusicResultBean.DataBean dataBean : dataBeanList) {
            msuicInfo.put(dataBean.getName() + " - " + dataBean.getArtists(), dataBean.getId());
        }
        return msuicInfo;
    }
}
