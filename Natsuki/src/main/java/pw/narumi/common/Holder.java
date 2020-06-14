package pw.narumi.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Holder {

    private static final Map<String, Integer> channelMap = new HashMap<>();
    private static final AtomicInteger channels = new AtomicInteger();
    private static final List<String> whitelist = new ArrayList<>();
    private static final Set<String> blacklist = new HashSet<>();

    public static void startTask() {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(blacklist::clear, 0, 3, TimeUnit.MINUTES);
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            channels.set(0);
            channelMap.clear();
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static List<String> getWhitelist() {
        return whitelist;
    }

    public static Set<String> getBlacklist() {
        return blacklist;
    }

    public static Map<String, Integer> getChannelMap() {
        return channelMap;
    }

    public static AtomicInteger getChannels() {
        return channels;
    }
}
