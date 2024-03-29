package org.bukkit.block;

import org.bukkit.SkullType;

/**
 * Represents a Skull
 */
public interface Skull extends BlockState {

  /**
   * Checks to see if the skull has an owner
   *
   * @return true if the skull has an owner
   */
  boolean hasOwner();

  /**
   * Gets the owner of the skull, if one exists
   *
   * @return the owner of the skull or null if the skull does not have an owner
   */
  String getOwner();

  /**
   * Sets the owner of the skull
   * <p>
   * Involves a potentially blocking web request to acquire the profile data for the provided name.
   *
   * @param name the new owner of the skull
   * @return true if the owner was successfully set
   */
  boolean setOwner(String name);

  /**
   * Gets the rotation of the skull in the world
   *
   * @return the rotation of the skull
   */
  BlockFace getRotation();

  /**
   * Sets the rotation of the skull in the world
   *
   * @param rotation the rotation of the skull
   */
  void setRotation(BlockFace rotation);

  /**
   * Gets the type of skull
   *
   * @return the type of skull
   */
  SkullType getSkullType();

  /**
   * Sets the type of skull
   *
   * @param skullType the type of skull
   */
  void setSkullType(SkullType skullType);
}
