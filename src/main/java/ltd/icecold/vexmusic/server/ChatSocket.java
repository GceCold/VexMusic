package ltd.icecold.vexmusic.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatSocket {
    public interface Callback {
        void onReadSocket(ChatSocket chatSocket, String msg);
        void onError(ChatSocket chatSocket, String error);
    }

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Callback callback;

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public ChatSocket(Socket socket, Callback callback) {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.callback = callback;
    }

    public void send(String send) {
        try {
            outputStream.writeUTF(send);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String accept = null;
                while (true) {
                    try {
                        accept = inputStream.readUTF();
                        if (callback != null) {
                            callback.onReadSocket(ChatSocket.this, accept);
                        }
                    } catch (IOException e) {
                        if (callback != null) {
                            callback.onError(ChatSocket.this, e.getMessage());
                        }
                    }
                }
            }
        });
        thread.start();
    }

    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }
}
