package pw.narumi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.narumi.common.Holder;

public class AddressCommand extends Command {

  public AddressCommand() {
    super("Address");
  }


  //TAKIE TROCHE ROZJEBANE XD
  @Override
  public boolean execute(final CommandSender sender, final String commandLabel,
      final String[] args) {
    if (!sender.hasPermission("natsuki.address")) {
      return true;
    }

    if (args.length < 1) {
      sender.sendMessage(" §8» §7Usage: §d/address <whitelist/info>");
      return false;
    }

    if (args[0].equalsIgnoreCase("whitelist")) {
      if (args.length < 2) {
        sender.sendMessage(" §8» §7Usage: §d/address whitelist <address>");
        return false;
      }

      final String address = args[1].contains(":") ? args[1].split(":")[0] : args[1];

      if (Holder.getWhitelist().contains(address)) {
        Holder.getWhitelist().remove(address);
        sender.sendMessage(" §8» §7Removed §d" + address + "§7 ip from whitelist");
      } else {
        Holder.getWhitelist().add(address);
        sender.sendMessage(" §8» §7Added §d" + address + "§7 ip to whitelist");
      }
    } else {
      sender.sendMessage(" §8» §7Blocked addresses: §d" + Holder.getBlacklist().size());
      sender.sendMessage(" §8» §7WhiteListed addresses: §d" + Holder.getWhitelist().size());
    }

    return false;
  }
}
