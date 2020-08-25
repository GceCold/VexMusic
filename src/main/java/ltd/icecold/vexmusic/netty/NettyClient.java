package ltd.icecold.vexmusic.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.HashedWheelTimer;
import ltd.icecold.vexmusic.VexMusic;
import ltd.icecold.vexmusic.interfaceservice.MessageService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler client;
    public static SocketChannel socketChannel;
    private static ChannelFuture futrue;
    protected static final HashedWheelTimer timer = new HashedWheelTimer();

    public void disconnectServer(){
        socketChannel.close();
    }
    /**
     * 获取代理对象
     * @param serviceClass
     * @param providerName
     * @return
     */
    public Object getBean(ClassLoader classLoader, final Class<?> serviceClass, String providerName){
        return Proxy.newProxyInstance(
                classLoader,
                new Class<?>[] {serviceClass},
                ((proxy, method, args) -> {
                    if (client == null){
                        initClient();
                    }
                    client.setPara(providerName + args[0]);
                    return executor.submit(client).get();
                })
        );
    }


    /**
     * 初始化客户端
     */
    private static void initClient(){
        client = new NettyClientHandler();
        //创建EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                ChannelPipeline pipeline = socketChannel.pipeline();
                                pipeline
                                        .addLast(new LengthFieldPrepender(2))
                                        .addLast(new LengthFieldBasedFrameDecoder(64*1024,0,2,0,2))
                                        .addLast(new StringEncoder(StandardCharsets.UTF_8))
                                        .addLast(new StringDecoder(Charset.forName("GBK")))
                                        .addLast(client);
                            }
                        }
                );
        final ConnectionWatchdog watchdog = new ConnectionWatchdog(bootstrap, timer, VexMusic.MUSIC_API_HOST_PORT,VexMusic.MUSIC_API_HOST_NAME, true) {
            @Override
            public ChannelHandler[] handlers() {
                return new ChannelHandler[] {
                        this,
                        new LengthFieldPrepender(2),
                        new LengthFieldBasedFrameDecoder(64*1024,0,2,0,2),
                        new StringEncoder(StandardCharsets.UTF_8),
                        new StringDecoder(Charset.forName("GBK")),
                        client
                };
            }
        };
        try {
            synchronized (bootstrap) {
                bootstrap.remoteAddress(VexMusic.MUSIC_API_HOST_NAME,VexMusic.MUSIC_API_HOST_PORT);
                bootstrap.handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(watchdog.handlers());
                    }
                });
                futrue = bootstrap.connect(VexMusic.MUSIC_API_HOST_NAME,VexMusic.MUSIC_API_HOST_PORT);
            }
            futrue.sync();
            if(futrue.isSuccess()) {
                socketChannel = (SocketChannel) futrue.channel();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        MessageService messageService = (MessageService) VexMusic.nettyClient.getBean(VexMusic.getInstance().getClass().getClassLoader(), MessageService.class, "#VEXMUSIC_ICECOLD#");
                        messageService.message("Heart");
                    }
                }.runTaskTimerAsynchronously(VexMusic.getInstance(),0,20*10);
            }
        }catch (Exception e){
            Bukkit.getConsoleSender().sendMessage("§c [VexMusic] > 服务器连接失败");
            e.printStackTrace();
            if (null != futrue) {
                try {
                    timer.newTimeout(watchdog,60, TimeUnit.SECONDS);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
