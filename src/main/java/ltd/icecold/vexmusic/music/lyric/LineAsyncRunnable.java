package ltd.icecold.vexmusic.music.lyric;

import ltd.icecold.vexmusic.config.Config;
import ltd.icecold.vexmusic.utils.ActionBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class LineAsyncRunnable extends Thread{
    public static ConcurrentHashMap<String,LineAsyncRunnable> runnableList = new ConcurrentHashMap<>();
    public static Executor executor = Executors.newCachedThreadPool();
    private boolean stop = false;
    List<String> lines = new ArrayList<>();
    ConcurrentHashMap<Long,String> timeTickLine = new ConcurrentHashMap<>();
    String lastLine = null;
    Long nowTick = 0L;
    Player listener;
    long largest = 0L;
    public LineAsyncRunnable(Player who, String lines) {
        this.lines.addAll(Arrays.asList(lines.split(Pattern.quote("\n"))));
        for (String object : this.lines) {
            if (object.contains(Pattern.quote("\n"))) {
                object = object.replace(Pattern.quote("\n"), "");
            }
            if (object.contains(Pattern.quote("\r"))) {
                object = object.replace(Pattern.quote("\r"), "");
            }
            try {
                String line = object.split("]", 2)[1];
                if (line.contains("][")) {
                    line = line.replace("][", "");
                    line = line.split("]", 2)[1];
                } else if (line.contains("]")) {
                    line = line.split("]", 2)[1];
                }
                String timeDrop = object.split("]", 2)[0]
                        .replace("[", "").replace("]", "");
                String minute = timeDrop.split(":")[0];
                String others = timeDrop.split(":")[1];
                String second = others.split(Pattern.quote("."))[0];
                String min_second = others.split(Pattern.quote("."))[1];
                int numberSize = Integer.valueOf(min_second);
                if (numberSize > 700) {
                    numberSize = 1000;
                } else {
                    numberSize = 0;
                }
                Long time = (long) (numberSize + Integer.valueOf(second) *
                        1000 + Integer.valueOf(minute) * 1000 * 60);
                timeTickLine.put(time, line);
            } catch (Exception exc) {
                continue;
            }
        }
        this.listener = who;
        Long[] numbers = timeTickLine.keySet().toArray(new Long[timeTickLine.size()]);
        Arrays.sort(numbers);
        this.largest = numbers[numbers.length - 1];
    }

    @Override
    public void run() {
        while (!stop) {
            if (this.listener == null || !this.listener.isOnline()) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nowTick = nowTick + 500;
            executor.execute(() -> {
                if (timeTickLine.getOrDefault(nowTick, null) != null) {
                    this.lastLine = timeTickLine.get(nowTick);
                }
                if (this.lastLine != null) {
                    ActionBarAPI.sendActionBar(this.listener, ChatColor.valueOf((String)Config.get("lyric.color")) + this.lastLine);
                    if (this.nowTick >= this.largest) {
                        this.stopMe();
                    }
                }
            });
        }
    }

    public void stopMe() {
        stop = true;
    }
}
