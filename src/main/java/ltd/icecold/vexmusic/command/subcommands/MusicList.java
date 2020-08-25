package ltd.icecold.vexmusic.command.subcommands;

import ltd.icecold.vexmusic.command.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MusicList extends BaseCommand {

    public MusicList() {
        super("list");
    }

    @Override
    public void onCommand(CommandSender sender, String command, ArrayList<String> args) {

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Object subCmd, ArrayList<String> args) {
        return null;
    }
}
