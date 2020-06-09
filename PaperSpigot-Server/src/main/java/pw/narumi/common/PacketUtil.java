package pw.narumi.common;

import net.minecraft.server.PacketDataSerializer;
import pw.narumi.exception.NatsukiException;

public class PacketUtil {

    public static int checkHandshake(final PacketDataSerializer buf) throws NatsukiException {
        if (buf.readableBytes() > 300)
            throw new NatsukiException("Invalid Handshake packet");

        final int packetId = buf.readVarInt();
        if (!buf.isReadable() || packetId != 0)
            throw new NatsukiException("Invalid Handshake packet");

        final int protocolId = buf.readVarInt();
        if (!buf.isReadable() || protocolId != 47)
            throw new NatsukiException("Invalid Handshake packet");

        final byte[] host = new byte[buf.readVarInt()];
        buf.readBytes(host);
        if (buf.readableBytes() <= 2 || host.length < 5)
            throw new NatsukiException("Invalid Handshake packet");

        final int stateId = buf.readVarInt();
        if (buf.isReadable() || (stateId != 1 && stateId != 2))
            throw new NatsukiException("Invalid Handshake packet");

        return stateId;
    }

    public static void checkLogin(final PacketDataSerializer buf) throws NatsukiException {
        if (buf.readableBytes() > 20)
            throw new NatsukiException("Invalid Handshake packet");

        final int packetId = buf.readVarInt();
        if (!buf.isReadable() || packetId != 0)
            throw new NatsukiException("Invalid LoginStart packet");

        final byte[] host = new byte[buf.readVarInt()];
        buf.readBytes(host);
        final String nick = new String(host);
        if (buf.isReadable() || nick.length() > 16 || nick.length() <= 2)
            throw new NatsukiException("Invalid LoginStart packet");
    }
}
