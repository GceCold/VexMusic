package ltd.icecold.vexmusic.command;

import ltd.icecold.vexmusic.command.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;


public class CommandHandler implements CommandExecutor {
    public HashMap<String, BaseCommand> subCommands = new HashMap<>();
    public static CommandHandler instance;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if(command.getName().contentEquals("music")) {
            if(args.length != 0) {
                if("help".equalsIgnoreCase(args[0])) {
                    Helper.sendHelp(sender);
                    return true;
                }
                BaseCommand base = subCommands.get(args[0]);
                if(base == null) {
                    Helper.sendHelp(sender);
                    return true;
                }
                base.onCommand(sender, args[0], getSubArgs(args));
            } else {
                Helper.sendHelp(sender);
            }
        }
        return true;
    }

    /**
     *
     * @param args 指令参数
     * @return 返回清除null后的子指令的参数
     */
    public ArrayList<String> getSubArgs(final String[] args) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(args));
        list.remove(0);
        list.removeAll(Collections.singleton(null));
        return list;
    }

    /**
     *
     * @param cmd 注册子指令
     */
    public void registerSubCommand(BaseCommand cmd) {
        subCommands.put(cmd.getSubCmd(), cmd);
    }

    public List<String> onComplete(CommandSender sender, Object subCmd, ArrayList<String> parms) {
        BaseCommand base = subCommands.get(subCmd);
        if(base == null) {
            return new ArrayList<>();
        }
        return base.onTabComplete(sender, subCmd, parms);
    }

    /*
      注册command
     */
    static {
        instance = new CommandHandler();
        new Test();
        new Play();
        new Accept();
        new Helper();
        new MusicList();
        new Refuse();
        new Server();
        new Stop();
    }
}
