package org.bukkit.entity;

/**
 * Represents a Creeper
 */
public interface Creeper extends Monster {

  /**
   * Checks if this Creeper is powered (Electrocuted)
   *
   * @return true if this creeper is powered
   */
  boolean isPowered();

  /**
   * Sets the Powered status of this Creeper
   *
   * @param value New Powered status
   */
  void setPowered(boolean value);
}
