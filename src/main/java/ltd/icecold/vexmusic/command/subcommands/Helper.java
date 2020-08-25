package ltd.icecold.vexmusic.command.subcommands;

import ltd.icecold.vexmusic.command.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ice_cold
 * @description
 * @date Create in 2:01 2020/8/3
 */
public class Helper extends BaseCommand {

    public Helper() {
        super("help");
    }

    @Override
    public void onCommand(CommandSender sender, String command, ArrayList<String> args) {

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Object subCmd, ArrayList<String> args) {
        return null;
    }


}
