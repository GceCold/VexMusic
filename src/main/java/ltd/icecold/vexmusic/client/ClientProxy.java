package ltd.icecold.vexmusic.client;

import ltd.icecold.vexmusic.classloader.LoadMusicPlayer;
import ltd.icecold.vexmusic.common.CommonProxy;
import ltd.icecold.vexmusic.server.ClientManager;
import ltd.icecold.vexmusic.server.ServerMain;
import ltd.icecold.vexmusic.server.SimpleChatService;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/**
 * @author ice_cold
 * @date Create in 7:56 2020/8/4
 */
public class ClientProxy extends CommonProxy {
    public static ServerMain server ;

    @Override
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        super.preInit(event);
        new NetworkHandle();
        new Thread(() -> {
            ClientManager clientManager = new ClientManager();
            SimpleChatService service = new SimpleChatService(clientManager);
            server = new ServerMain(service);
            try {
                LoadMusicPlayer.start(String.valueOf(server.getService().getServerSocket().getLocalPort()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }


}
