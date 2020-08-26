package ltd.icecold.vexmusic.server;


import java.net.Socket;

public class ServerMain implements SimpleChatService.OnSocketAcceptListener {
    private SimpleChatService service;
    public ServerMain(SimpleChatService service) {
        this.service = service;
        this.service.setOnAcceptListener(this);
        this.service.startup();
        System.out.println("服务器启动");
    }
    @Override
    public void onSocketAccept(Socket socket) {
        //播放器连接

    }

    public SimpleChatService getService() {
        return service;
    }
}
