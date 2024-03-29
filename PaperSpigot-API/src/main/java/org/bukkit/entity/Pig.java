package org.bukkit.entity;

/**
 * Represents a Pig.
 */
public interface Pig extends Animals, Vehicle {

  /**
   * Check if the pig has a saddle.
   *
   * @return if the pig has been saddled.
   */
  boolean hasSaddle();

  /**
   * Sets if the pig has a saddle or not
   *
   * @param saddled set if the pig has a saddle or not.
   */
  void setSaddle(boolean saddled);
}
