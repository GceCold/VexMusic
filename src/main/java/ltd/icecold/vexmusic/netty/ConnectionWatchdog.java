package ltd.icecold.vexmusic.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import ltd.icecold.vexmusic.VexMusic;
import ltd.icecold.vexmusic.command.CommandHandler;
import ltd.icecold.vexmusic.events.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/**
 * @author ice_cold
 * @date Create in 9:09 2020/5/5
 */
@ChannelHandler.Sharable
public abstract class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask, ChannelHandlerHolder {
    private final Bootstrap bootstrap;
    private final Timer timer;
    private final int port;

    private final String host;

    private volatile boolean reconnect;
    private int attempts = 0;

    public ConnectionWatchdog(Bootstrap bootstrap, Timer timer, int port, String host, boolean reconnect) {
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.port = port;
        this.host = host;
        this.reconnect = reconnect;
    }

    /**
     * channel链路每次active的时候，将其连接的次数重新☞ 0
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        NettyClient.socketChannel = (SocketChannel) ctx.channel();
        attempts = 0;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Bukkit.getConsoleSender().sendMessage("§7[VexMusic] >§c与服务器断开连接");
        boolean isChannelActive = true;
        if (VexMusic.isCancelConnection) {
            return;
        }
        if (null == NettyClient.socketChannel || !NettyClient.socketChannel.isActive()) {
            Bukkit.getConsoleSender().sendMessage("§7[VexMusic] >§c与服务器断开连接，正在重连");
            isChannelActive = false;
        }
        if (reconnect && !isChannelActive) {
            if (attempts < 60) {
                int timeout = 10;
                timer.newTimeout(this, timeout, TimeUnit.SECONDS);
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("§7[Vexmusic] >§e与服务器连接错误");
        }
    }


    @Override
    public void run(Timeout timeout) {
        ChannelFuture future;
        synchronized (bootstrap) {
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(handlers());
                }
            });
            future = bootstrap.connect(host, port);
        }

        //future对象监听
        future.addListener((ChannelFutureListener) f -> {
            boolean succeed = f.isSuccess();
            Channel channel = f.channel();
            if (!succeed) {
                Bukkit.getConsoleSender().sendMessage("§7[Vexmusic] >§c重新连接失败");
                f.channel().pipeline().fireChannelInactive();
            } else {
                NettyClient.socketChannel = (SocketChannel) channel;
                Bukkit.getConsoleSender().sendMessage("§7[Vexmusic] >§b重新连接成功");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        VexMusic.checkUser();
                        if (Bukkit.getPluginCommand("music").isRegistered()) {
                            Bukkit.getPluginCommand("music").setExecutor(new CommandHandler());
                            Bukkit.getMessenger().registerOutgoingPluginChannel(VexMusic.getInstance(), VexMusic.getChannel());
                            Bukkit.getMessenger().registerIncomingPluginChannel(VexMusic.getInstance(), VexMusic.getChannel(), new MessageListener());
                        }
                    }
                }.runTaskAsynchronously(VexMusic.getInstance());
            }
        });
    }

}
