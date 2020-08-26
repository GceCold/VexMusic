package ltd.icecold.vexmusic.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleChatService {

    public interface OnSocketAcceptListener {
        void onSocketAccept(Socket socket);
    }

    private ClientManager clientManager;
    private OnSocketAcceptListener onAcceptListener;
    private ServerSocket serverSocket;

    public SimpleChatService(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public void setOnAcceptListener(OnSocketAcceptListener listener) {
        onAcceptListener = listener;
    }

    public void startup() {
        // TODO Auto-generated method stub
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runStartup();
            }

        });
        thread.start();
    }

    public void shutdown() {
        try {
            clientManager.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runStartup() {
        try {
            serverSocket = new ServerSocket(0);
            while (true) {
                Socket socket = serverSocket.accept();
                if (onAcceptListener != null) {
                    onAcceptListener.onSocketAccept(socket);
                }

                clientManager.addClientSocket(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
