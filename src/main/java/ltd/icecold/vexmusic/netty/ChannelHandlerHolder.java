package ltd.icecold.vexmusic.netty;

import io.netty.channel.ChannelHandler;

/**
 * @author ice_cold
 * @description
 * @date Create in 9:20 2020/5/5
 */
public interface ChannelHandlerHolder {
    ChannelHandler[] handlers();
}
