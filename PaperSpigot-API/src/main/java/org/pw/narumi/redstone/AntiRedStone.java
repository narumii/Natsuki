package org.pw.narumi.redstone;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockRedstoneEvent;

public class AntiRedStone {

  private static boolean check;

  public static void startTask(final boolean c) {
    Executors.newScheduledThreadPool(1)
        .scheduleWithFixedDelay(chunkRedstoneUpdates::clear, 0, 1, TimeUnit.SECONDS);
    check = c;
  }

  public static synchronized void syncAntiRedStone(final Event event) {
    if (!check) {
      return;
    }

    if (event instanceof BlockRedstoneEvent) {
      final BlockRedstoneEvent blockEvent = (BlockRedstoneEvent) event;

      if (blockEvent.getBlock() == null) {
        return;
      }

      final Material material = blockEvent.getBlock().getType();
      final org.bukkit.Chunk chunk = blockEvent.getBlock().getChunk();

      if (chunk != null) {
        if (material == Material.REDSTONE || material == Material.PISTON_BASE) {
          if (!chunkRedstoneUpdates.containsKey(chunk)) {
            chunkRedstoneUpdates.put(chunk, 1);
          } else {
            chunkRedstoneUpdates.put(
                chunk,
                chunkRedstoneUpdates.getOrDefault(chunk, 1) + 1);
          }
        }

        if (chunkRedstoneUpdates.getOrDefault(chunk, 1) > 300) {
          blockEvent.setNewCurrent(0);
        }
      }
    }
  }

  public static void antiRedStone(final Event event) {
    if (!check) {
      return;
    }

    if (event instanceof BlockRedstoneEvent) {
      final BlockRedstoneEvent blockEvent = (BlockRedstoneEvent) event;

      if (blockEvent.getBlock() == null) {
        return;
      }

      final Material material = blockEvent.getBlock().getType();
      final org.bukkit.Chunk chunk = blockEvent.getBlock().getChunk();

      if (chunk != null) {
        if (material == Material.REDSTONE || material == Material.PISTON_BASE) {
          if (!chunkRedstoneUpdates.containsKey(chunk)) {
            chunkRedstoneUpdates.put(chunk, 1);
          } else {
            chunkRedstoneUpdates.put(
                chunk,
                chunkRedstoneUpdates.getOrDefault(chunk, 1) + 1);
          }
        }

        if (chunkRedstoneUpdates.getOrDefault(chunk, 1) > 300) {
          blockEvent.setNewCurrent(0);
        }
      }
    }
  }

  private static final Map<Chunk, Integer> chunkRedstoneUpdates = new ConcurrentHashMap<>();
}
