package pw.narumi.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Async {

    private static final Executor executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2,
            new ThreadFactoryBuilder().build());

    public static Executor getExecutor() {
        return executor;
    }
}
