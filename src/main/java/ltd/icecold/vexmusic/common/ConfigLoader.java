package ltd.icecold.vexmusic.common;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

/**
 * @author ice_cold
 * @date Create in 8:49 2020/8/4
 */
public class ConfigLoader {

    private static Configuration config;
    private static Logger logger;
    public static int playerMode;

    public ConfigLoader(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        load();
    }

    public static void load()
    {
        logger.info("[VexMusic]Started loading config. ");
        String comment = "Choose player mode. 0 Old Player   1 New Player";
        playerMode = config.get(Configuration.CATEGORY_GENERAL, "player", 1, comment).getInt();

        config.save();
        logger.info("Finished loading config. ");
    }

    public static Logger logger()
    {
        return logger;
    }
}
