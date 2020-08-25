package ltd.icecold.vexmusic.gui;

import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.*;

import ltd.icecold.vexmusic.config.gui.Search;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: gdenga
 * @date: 2019/8/17 21:49
 * @content:
 */
public class SearchMusicGui {
    public static void OpenPlayerGUI(Player player){
        List<VexComponents> guiCompontents = new ArrayList<>();
        VexGui vexgui = new VexGui(
                Search.yamlConfiguration.getString("gui.gui"),
                Search.yamlConfiguration.getInt("gui.x"),
                Search.yamlConfiguration.getInt("gui.y"),
                Search.yamlConfiguration.getInt("gui.width"),
                Search.yamlConfiguration.getInt("gui.high")
        );
        if (Search.yamlConfiguration.getBoolean("text.title.enable")) {
            List<String> titleText = Search.yamlConfiguration.getStringList("text.title.message");
            VexText vexTitle1 = new VexText(
                    Search.yamlConfiguration.getInt("text.title.x"),
                    Search.yamlConfiguration.getInt("text.title.y"),
                    titleText
            );
            guiCompontents.add(vexTitle1);
        }
        VexTextField vextextfield = new VexTextField(
                Search.yamlConfiguration.getInt("textField.x"),
                Search.yamlConfiguration.getInt("textField.y"),
                Search.yamlConfiguration.getInt("textField.width"),
                Search.yamlConfiguration.getInt("textField.high"),
                100,
                73613,
                Search.yamlConfiguration.getString("textField.text")
        );
        guiCompontents.add(vextextfield);

        VexCheckBox checkBoxMusic = new VexCheckBox(
                74550,
                Search.yamlConfiguration.getString("checkBox.music.url1"),
                Search.yamlConfiguration.getString("checkBox.music.url2"),
                Search.yamlConfiguration.getInt("checkBox.music.x"),
                Search.yamlConfiguration.getInt("checkBox.music.y"),
                Search.yamlConfiguration.getInt("checkBox.music.width"),
                Search.yamlConfiguration.getInt("checkBox.music.high"),
                true
        );
        VexCheckBox checkBoxPlayList = new VexCheckBox(
                74549,
                Search.yamlConfiguration.getString("checkBox.playlist.url1"),
                Search.yamlConfiguration.getString("checkBox.playlist.url2"),
                Search.yamlConfiguration.getInt("checkBox.playlist.x"),
                Search.yamlConfiguration.getInt("checkBox.playlist.y"),
                Search.yamlConfiguration.getInt("checkBox.playlist.width"),
                Search.yamlConfiguration.getInt("checkBox.playlist.high"),
                false
        );

        VexButton vexButton = new VexButton(
                10086,
                Search.yamlConfiguration.getString("button.search.message"),
                Search.yamlConfiguration.getString("button.search.url"),
                Search.yamlConfiguration.getString("button.search.url2"),
                Search.yamlConfiguration.getInt("button.search.x"),
                Search.yamlConfiguration.getInt("button.search.y"),
                Search.yamlConfiguration.getInt("button.search.width"),
                Search.yamlConfiguration.getInt("button.search.high"),
                player1 -> {
                    if (vextextfield.getTypedText().equals("")){
                        player1.sendMessage("§7[VexMusic§7] §e> 请输入查询名称");
                        return;
                    }
                    if (checkBoxMusic.isChecked() && checkBoxPlayList.isChecked()){
                        player1.sendMessage("§7[VexMusic§7] §e> 您只能选择一个");
                        return;
                    }else if (!checkBoxMusic.isChecked() && !checkBoxPlayList.isChecked()){
                        player1.sendMessage("§7[VexMusic§7] §e> 请选择一个查询类别");
                        return;
                    }

                    if (checkBoxMusic.isChecked()){
                        ResultMusicGui.openSearchMusicGui(player1,vextextfield.getTypedText());
                    }
                    if (checkBoxPlayList.isChecked()){
                        //MusicListSearch.openGui(player1,vextextfield.getTypedText());
                    }
                });


        guiCompontents.add(checkBoxPlayList);
        guiCompontents.add(checkBoxMusic);
        guiCompontents.add(vexButton);
        vexgui.addAllComponents(guiCompontents);
        VexViewAPI.openGui(player,vexgui);
    }
}
