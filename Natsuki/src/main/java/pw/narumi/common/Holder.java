package pw.narumi.common;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Holder {

    //private static final Map<String, Integer> channelMap = new HashMap<>();
    private static final List<String> whitelist = new ArrayList<>();
    private static final Set<String> blacklist = new HashSet<>();
    private static final Set<String> ping = new HashSet<>();
    private static final Set<String> verified = new HashSet<>();

    private static final AtomicInteger channels = new AtomicInteger();

    public static void startTask() {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            blacklist.clear();
            ping.clear();
            verified.clear();
        }, 0, 3, TimeUnit.MINUTES);
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            channels.set(0);
            //channelMap.clear();
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static List<String> getWhitelist() {
        return whitelist;
    }

    public static Set<String> getBlacklist() {
        return blacklist;
    }

    //public static Map<String, Integer> getChannelMap() {
    //return channelMap;
    //}

    public static AtomicInteger getChannels() {
        return channels;
    }

    public static Set<String> getPing() {
        return ping;
    }

    public static Set<String> getVerified() {
        return verified;
    }
}
