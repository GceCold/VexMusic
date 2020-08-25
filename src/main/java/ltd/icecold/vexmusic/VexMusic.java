package ltd.icecold.vexmusic;

import com.google.gson.JsonParser;
import lk.vexview.api.VexViewAPI;
import ltd.icecold.vexmusic.command.CommandHandler;
import ltd.icecold.vexmusic.config.*;
import ltd.icecold.vexmusic.events.MessageListener;
import ltd.icecold.vexmusic.events.PlayerListener;
import ltd.icecold.vexmusic.music.ExecutePlayMusicDay;
import ltd.icecold.vexmusic.netty.NettyClient;
import ltd.icecold.vexmusic.utils.PluginMessage;
import ltd.icecold.vexmusic.utils.RSACoder;
import ltd.icecold.vexmusic.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public final class VexMusic extends JavaPlugin {
    /**
     * 插件版本
     */
    private static final String version = "2.0.0";
    /**
     * 主类实例
     */
    private static VexMusic instance;
    /**
     * 插件通信名
     */
    private static final String CHANNEL = "vexmusic:message";
    /**
     * netty服务器地址
     */
    public static final String MUSIC_API_HOST_NAME = "127.0.0.1";
    /**
     * netty服务器端口
     */
    public static final Integer MUSIC_API_HOST_PORT = 5211;
    /**
     * netty客户端
     */
    public static NettyClient nettyClient;
    /**
     * 是否取消连接netty服务器
     */
    public static Boolean isCancelConnection = false;
    /**
     * 服务器动态验证标识码
     */
    private static String useCode;
    /**
     * Vault
     */
    private static Economy economy;
    /**
     * 是否使用BossBarApi
     */
    public static boolean useBarApi = false;
    /**
     * NMS包名版本
     */
    public static String NMSVersion;
    /**
     * ActionBarAPI
     */
    public static Boolean useOldMethods = false;


    @Override
    public void onEnable() {
        System.out.println(Utils.isSpigot());
        instance = this;
        initFile();
        //再不填用户名和使用码就可以去医院看看脑子了
        if (Login.getUsername() == null || Login.getCode() == null || Login.getUsername().isEmpty() || Login.getCode().isEmpty()) {
            getServer().getConsoleSender().sendMessage("§6[VexMusic] > §c请输入邮箱及使用码");
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) {
                File loginFile = new File(getDataFolder(), "login.yml");
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler " + loginFile.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "请输入邮箱及使用码\n请在填写完毕并保存后关闭本提示框", "VexMusic", JOptionPane.WARNING_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        getServer().getConsoleSender().sendMessage("§a =============§6[VexMusic]§a=============");
        getServer().getConsoleSender().sendMessage("§a =  §3服务器核心版本:§f" + Bukkit.getBukkitVersion());
        if (Login.getUsername().isEmpty() || Login.getCode().isEmpty()) {
            sendError("请检查§e邮箱§c和§e使用码§c的填写");
            return;
        }

        getServer().getConsoleSender().sendMessage("§a =  §6用户：§3§l" + Login.getUsername());
        getServer().getConsoleSender().sendMessage("§a =  插件版本：§f2.0.0");

        checkUser();
        checkConfigFile();
        checkDepend();
        setupEconomy();
        register();
        new ExecutePlayMusicDay();
        getServer().getConsoleSender().sendMessage("§a ==================================== ");

        loginNetease();

        NMSVersion = Bukkit.getServer().getClass().getPackage().getName();
        NMSVersion = NMSVersion.substring(NMSVersion.lastIndexOf(".") + 1);
        if ("v1_8_R1".equalsIgnoreCase(NMSVersion) || NMSVersion.startsWith("v1_7_")) {
            useOldMethods = true;
        }

    }

    @Override
    public void onDisable() {
        isCancelConnection = true;
        if (!"".equals(useCode) && nettyClient != null) {
            nettyClient.disconnectServer();
        }
    }

    public void loginNetease(){
        if(Login.getWyPassword() == null || Login.getWyPassword().isEmpty()){
           return;
        }
        Map<String, String> loginMessage = new HashMap<>();
        if (!Login.getWyPhone().isEmpty()){
            loginMessage.put("username", Login.getWyPhone());
            Bukkit.getServer().getConsoleSender().sendMessage("[VexMusic] > "+ ChatColor.AQUA +"已开启网易云账号登录，正在登录 账号："+ChatColor.GOLD+Login.getWyPhone());
        }else {
            loginMessage.put("username", Login.getWyUserName());
            Bukkit.getServer().getConsoleSender().sendMessage("[VexMusic] > "+ ChatColor.AQUA +"已开启网易云账号登录，正在登录 邮箱："+ChatColor.GOLD+Login.getWyUserName());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if(Login.getWyUserName().isEmpty() && Login.getWyPhone().isEmpty()){
                    cancel();
                    return;
                }
                loginMessage.put("password", Login.getWyPassword());
                String result = PluginMessage.sendMsgToServer("NETEASE_LOGIN",loginMessage);
                if("502".equals(result)){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY +"[VexMusic] > §e登录失败！网易云音乐账号或密码错误");
                    cancel();
                }
                if ("501".equals(result)){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY +"[VexMusic] > §e登录失败！您输入的网易云账号邮箱格式错误或服务器通讯错误");
                    cancel();
                }
                if ("200".equals(result)){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY +"[VexMusic] > "+ChatColor.GREEN+"网易云音乐登录成功");
                }
            }
        }.runTaskTimerAsynchronously(this,10,20*60*20);
    }


    /**
     * 插件注册
     */
    private void register(){
        //注册指令
        Bukkit.getPluginCommand("music").setExecutor(CommandHandler.instance);
        //注册通道
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, CHANNEL, new MessageListener());
        //监听器
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
    }

    /**
     * 初始化配置文件
     */
    private void initFile() {
        writeConfig("config.yml");
        writeConfig("login.yml");
        writeConfig("soundlist.yml");
        writeConfig("worldBGM.yml");
        writeConfig("regionBGM.yml");
        writeConfig("language.yml");
        writeConfig("timing.yml");
        //GUI配置文件
        writeConfig("gui/musiclist.yml");
        writeConfig("gui/search.yml");
        writeConfig("gui/main.yml");
        writeConfig("gui/toplist.yml");
        writeConfig("gui/listdetail.yml");
        writeConfig("gui/musicresult.yml");
        writeConfig("gui/playlistresult.yml");

        Config.init();
        Login.init();
        Language.init();
        WorldBgm.init();
        Timing.init();
    }

    /**
     * 写出配置文件
     * @param fileName 文件名
     */
    private void writeConfig(String fileName) {
        File file = new File(VexMusic.getInstance().getDataFolder(), fileName);
        if (!file.exists()) {
            this.saveResource(fileName, false);
        }
    }

    /**
     * 检查config.yml文件版本与是否已同意EULA协议
     */
    private void checkConfigFile(){
        String configVersion = Config.getVersion();
        if (!configVersion.equals(version)){
            File configFile = new File(this.getDataFolder(),"config.yml");
            File oldConfigFile = new File(this.getDataFolder(),"config - old - "+configVersion+".yml");
            configFile.renameTo(oldConfigFile);
            writeConfig("config.yml");
            Bukkit.getConsoleSender().sendMessage("§a = §c 检测到您的§e config.yml§c 的版本为§e "+configVersion+" §c本次更新更新了此文件，已将您原本的文件重命名为 §e"+"config - old - "+configVersion+".yml"+"§c 请自行修改配置");
        }
        if (!Config.getEula()){
            sendError("请同意位于§e config.yml§c 中的EULA协议");
        }
    }

    /**
     * 检测插件依赖
     */
    private void checkDepend() {
        if (Bukkit.getPluginManager().isPluginEnabled("VexView")) {
            getServer().getConsoleSender().sendMessage("§a =  §6已检测到VexView v" + VexViewAPI.getVexView().getVersion());
        } else {
            sendError("未检测到VexView，VexView为必须前置");
            return;
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            sendError("未检测到Vault，Vault为必须前置");
            return;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("BossBarAPI")) {
            useBarApi = true;
            getServer().getConsoleSender().sendMessage(" §a= §d 检测到BossBarAPI");
        }
    }

    /**
     * 输出错误
     * @param errorMsg 错误信息
     */
    private static void sendError(String errorMsg) {
        isCancelConnection = true;
        Bukkit.getServer().getConsoleSender().sendMessage("§a =  §c验证失败");
        Bukkit.getServer().getConsoleSender().sendMessage("§a =  §c错误信息:§l§e" + errorMsg);
        Bukkit.getServer().getConsoleSender().sendMessage("§a ==================================== ");
        Bukkit.getPluginManager().disablePlugin(VexMusic.getInstance());
    }

    /**
     * 插件验证
     */
    public static void checkUser() {
        String enPort = "";
        String enUsername = "";
        String enCode = "";
        try {
            enPort = Base64.encodeBase64String(RSACoder.encryptByPublicKey(String.valueOf(Bukkit.getServer().getPort()).getBytes()));
            enUsername = Base64.encodeBase64String(RSACoder.encryptByPublicKey(Login.getUsername().getBytes()));
            enCode = Base64.encodeBase64String(RSACoder.encryptByPublicKey(Login.getCode().getBytes()));
        } catch (Exception e) {
            sendError("插件数据转码错误");
        }
        nettyClient = new NettyClient();

        Map<String, String> loginMessage = new HashMap<>();
        loginMessage.put("username", enUsername);
        loginMessage.put("code", enCode);
        loginMessage.put("port", enPort);
        String result = PluginMessage.sendMsgToServer("LOGIN",loginMessage);
        try {
            if (result.isEmpty()) {
                sendError("服务器返回空数据，请检查是否使用正版插件");
                return;
            }
            byte[] packet = RSACoder.decryptByPublicKey(Base64.decodeBase64(result));
            String data = new String(packet, StandardCharsets.UTF_8);
            String code = new JsonParser().parse(data).getAsJsonObject().get("code").getAsString();
            String type = new JsonParser().parse(data).getAsJsonObject().get("type").getAsString();
            if (!"200".equals(code)) {
                if ("error_report".equals(type)) {
                    sendError("向服务器数据发送失败");
                } else if ("error_code".equals(type)) {
                    sendError("使用码填写错误");
                } else if ("error_ip".equals(type)) {
                    sendError("已有另一台不同IP地址的机器正在使用！");
                } else if ("error_cdk".equals(type)) {
                    sendError("激活码验证失败，请检查插件是否到期");
                } else if ("error_cdk_time".equals(type)) {
                    sendError("插件已到期");
                } else {
                    sendError("发生未知错误");
                }
            } else {
                if (type.startsWith("succeed")) {
                    String endTime = new JsonParser().parse(data).getAsJsonObject().get("end_time").getAsString();
                    //type = type.replace("succeed", "");
                    Bukkit.getServer().getConsoleSender().sendMessage(" §a= §e 与API服务器连接成功");
                    if ("forever".equals(endTime)) {
                        Bukkit.getServer().getConsoleSender().sendMessage(" §a= §3 用户插件到期时间：§6永久用户");
                    } else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Bukkit.getServer().getConsoleSender().sendMessage(" §a= §3 用户插件到期时间：§e" + simpleDateFormat.format(new Date(Long.parseLong(endTime))));
                    }
                    useCode = new JsonParser().parse(data).getAsJsonObject().get("useCode").getAsString();
                } else {
                    sendError("发生未知错误");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendError("插件解码出现错误，请重新加载插件");
        }

    }

    /**
     * 初始化Vault
     */
    public void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static VexMusic getInstance() {
        return instance;
    }

    public static String getChannel() {
        return CHANNEL;
    }

    public static String getUseCode() {
        return useCode;
    }
}
