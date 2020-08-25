package ltd.icecold.vexmusic.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import ltd.icecold.vexmusic.VexMusic;
import ltd.icecold.vexmusic.interfaceservice.MessageService;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ice_cold
 * @date 2019/8/7 17:00
 */
public class PluginMessage {
    private static final int IDX = 6666;

    /**
     * 兼容高版本forge发送信息
     * @param player 玩家
     * @param msg 信息
     */
    public static void send(Player player, String msg) {
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = Unpooled.buffer(bytes.length + 1);
        buf.writeByte(IDX);
        buf.writeBytes(bytes);
        player.sendPluginMessage(VexMusic.getInstance(), VexMusic.getChannel(), buf.array());
    }

    /**
     * 兼容高版本forge读取信息
     * @param array 数据
     */
    public static String read(byte[] array) {
        ByteBuf buf = Unpooled.wrappedBuffer(array);
        if (buf.readUnsignedByte() == IDX) {
            return buf.toString(StandardCharsets.UTF_8);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 向netty服务器发送数据
     * @param name 标识名
     * @param msg 数据
     * @return 服务器返回数据
     */
    public static String sendMsgToServer(String name,Map<?,?> msg){
        MessageService messageService = (MessageService) VexMusic.nettyClient.getBean(VexMusic.getInstance().getClass().getClassLoader(), MessageService.class, "#VEXMUSIC_ICECOLD#"+name);
        return messageService.message(new Gson().toJson(msg));
    }

}
