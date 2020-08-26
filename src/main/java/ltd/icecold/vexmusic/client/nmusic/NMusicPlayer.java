package ltd.icecold.vexmusic.client.nmusic;

/**
 * @author ice_cold
 * @date Create in 8:40 2020/8/4
 */
public class NMusicPlayer {
    private String musicCover;
    private String musicName;
    public String musicArtist;
    private String musicUrl;

    public NMusicPlayer(String musicCover, String musicName, String musicArtist, String musicUrl) {
        this.musicCover = musicCover;
        this.musicName = musicName;
        this.musicArtist = musicArtist;
        this.musicUrl = musicUrl;
    }

    public String getMusicCover() {
        return musicCover;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getMusicArtist() {
        return musicArtist;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicCover(String musicCover) {
        this.musicCover = musicCover;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }
}
