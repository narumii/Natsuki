package org.spigotmc;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import net.minecraft.server.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.narumi.Natsuki;
import pw.narumi.common.Utils;

public class TicksPerSecondCommand extends Command {

  public TicksPerSecondCommand(String name) {
    super(name);
    this.description = "Gets the current ticks per second for the server / [] = optional (can be null/empty)";
    this.usageMessage = "/gc [info/can be empty]";
    this.setAliases(Collections.singletonList("gc"));
    this.setPermission("bukkit.command.tps");
  }

  @Override
  public boolean execute(CommandSender sender, String currentAlias, String[] args) {
    if (Natsuki.getInstance().getConfig().UTILS.tpsCommandPermission && !testPermission(sender)) {
      return true;
    }

    final Runtime runtime = Runtime.getRuntime();
    if (args.length > 0 && (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("system")
        || args[0].equalsIgnoreCase("machine") || args[0].equalsIgnoreCase("information"))) {
      sender.sendMessage("\n                      §d* §fInfo §d*                    ");
      sender.sendMessage(" §8» §7Processor arch: §d" + Utils.getOsBean().getArch());
      sender.sendMessage(
          " §8» §7System: §d" + Utils.getSystemInfo().getOperatingSystem().getFamily() + Utils
              .getSystemInfo().getOperatingSystem().getVersionInfo());
      sender.sendMessage(
          " §8» §7Processor: §d" + Utils.getSystemInfo().getHardware().getProcessor().getName());
      sender.sendMessage(" §8» §7Available processors: §d" + runtime.availableProcessors());
      return true;
    }

    final long usedMemory = runtime.totalMemory() - runtime.freeMemory();
    final double[] tpsA = DedicatedServer.getServer().recentTps;
    sender.sendMessage("\n                    §d* §fServer §d*                    ");
    sender.sendMessage(" §8» §7TPS: §d" + Math.min(Math.round(tpsA[0] * 100.0) / 100.0, 20));
    sender.sendMessage(
        " §8» §7RAM usage: §d" + Utils.humanReadableByteCount(usedMemory) + "§7/§5" + Utils
            .humanReadableByteCount(runtime.totalMemory()));
    sender.sendMessage(" §8» §7CPU usage: §d" + Utils.getFormat()
        .format(Utils.getOsBean().getProcessCpuLoad() * 100) + "§d%");
    sender.sendMessage(
        " §8» §7Uptime: " + Utils.getUpTime(ManagementFactory.getRuntimeMXBean().getUptime()));
    sender.sendMessage("\n                     §d* §fWorld §d*                    ");
    sender.sendMessage(" §8» §7Loaded chunks: §d" + Bukkit.getServer().getWorlds().get(0)
        .getLoadedChunks().length);
    sender.sendMessage(
        " §8» §7Loaded entities: §d" + Bukkit.getServer().getWorlds().get(0).getLivingEntities()
            .size());
    sender.sendMessage("\n                    §d* §fSystem §d*                    ");
    sender.sendMessage(" §8» §7RAM usage: §d" + Utils.humanReadableByteCount(
        Utils.getSystemInfo().getHardware().getMemory().getTotal() - Utils.getSystemInfo()
            .getHardware().getMemory().getAvailable()) + "§7/§5" + Utils
        .humanReadableByteCount(Utils.getSystemInfo().getHardware().getMemory().getTotal()));
    sender.sendMessage(" §8» §7CPU usage: §d" + Utils.getFormat()
        .format(Utils.getOsBean().getSystemCpuLoad() * 100) + "§d%");
    return true;
  }
}
