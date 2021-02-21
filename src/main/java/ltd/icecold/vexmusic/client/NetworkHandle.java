package ltd.icecold.vexmusic.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import ltd.icecold.vexmusic.client.jmusic.JMusicHandle;
import ltd.icecold.vexmusic.client.nmusic.NMusicHandle;
import ltd.icecold.vexmusic.common.ConfigLoader;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author ice_cold
 * @date Create in 8:55 2020/8/4
 */
public class NetworkHandle {
    static FMLEventChannel channel;
    private static final int IDX = 6666;
    public NetworkHandle(){
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("vexmusic:message");
        channel.register(this);
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent evt) throws Exception {
        ByteBuf buf = Unpooled.wrappedBuffer(evt.getPacket().payload());
        String origin = buf.toString(StandardCharsets.UTF_8);
        if (origin.contains("{")){
            String json = origin.substring(origin.indexOf("{"));
            if (validate(json)){
                String type = new JsonParser().parse(json).getAsJsonObject().get("type").getAsString();
                if ("bgm".equals(type)){
                    JMusicHandle.PluginMessageHandle(json);
                }
                if ("play_music_now".equals(type)) {
                    NMusicHandle.PluginMessageHandle(json);
                }
            }
        }
    }

    public static void sendMsgToPlugin(String msg){
        byte[] array = msg.getBytes();
        ByteBuf buf = Unpooled.wrappedBuffer(array);
        FMLProxyPacket packet = new FMLProxyPacket(new PacketBuffer(buf), "vexmusic:message"); // 数据包
        channel.sendToServer(packet);
    }

    /**
     * 判断是否为json数据，防止异常
     * @param json json数据
     * @return 是否为json数据格式
     */
    public static boolean validate(String json) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(json);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        return jsonElement.isJsonObject();
    }
}
