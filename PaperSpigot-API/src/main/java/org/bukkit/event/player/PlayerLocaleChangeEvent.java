package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when the locale of the player is changed.
 */
public class PlayerLocaleChangeEvent extends PlayerEvent {

  private static final HandlerList handlers = new HandlerList();
  private final String oldLocale;
  private final String newLocale;

  public PlayerLocaleChangeEvent(final Player player, final String oldLocale,
      final String newLocale) {
    super(player);
    this.oldLocale = oldLocale;
    this.newLocale = newLocale;
  }

  /**
   * Gets the locale the player switched from.
   *
   * @return player's old locale
   */
  public String getOldLocale() {
    return oldLocale;
  }

  /**
   * Gets the locale the player is changed to.
   *
   * @return player's new locale
   */
  public String getNewLocale() {
    return newLocale;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}