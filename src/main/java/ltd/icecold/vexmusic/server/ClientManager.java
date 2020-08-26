package ltd.icecold.vexmusic.server;


import com.google.gson.Gson;
import ltd.icecold.vexmusic.client.NetworkHandle;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientManager {
    public ArrayList<ChatSocket> chatSockets = new ArrayList<>();

    public void addClientSocket(Socket socket) {
        final ChatSocket chatSocket = new ChatSocket(socket, new ChatSocket.Callback() {

            @Override
            public void onReadSocket(ChatSocket cs, String msg) {
                if (msg.startsWith("MusicEnded_")){
                    Map<String,String> message = new HashMap<>();
                    message.put("type","musicEnd");
                    message.put("message",msg.replace("MusicEnded_",""));
                    NetworkHandle.sendMsgToPlugin(new Gson().toJson(message));
                }

            }

            @Override
            public void onError(ChatSocket cs, String error) {
                synchronized (chatSockets) {
                    chatSockets.remove(cs);
                }
            }
        });
        synchronized (chatSockets) {
            chatSockets.add(chatSocket);
        }

        chatSocket.start();
    }

    public void sendAll(String msg) {
        synchronized (chatSockets) {
            for (ChatSocket cs : chatSockets) {
                cs.send(msg);
            }
        }
    }

    public void close() throws IOException {
        synchronized (chatSockets) {
            for (ChatSocket socket : chatSockets) {
                socket.close();
            }
            chatSockets.clear();
        }
    }
}
