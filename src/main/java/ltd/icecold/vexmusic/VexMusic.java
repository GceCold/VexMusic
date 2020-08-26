package ltd.icecold.vexmusic;

import ltd.icecold.vexmusic.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author ice_cold
 * @date Create in 7:52 2020/8/4
 */
@Mod(modid = VexMusic.MODID, name = VexMusic.NAME, version = VexMusic.VERSION, acceptedMinecraftVersions = "1.12.2")
public class VexMusic {
    public static final String MODID = "vexmusic";
    public static final String NAME = "VexMusic";
    public static final String VERSION = "2.0.0";

    @SidedProxy(clientSide = "ltd.icecold.vexmusic.client.ClientProxy",
            serverSide = "ltd.icecold.vexmusic.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(VexMusic.MODID)
    public static VexMusic instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws Exception {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
