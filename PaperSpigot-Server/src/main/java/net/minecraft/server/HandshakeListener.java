package net.minecraft.server;

// CraftBukkit start


import json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pw.narumi.Natsuki;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashMap;
// CraftBukkit end

public class HandshakeListener implements PacketHandshakingInListener {

    private static final com.google.gson.Gson gson = new com.google.gson.Gson(); // Spigot
    // CraftBukkit start - add fields
    private static final HashMap<InetAddress, Long> throttleTracker = new HashMap<InetAddress, Long>();
    private static int throttleCounter = 0;
    // CraftBukkit end

    private final MinecraftServer a;
    private final NetworkManager b;

    public HandshakeListener(MinecraftServer minecraftserver, NetworkManager networkmanager) {
        this.a = minecraftserver;
        this.b = networkmanager;
    }

    //SYF KOD WIEM
    private void handleAddressCheck() {
        try {
            if (Natsuki.getInstance().getConfig().API.proxyCheckKey.equals("Your Key") || Natsuki.getInstance().getConfig().API.proxyCheckKey.equals("none")) {
                final InetAddress address = ((InetSocketAddress) b.getSocketAddress()).getAddress();
                final String json = new String(IOUtils.toByteArray(new URL("http://api.stopforumspam.org/api?ip=%address%&json"
                        .replace("%address%", address.getHostAddress()))
                        .openStream()));

                final JSONObject obj = new JSONObject(json).getJSONObject("ip");

                if (obj.getInt("appears") == 1) {
                    if (!Natsuki.getInstance().getBlockedAddresses().contains(address.getHostAddress()))
                        Natsuki.getInstance().getBlockedAddresses().add(address.getHostAddress());

                    final ChatComponentText message = new ChatComponentText(Natsuki.getInstance().getConfig().PREFIX + "\n\n" + Natsuki.getInstance().getConfig().MESSAGES.get("ProxyOrVpnKick"));
                    this.b.handle(new PacketLoginOutDisconnect(message));
                    this.b.close(message);
                }
            }else {
                final InetAddress address = ((InetSocketAddress) b.getSocketAddress()).getAddress();
                final String json = new String(IOUtils.toByteArray(new URL("http://proxycheck.io/v2/%address%?&vpn=1&asn=1&risk=1&key=%key%"
                        .replace("%address%", address.getHostAddress())
                        .replace("%key%", Natsuki.getInstance().getConfig().API.proxyCheckKey))
                        .openStream()));
                final JSONObject obj = new JSONObject(json).getJSONObject(address.getHostAddress());

                if (obj.getString("proxy").equalsIgnoreCase("yes") || obj.getInt("risk") > 40) {
                    if (!Natsuki.getInstance().getBlockedAddresses().contains(address.getHostAddress()))
                        Natsuki.getInstance().getBlockedAddresses().add(address.getHostAddress());

                    final ChatComponentText message = new ChatComponentText(
                            Natsuki.getInstance().getConfig().PREFIX
                                    + "\n\n" +
                                    Natsuki.getInstance().getConfig().MESSAGES.get("ProxyOrVpnKick"));

                    this.b.handle(new PacketLoginOutDisconnect(message));
                    this.b.close(message);
                }
            }
        } catch (final Exception ignored) {
        }
    }

    public void a(PacketHandshakingInSetProtocol packethandshakinginsetprotocol) {
        switch (HandshakeListener.SyntheticClass_1.a[packethandshakinginsetprotocol.a().ordinal()]) {
        case 1:
            final String addressA = ((InetSocketAddress)b.getSocketAddress()).getAddress().getHostAddress();

            if (!Natsuki.getInstance().getWhiteListedAddresses().contains(addressA)
                    && Natsuki.getInstance().getConfig().CONNECTION.addressCheck)
                handleAddressCheck();

            //SYF KOD WIEM
            if (Natsuki.getInstance().getConfig().CONNECTION.maxConnections != -1) {
                int same = 1;
                for (final Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                    if (onlinePlayer.getAddress().getAddress().getHostAddress().equals(addressA))
                        same++;
                }

                if (same > Natsuki.getInstance().getConfig().CONNECTION.maxConnections) {
                    final ChatComponentText message = new ChatComponentText(Natsuki.getInstance().getConfig().PREFIX + "\n\n" + Natsuki.getInstance().getConfig().MESSAGES.get("MaxConnectionsPerIp"));
                    this.b.handle(new PacketLoginOutDisconnect(message));
                    this.b.close(message);
                    return;
                }
            }

            this.b.a(EnumProtocol.LOGIN);
            ChatComponentText chatcomponenttext;

            // CraftBukkit start - Connection throttle
            try {
                long currentTime = System.currentTimeMillis();
                long connectionThrottle = MinecraftServer.getServer().server.getConnectionThrottle();
                InetAddress address = ((java.net.InetSocketAddress) this.b.getSocketAddress()).getAddress();

                synchronized (throttleTracker) {
                    if (throttleTracker.containsKey(address) && !"127.0.0.1".equals(address.getHostAddress()) && currentTime - throttleTracker.get(address) < connectionThrottle) {
                        throttleTracker.put(address, currentTime);
                        chatcomponenttext = new ChatComponentText("Connection throttled! Please wait before reconnecting.");
                        this.b.handle(new PacketLoginOutDisconnect(chatcomponenttext));
                        this.b.close(chatcomponenttext);
                        return;
                    }

                    throttleTracker.put(address, currentTime);
                    throttleCounter++;
                    if (throttleCounter > 200) {
                        throttleCounter = 0;

                        // Cleanup stale entries
                        java.util.Iterator iter = throttleTracker.entrySet().iterator();
                        while (iter.hasNext()) {
                            java.util.Map.Entry<InetAddress, Long> entry = (java.util.Map.Entry) iter.next();
                            if (entry.getValue() > connectionThrottle) {
                                iter.remove();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                org.apache.logging.log4j.LogManager.getLogger().debug("Failed to check connection throttle", t);
            }
            // CraftBukkit end

            if (packethandshakinginsetprotocol.b() > 47) {
                chatcomponenttext = new ChatComponentText( java.text.MessageFormat.format( org.spigotmc.SpigotConfig.outdatedServerMessage.replaceAll("'", "''"), "1.8.8" ) ); // Spigot
                this.b.handle(new PacketLoginOutDisconnect(chatcomponenttext));
                this.b.close(chatcomponenttext);
            } else if (packethandshakinginsetprotocol.b() < 47) {
                chatcomponenttext = new ChatComponentText( java.text.MessageFormat.format( org.spigotmc.SpigotConfig.outdatedClientMessage.replaceAll("'", "''"), "1.8.8" ) ); // Spigot
                this.b.handle(new PacketLoginOutDisconnect(chatcomponenttext));
                this.b.close(chatcomponenttext);
            } else {
                this.b.a((PacketListener) (new LoginListener(this.a, this.b)));
                // Spigot Start
                if (org.spigotmc.SpigotConfig.bungee) {
                    String[] split = packethandshakinginsetprotocol.hostname.split("\00");
                    if ( split.length == 3 || split.length == 4 ) {
                        packethandshakinginsetprotocol.hostname = split[0];
                        b.l = new java.net.InetSocketAddress(split[1], ((java.net.InetSocketAddress) b.getSocketAddress()).getPort());
                        b.spoofedUUID = com.mojang.util.UUIDTypeAdapter.fromString( split[2] );
                    } else
                    {
                        chatcomponenttext = new ChatComponentText("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
                        this.b.handle(new PacketLoginOutDisconnect(chatcomponenttext));
                        this.b.close(chatcomponenttext);
                        return;
                    }
                    if ( split.length == 4 )
                    {
                        b.spoofedProfile = gson.fromJson(split[3], com.mojang.authlib.properties.Property[].class);
                    }
                }
                // Spigot End
                ((LoginListener) this.b.getPacketListener()).hostname = packethandshakinginsetprotocol.hostname + ":" + packethandshakinginsetprotocol.port; // CraftBukkit - set hostname
            }
            break;

        case 2:
            this.b.a(EnumProtocol.STATUS);
            this.b.a((PacketListener) (new PacketStatusListener(this.a, this.b)));
            break;

        default:
            if (!Natsuki.getInstance().getBlockedAddresses().contains(((java.net.InetSocketAddress) b.getSocketAddress()).getAddress().getHostAddress()))
                Natsuki.getInstance().getBlockedAddresses().add(((java.net.InetSocketAddress) b.getSocketAddress()).getAddress().getHostAddress());
            this.b.channel.close();
            throw new UnsupportedOperationException("Invalid intention " + packethandshakinginsetprotocol.a());
        }

    }

    public void a(IChatBaseComponent ichatbasecomponent) {}

    static class SyntheticClass_1 {

        static final int[] a = new int[EnumProtocol.values().length];

        static {
            try {
                HandshakeListener.SyntheticClass_1.a[EnumProtocol.LOGIN.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                HandshakeListener.SyntheticClass_1.a[EnumProtocol.STATUS.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

        }
    }
}
