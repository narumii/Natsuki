package pw.narumi.common;

import net.minecraft.server.PacketDataSerializer;
import pw.narumi.exception.NatsukiException;

public class PacketUtil {

    public static int checkHandshake(final PacketDataSerializer buf) throws NatsukiException {
        if (buf.readableBytes() > 300 || buf.readableBytes() < 5)
            throw new NatsukiException("Invalid Handshake packet");

        final int packetId = buf.readVarInt2();
        if (!buf.isReadable() || packetId != 0)
            throw new NatsukiException("Invalid Handshake packet");

        final int protocolId = buf.readVarInt2();
        if (!buf.isReadable() || protocolId <= 0)
            throw new NatsukiException("Invalid Handshake packet");

        final byte[] host = new byte[buf.readVarInt2()];
        buf.readBytes(host);
        if (buf.readableBytes() <= 2)
            throw new NatsukiException("Invalid Handshake packet");

        final int port = buf.readUnsignedShort();
        if (!buf.isReadable() || port <= 0 || buf.readableBytes() > 1)
            throw new NatsukiException("Invalid Handshake packet");

        final int stateId = buf.readVarInt2();
        if (buf.isReadable() || (stateId != 1 && stateId != 2))
            throw new NatsukiException("Invalid Handshake packet");

        return stateId;
    }

    public static void checkLogin(final PacketDataSerializer buf) throws NatsukiException {
        if (buf.readableBytes() > 40 || buf.readableBytes() < 5)
            throw new NatsukiException("Too long LoginStart");

        final int packetId = buf.readVarInt2();
        if (!buf.isReadable() || packetId != 0)
            throw new NatsukiException("Invalid LoginStart packet id");

        final byte[] bytes = new byte[buf.readVarInt2()];
        buf.readBytes(bytes);

        final String nick = new String(bytes);
        if (buf.isReadable() || nick.length() > 16 || nick.length() <= 2)
            throw new NatsukiException("Invalid LoginStart packet");
    }
}
