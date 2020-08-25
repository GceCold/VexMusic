package ltd.icecold.vexmusic.gui.bean;

import java.util.List;

public class SearchMusicResultBean {

    /**
     * data : [{"id":176349,"name":"大海","artists":"杨培安"},{"id":194186,"name":"大海","artists":"钟镇涛"},{"id":188057,"name":"大海","artists":"张雨生"},{"id":27906981,"name":"大海","artists":"蓝雨"},{"id":28167216,"name":"大海","artists":"反光镜"},{"id":5242750,"name":"大海","artists":"张雨生"},{"id":244625,"name":"大海","artists":"黄绮珊"},{"id":29814499,"name":"大海","artists":"阿鲁阿卓#山风组合"},{"id":5250492,"name":"大海","artists":"张雨生"},{"id":5238223,"name":"大海","artists":"张雨生"},{"id":4877546,"name":"大海","artists":"THE JAYWALK"},{"id":1362887277,"name":"大海","artists":"舒暖纯音乐"},{"id":1420751303,"name":"大海","artists":"彭飞"},{"id":1417136636,"name":"大海","artists":"金锋#徐卫国#胡建#徐震涛#冯亚军#徐军林#万秋雯#宋寅#刘成义#唐顺杰"},{"id":67799,"name":"大海","artists":"陈建骐"},{"id":34167070,"name":"大海","artists":"群星"},{"id":278058,"name":"大海","artists":"曼里"},{"id":469065607,"name":"大海","artists":"黄格选#杨树林#男光音组合"},{"id":75067,"name":"大海","artists":"陈小平"},{"id":454828906,"name":"大海","artists":"张雨生"},{"id":33668317,"name":"大海","artists":"蔡幸娟"},{"id":1372350231,"name":"大海","artists":"MIKA STUDIO"},{"id":5274360,"name":"大海","artists":"群星"},{"id":419827606,"name":"大海","artists":"张雨生"},{"id":319345,"name":"大海","artists":"闫月"},{"id":5249848,"name":"大海","artists":"张雨生"},{"id":188039,"name":"我是一棵秋天的树","artists":"张雨生"},{"id":140772,"name":"大海","artists":"潘军"},{"id":187949,"name":"大海","artists":"张雨生"},{"id":69100,"name":"大海","artists":"陈伟联"}]
     * code : 200
     */

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
        /**
         * id : 176349
         * name : 大海
         * artists : 杨培安
         */

        private int id;
        private String name;
        private String artists;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArtists() {
            return artists;
        }

        public void setArtists(String artists) {
            this.artists = artists;
        }
    }
}