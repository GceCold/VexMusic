package ltd.icecold.vexmusic.client.jmusic;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.*;


import javazoom.jl.decoder.*;
import javazoom.jl.player.*;

public class JPlayer extends Thread {

    private Player player;
    private String musicName;
    private static BufferedInputStream buffer;
    private Boolean loop;

    public JPlayer(String musicName,Boolean loop) {
        this.musicName = musicName;
        this.loop = loop;
    }

    @Override
    public void run() {
        while (loop) {
            try {
                buffer = new BufferedInputStream(new FileInputStream(musicName));
                player = new Player(buffer);
                player.play();
            } catch (JavaLayerException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return ;
            }
        }
    }

    public void close() {
        try {
            player.close();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
