package ltd.icecold.vexmusic.gui;

import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexText;
import lk.vexview.gui.components.VexTextField;
import ltd.icecold.vexmusic.config.gui.SelectPlayer;
import ltd.icecold.vexmusic.music.MusicHandle;
import ltd.icecold.vexmusic.music.MusicMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class SelectPlayerGui {
    public static void openGui(Player player, MusicMessage music){
        VexGui vexgui = new VexGui(
                SelectPlayer.yamlConfiguration.getString("gui.gui"),
                SelectPlayer.yamlConfiguration.getInt("gui.x"),
                SelectPlayer.yamlConfiguration.getInt("gui.y"),
                SelectPlayer.yamlConfiguration.getInt("gui.width"),
                SelectPlayer.yamlConfiguration.getInt("gui.high")
        );

        VexText title = new VexText(
                SelectPlayer.yamlConfiguration.getInt("text.title.x"),
                SelectPlayer.yamlConfiguration.getInt("text.title.y"),
                SelectPlayer.yamlConfiguration.getStringList("text.title.message")
        );

        VexText titlePlayer = new VexText(
                SelectPlayer.yamlConfiguration.getInt("text.title2.x"),
                SelectPlayer.yamlConfiguration.getInt("text.title2.y"),
                SelectPlayer.yamlConfiguration.getStringList("text.title2.message")
        );

        VexTextField vextextfield1 = new VexTextField(30,17,75,12,20,10001);
        VexButton vexButton = new VexButton(10000,"下一步", SelectPlayer.yamlConfiguration.getString("Button.url"),SelectPlayer.yamlConfiguration.getString("Button.url2"),120,15,40,15, (ButtonFunction) player1 -> {
            String playerName = vextextfield1.getTypedText();
            if ("".equalsIgnoreCase(playerName)){
                player1.sendMessage("§7[VexMusic§7] §e> 请输入目标玩家名");
                return;
            }
            Player target = Bukkit.getPlayer(playerName);
            if (target == null){
                player1.closeInventory();
                player1.sendMessage("§7[VexMusic§7] §e> 目标玩家不在线");
                return;
            }
            if (player1.getName().equalsIgnoreCase(target.getName())){
                player1.closeInventory();
                player1.sendMessage("§7[VexMusic§7] §e> 本指令不能对自己使用！");
            }
            //MusicJson.Main(player,TextFieldText.gettext(player));
            MusicHandle.sendRequest(player1,target,music);
        });
        if (SelectPlayer.yamlConfiguration.getBoolean("text.title.enable")){
            vexgui.addComponent(title);
        }
        if (SelectPlayer.yamlConfiguration.getBoolean("text.title2.enable")){
            vexgui.addComponent(titlePlayer);
        }

        vexgui.addComponent(vexButton);
        vexgui.addComponent(vextextfield1);
        VexViewAPI.openGui(player,vexgui);
    }
}
