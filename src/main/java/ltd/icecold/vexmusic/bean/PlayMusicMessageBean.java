package ltd.icecold.vexmusic.bean;

/**
 * @author ice_cold
 * @date Create in 3:23 2020/8/6
 */
public class PlayMusicMessageBean {

    /**
     * type : play_music_now
     * message : {"musicName":"","musicArtist":"","musicCover":"","musicUrl":""}
     */

    private String type;
    private MessageBean message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * musicName :
         * musicArtist :
         * musicCover :
         * musicUrl :
         */

        private String musicName;
        private String musicArtist;
        private String musicCover;
        private String musicUrl;

        public String getMusicName() {
            return musicName;
        }

        public void setMusicName(String musicName) {
            this.musicName = musicName;
        }

        public String getMusicArtist() {
            return musicArtist;
        }

        public void setMusicArtist(String musicArtist) {
            this.musicArtist = musicArtist;
        }

        public String getMusicCover() {
            return musicCover;
        }

        public void setMusicCover(String musicCover) {
            this.musicCover = musicCover;
        }

        public String getMusicUrl() {
            return musicUrl;
        }

        public void setMusicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
        }
    }
}
