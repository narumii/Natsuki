package org.spigotmc;

import net.minecraft.server.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import oshi.hardware.NetworkIF;
import pw.narumi.Natsuki;
import pw.narumi.common.Utils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Collections;

public class TicksPerSecondCommand extends Command
{

    public TicksPerSecondCommand(String name)
    {
        super( name );
        this.description = "Gets the current ticks per second for the server / [] = optional (can be null/empty)";
        this.usageMessage = "/gc [threads/info/can be empty]";
        this.setAliases(Collections.singletonList("gc"));
        this.setPermission( "bukkit.command.tps" );
    }


    //ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (Natsuki.getInstance().getConfig().isCheckTpsPermission() && !testPermission(sender)) {
            return true;
        }
        if (args.length > 0 && (args[0].equalsIgnoreCase("threads") || args[0].equalsIgnoreCase("watki") || args[0].equalsIgnoreCase("usage"))) {
            sender.sendMessage("\n                   §d* §fThreads §d*                    ");
            long size = 0;
            final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            for (final Thread thread : Thread.getAllStackTraces().keySet()) {
                size += threadMXBean.getThreadCpuTime(thread.getId());
                if (threadMXBean.getThreadCpuTime(thread.getId()) > 0) {
                    long l = threadMXBean.getThreadCpuTime(thread.getId()) * 100L / size;
                    if (l > 0) sender.sendMessage(" §8» §7" + thread.getName() + " §8-> §d" + l + "%");
                }
            }
            return true;
        }
        final Runtime runtime = Runtime.getRuntime();
        if (args.length > 0 && (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("system") || args[0].equalsIgnoreCase("machine") || args[0].equalsIgnoreCase("information"))) {
            sender.sendMessage("\n                      §d* §fInfo §d*                    ");
            sender.sendMessage(" §8» §7Processor arch: §d" + Utils.getOsBean().getArch());
            sender.sendMessage(" §8» §7System: §d" + Utils.getSystemInfo().getOperatingSystem().getFamily() + Utils.getSystemInfo().getOperatingSystem().getVersionInfo());
            sender.sendMessage(" §8» §7Processor: §d" + Utils.getSystemInfo().getHardware().getProcessor().getName());
            sender.sendMessage(" §8» §7Available processors: §d" + runtime.availableProcessors());
            for (final NetworkIF networkIF : Utils.getSystemInfo().getHardware().getNetworkIFs()) {
                sender.sendMessage(" §8» §7Network (" + networkIF.getDisplayName() + ")§8 -> §7speed: §d" + Utils.humanReadableByteCountInternet(networkIF.getSpeed()) + " §8| §7bytes sent: §d" + Utils.humanReadableByteCount(networkIF.getBytesSent()) + " §8| §7bytes received: §d" + Utils.humanReadableByteCount(networkIF.getBytesRecv()));
            }
            return true;
        }

        final long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        final double[] tpsA = DedicatedServer.getServer().recentTps;
        sender.sendMessage("\n                    §d* §fServer §d*                    ");
        sender.sendMessage(" §8» §7TPS: §d" + Math.min(Math.round(tpsA[0] * 100.0) / 100.0, 20));
        sender.sendMessage(" §8» §7RAM usage: §d" + Utils.humanReadableByteCount(usedMemory) + "§7/§5" + Utils.humanReadableByteCount(runtime.totalMemory()));
        sender.sendMessage(" §8» §7CPU usage: §d" + Utils.getFormat().format(Utils.getOsBean().getProcessCpuLoad() * 100) + "§d%");
        sender.sendMessage(" §8» §7Uptime: " + Utils.getUpTime(ManagementFactory.getRuntimeMXBean().getUptime()));
        sender.sendMessage("\n                     §d* §fWorld §d*                    ");
        sender.sendMessage(" §8» §7Loaded chunks: §d" + Bukkit.getServer().getWorlds().get(0).getLoadedChunks().length);
        sender.sendMessage(" §8» §7Loaded entities: §d" + Bukkit.getServer().getWorlds().get(0).getLivingEntities().size());
        sender.sendMessage("\n                    §d* §fSystem §d*                    ");
        sender.sendMessage(" §8» §7RAM usage: §d" + Utils.humanReadableByteCount(Utils.getSystemInfo().getHardware().getMemory().getTotal() -  Utils.getSystemInfo().getHardware().getMemory().getAvailable()) + "§7/§5" + Utils.humanReadableByteCount(Utils.getSystemInfo().getHardware().getMemory().getTotal()));
        sender.sendMessage(" §8» §7CPU usage: §d" + Utils.getFormat().format(Utils.getOsBean().getSystemCpuLoad() * 100) + "§d%");
       //sender.sendMessage(" §8» §7Processors: §d" + runtime.availableProcessors());
        //final NetworkIF networkIF = Utils.getSystemInfo().getHardware().getNetworkIFs()[0];
        //sender.sendMessage(" §8» §7Network§8 -> §7speed: §d" + Utils.humanReadableByteCountInternet(networkIF.getSpeed()) + " §8| §7bytes sent: §d" + Utils.humanReadableByteCount(networkIF.getBytesSent()) + " §8| §7bytes received: §d" + Utils.humanReadableByteCount(networkIF.getBytesRecv()));
        /*sender.sendMessage("\n                      §d* §fInfo §d*                    ");
        sender.sendMessage(" §8» §7Processor arch: §d" + Utils.getOsBean().getArch());
        sender.sendMessage(" §8» §7System: §d" + Utils.getSystemInfo().getOperatingSystem().getFamily() + Utils.getSystemInfo().getOperatingSystem().getVersionInfo());
        sender.sendMessage(" §8» §7Processor: §d" + Utils.getSystemInfo().getHardware().getProcessor().getName());*/
        return true;
    }
}
