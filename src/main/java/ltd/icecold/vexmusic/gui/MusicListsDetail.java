package ltd.icecold.vexmusic.gui;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexScrollingList;
import lk.vexview.gui.components.VexText;
import ltd.icecold.vexmusic.VexMusic;
import ltd.icecold.vexmusic.command.subcommands.Play;
import ltd.icecold.vexmusic.config.Login;
import ltd.icecold.vexmusic.config.gui.ListDetail;
import ltd.icecold.vexmusic.gui.bean.ListDetailResultBean;
import ltd.icecold.vexmusic.interfaceservice.MessageService;
import ltd.icecold.vexmusic.utils.PluginMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: gdenga
 * @date: 2019/8/21 10:26
 * @content:
 */
public class MusicListsDetail {
    public static void openGui(Player player, String type, Long id, String name) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    VexGui vexgui = new VexGui(
                            ListDetail.yamlConfiguration.getString("GUI.gui"),
                            ListDetail.yamlConfiguration.getInt("GUI.x"),
                            ListDetail.yamlConfiguration.getInt("GUI.y"),
                            ListDetail.yamlConfiguration.getInt("GUI.width"),
                            ListDetail.yamlConfiguration.getInt("GUI.high"),
                            ListDetail.yamlConfiguration.getInt("GUI.xshow"),
                            ListDetail.yamlConfiguration.getInt("GUI.yshow")
                    );
                    VexText title = new VexText(
                            ListDetail.yamlConfiguration.getInt("Text.title.x"),
                            ListDetail.yamlConfiguration.getInt("Text.title.y"),
                            ListDetail.yamlConfiguration.getStringList("Text.title.message")
                    );
                    if (ListDetail.yamlConfiguration.getBoolean("Text.title.enable")) {
                        vexgui.addComponent(title);
                    }
                    List<String> listName = new ArrayList<>();
                    listName.add(name);
                    VexText musicListName = new VexText(
                            ListDetail.yamlConfiguration.getInt("Text.musicListName.x"),
                            ListDetail.yamlConfiguration.getInt("Text.musicListName.y"),
                            listName
                    );

                    if (ListDetail.yamlConfiguration.getBoolean("Text.title.enable")) {
                        vexgui.addComponent(title);
                    }
                    if (ListDetail.yamlConfiguration.getBoolean("Text.musicListName.enable")) {
                        vexgui.addComponent(musicListName);
                    }

                    MessageService messageService = (MessageService) VexMusic.nettyClient.getBean(VexMusic.getInstance().getClass().getClassLoader(),MessageService.class,"#VEXMUSIC_ICECOLD#MUSIC_LIST_MUSIC_LIST_DETAIL");
                    Map<String,String> message = new HashMap<>();
                    message.put("userName",Login.getUsername());
                    message.put("useCode",VexMusic.getUseCode());
                    message.put("id",String.valueOf(id));
                    String result = messageService.message(new Gson().toJson(message));

                    if (result.isEmpty() || "error".equals(result)) {
                        player.sendMessage("§7[VexMusic] >§c服务器内部发生错误！错误代码:§l§eERROR005");
                        player.closeInventory();
                        cancel();
                        return;
                    }

                    if (!new JsonParser().parse(result).getAsJsonObject().has("data") || !new JsonParser().parse(result).getAsJsonObject().has("code")) {
                        player.sendMessage("§7[VexMusic] >§c服务器内部发生错误！错误代码:§l§eERROR006");
                        player.closeInventory();
                        cancel();
                        return;
                    }

                    ListDetailResultBean listDetailResultBean = new Gson().fromJson(result, ListDetailResultBean.class);
                    List<ListDetailResultBean.DataBean> dataBeanList = listDetailResultBean.getData();
                    VexScrollingList scrollingList = new VexScrollingList(
                            ListDetail.yamlConfiguration.getInt("ScrollingList.musicList.x"),
                            ListDetail.yamlConfiguration.getInt("ScrollingList.musicList.y"),
                            ListDetail.yamlConfiguration.getInt("ScrollingList.musicList.width"),
                            ListDetail.yamlConfiguration.getInt("ScrollingList.musicList.high"),
                            dataBeanList.size() * (ListDetail.yamlConfiguration.getInt("Button.music.high") + 2)
                    );

                    for (int i = 0; i < dataBeanList.size(); i++) {
                        VexButton music = new VexButton(
                                6238 + i,
                                dataBeanList.get(i).getMusicName(),
                                ListDetail.yamlConfiguration.getString("Button.music.url1"),
                                ListDetail.yamlConfiguration.getString("Button.music.url2"),
                                0,
                                i * (ListDetail.yamlConfiguration.getInt("Button.music.high") + 2),
                                ListDetail.yamlConfiguration.getInt("Button.music.width"),
                                ListDetail.yamlConfiguration.getInt("Button.music.high")
                        );
                        music.setFunction(player1 -> {
                            String musicName = dataBeanList.get(Integer.parseInt(music.getId().toString()) - 6238).getMusicName();
                            Integer musicId = dataBeanList.get(Integer.parseInt(music.getId().toString()) - 6238).getMusicID();
                            Play.playMusic(player1,String.valueOf(musicId));

                        });
                        scrollingList.addComponent(music);
                    }
                    List<String> musicNameList = new ArrayList<>();
                    for (int i = 0;i < dataBeanList.size();i++){
                        musicNameList.add(dataBeanList.get(i).getMusicName());
                    }
                    VexButton addToPlayList = new VexButton(
                            "addToPlayerList",
                            ListDetail.yamlConfiguration.getString("Button.addToPlayList.name"),
                            ListDetail.yamlConfiguration.getString("Button.addToPlayList.url1"),
                            ListDetail.yamlConfiguration.getString("Button.addToPlayList.url2"),
                            ListDetail.yamlConfiguration.getInt("Button.addToPlayList.x"),
                            ListDetail.yamlConfiguration.getInt("Button.addToPlayList.y"),
                            ListDetail.yamlConfiguration.getInt("Button.addToPlayList.width"),
                            ListDetail.yamlConfiguration.getInt("Button.addToPlayList.high"),
                            player1 -> {
                                PlayerList.addMusicListToPlayerList(player1,musicNameList);
                                player1.sendMessage(Language.getLang18().replace("${MUSIC_LIST_NAME}",name));
                            }
                    );
                    vexgui.addComponent(addToPlayList);
                    vexgui.addComponent(scrollingList);
                    VexViewAPI.openGui(player,vexgui);
                }
            }.runTaskAsynchronously(VexMusic.getInstance());
    }
}
