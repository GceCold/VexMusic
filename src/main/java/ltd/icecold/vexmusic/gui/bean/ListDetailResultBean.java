package ltd.icecold.vexmusic.gui.bean;

import java.util.List;

/**
 * @author: gdenga
 * @date: 2019/8/23 9:16
 * @content:
 */
public class ListDetailResultBean {

    private String code;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String musicName;
        private int musicID;

        public String getMusicName() {
            return musicName;
        }

        public void setMusicName(String musicName) {
            this.musicName = musicName;
        }

        public int getMusicID() {
            return musicID;
        }

        public void setMusicID(int musicID) {
            this.musicID = musicID;
        }
    }
}
