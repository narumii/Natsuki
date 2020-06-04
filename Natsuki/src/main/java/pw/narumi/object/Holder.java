package pw.narumi.object;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Holder {

    //YYY ZAJEBANE

    private static final Map<InetAddress, Integer> socketMap = new HashMap<>();

    public static Map<InetAddress, Integer> getSocketMap() {
        return socketMap;
    }

    private static AtomicInteger blacklistedJoins = new AtomicInteger();
    private static AtomicInteger channels = new AtomicInteger();

    public static void socketIncrease(final InetAddress key) {
        socketMap.replace(key, socketMap.get(key) + 1);
    }

    public static int socketGet(final InetAddress key) {
        return socketMap.get(key);
    }

    public static void socketAdd(final InetAddress key) {
        socketMap.put(key, 1);
    }

    public static void runTasks() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(()-> {
            socketMap.clear();
            channels.set(0);
        },0,1,TimeUnit.SECONDS);
    }

    public static AtomicInteger getBlacklistedJoins() {
        return blacklistedJoins;
    }

    public static AtomicInteger getChannels() {
        return channels;
    }
}
