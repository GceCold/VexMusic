package ltd.icecold.vexmusic.utils;

import ltd.icecold.vexmusic.VexMusic;
import org.bukkit.Bukkit;

/**
 * @author ice_cold
 * @date 2019/7/19 11:42
 */
public class VaultHandle {
    public static boolean delMoney(String name, int money)
    {
        if (!VexMusic.getEconomy().hasAccount(Bukkit.getPlayer(name))) {
            return false;
        }
        if (VexMusic.getEconomy().has(Bukkit.getPlayer(name), money)) {
            VexMusic.getEconomy().withdrawPlayer(Bukkit.getPlayer(name),money);
        }
        return true;
    }
    public static boolean hasMoney(String name, int money)
    {
        if ((name == null) || (name.length() <= 0)) {
            return false;
        }
        if (money <= 0.0D) {
            return true;
        }
        return VexMusic.getEconomy().has(Bukkit.getPlayer(name), money);
    }
    public static Integer getMoney(String name)
    {
        return (int) VexMusic.getEconomy().getBalance(Bukkit.getPlayer(name));
    }
}
