package pw.narumi.common;

import net.minecraft.server.PacketDataSerializer;
import pw.narumi.exception.NatsukiException;

public class PacketUtil {

  public static int checkHandshake(PacketDataSerializer buf) throws NatsukiException {
        final int packetId = buf.readVarInt2();
    if (!buf.isReadable() || packetId != 0) {
      throw new NatsukiException("Invalid handshake packet id");
    }

        final int protocolId = buf.readVarInt2();
    if (!buf.isReadable() || protocolId != 47) {
      throw new NatsukiException("Invalid handshake protocol id");
    }

        final byte[] host = new byte[buf.readVarInt2()];
        buf.readBytes(host);
    if (buf.readableBytes() <= 2 || host.length <= 0) {
      throw new NatsukiException("Invalid handshake host");
    }

        final int port = buf.readUnsignedShort();
    if (!buf.isReadable() || buf.readableBytes() > 1 || port <= 0) {
      throw new NatsukiException("Invalid handshake port");
    }

        final int stateId = buf.readVarInt2();
    if (buf.isReadable() || (stateId != 1 && stateId != 2)) {
      throw new NatsukiException("Invalid handshake state");
    }

        return stateId;
    }

  public static void checkLogin(PacketDataSerializer buf) throws NatsukiException {
        final int packetId = buf.readVarInt2();
    if (!buf.isReadable() || packetId != 0) {
      throw new NatsukiException("Invalid login packet id");
    }

        final byte[] bytes = new byte[buf.readVarInt2()];
        buf.readBytes(bytes);
        final String nick = new String(bytes);
    if (buf.isReadable() || nick.length() > 16 || nick.length() <= 2) {
      throw new NatsukiException("Invalid login nick");
    }
    }
}
