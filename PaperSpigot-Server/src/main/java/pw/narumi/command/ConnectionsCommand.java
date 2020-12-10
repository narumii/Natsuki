package pw.narumi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConnectionsCommand extends Command {

  public ConnectionsCommand() {
    super("Connections");
  }

  @Override
  public boolean execute(final CommandSender sender, final String commandLabel,
      final String[] args) {
    if (sender instanceof Player) {
      ((Player) sender).setConnectionsVisible(!((Player) sender).connectionsVisible());
    }
    return false;
  }
}