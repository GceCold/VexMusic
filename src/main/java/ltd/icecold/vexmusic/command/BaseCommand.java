package ltd.icecold.vexmusic.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseCommand {
    private String cmd;

    /**
     *
     * @param sender 指令发送者
     * @param command 指令
     * @param args 子指令的参数
     */
    public abstract void onCommand(CommandSender sender, String command, ArrayList<String> args);

    /**
     *  @param sender 指令发送者
     * @param subCmd 指令
     * @param args 子指令的参数
     * @return
     */
    public abstract List<String> onTabComplete(CommandSender sender, Object subCmd, ArrayList<String> args);

    /**
     *
     * @param subCmd 子指令
     */
    public BaseCommand(String subCmd) {
        this.cmd = subCmd;
        CommandHandler.instance.registerSubCommand(this);
    }

    /**
     *
     * @return 返回子指令
     */
    public final String getSubCmd() {
        return cmd;
    }

}
