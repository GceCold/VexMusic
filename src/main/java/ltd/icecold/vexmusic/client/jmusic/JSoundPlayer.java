package ltd.icecold.vexmusic.client.jmusic;

import javazoom.jl.player.Player;

/**
 * @author ice_cold
 * @date Create in 8:43 2020/8/4
 */
public class JSoundPlayer {
    private String musicUrl;

    public JSoundPlayer(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

}
