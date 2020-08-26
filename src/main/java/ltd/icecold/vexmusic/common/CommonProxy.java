package ltd.icecold.vexmusic.common;

import ltd.icecold.vexmusic.server.ClientManager;
import ltd.icecold.vexmusic.server.ServerMain;
import ltd.icecold.vexmusic.server.SimpleChatService;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * @author ice_cold
 * @date Create in 7:55 2020/8/4
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        new ConfigLoader(event);
    }

    public void init(FMLInitializationEvent event)
    {
        File vexmusic = new File("vexmusic");
        vexmusic.mkdirs();
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
