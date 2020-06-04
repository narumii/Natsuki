package pw.narumi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.narumi.Natsuki;

public class AddressCommand extends Command {

    public AddressCommand() {
        super("Address");
    }


    //TAKIE TROCHE ROZJEBANE XD
    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (args.length < 1) {
            sender.sendMessage(" §8» §7Usage: §d/address <whitelist/blacklist/info>");
            return false;
        }

        if (args[0].equalsIgnoreCase("whitelist")) {
            if (args.length < 2) {
                sender.sendMessage(" §8» §7Usage: §d/address whitelist <address>");
                return false;
            }

            final String address = args[1].contains(":") ? args[1].split(":")[0] : args[1];

            if (Natsuki.getInstance().getWhiteListedAddresses().contains(address)) {
                Natsuki.getInstance().getWhiteListedAddresses().remove(address);
                sender.sendMessage(" §8» §7Removed §d" + address + "§7 ip from whitelist");
            }else {
                Natsuki.getInstance().getWhiteListedAddresses().add(address);
                sender.sendMessage(" §8» §7Added §d" + address + "§7 ip to whitelist");
            }
        }else if (args[0].equalsIgnoreCase("blacklist")) {
            if (args.length < 2) {
                sender.sendMessage(" §8» §7Usage: §d/address blacklist <address>");
                return false;
            }

            final String address = args[1].contains(":") ? args[1].split(":")[0] : args[1];

            if (Natsuki.getInstance().getBlockedAddresses().contains(address)) {
                Natsuki.getInstance().getBlockedAddresses().remove(address);
                sender.sendMessage(" §8» §7Removed §d" + address + "§7 ip from blacklist");
            }else {
                Natsuki.getInstance().getBlockedAddresses().add(address);
                sender.sendMessage(" §8» §7Added §d" + address + "§7 ip to blacklist");
            }
        }else if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(" §8» §7Blocked addresses: §d" + Natsuki.getInstance().getBlockedAddresses().size());
            sender.sendMessage(" §8» §7WhiteListed addresses: §d" + Natsuki.getInstance().getWhiteListedAddresses());
        }else {
            sender.sendMessage(" §8» §7Usage: §d/address <whitelist/blacklist/info>");
        }
        return false;
    }
}
