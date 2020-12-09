package pw.narumi.holder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.Arrays;
import java.util.List;
import net.minecraft.server.EnumProtocolDirection;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import pw.narumi.Natsuki;
import pw.narumi.common.PacketUtil;
import pw.narumi.exception.NatsukiException;

public class NatsukiPacketDecoder extends ByteToMessageDecoder {

    private final EnumProtocolDirection direction;
    private int packetState = 0;
    private int handshakeIntent = 0;

    public NatsukiPacketDecoder(final EnumProtocolDirection direction) {
        this.direction = direction;
    }

    protected void decode(final ChannelHandlerContext channel, final ByteBuf buf, final List<Object> objects) throws Exception {
        if (Natsuki.getInstance().getConfig().UTILS.debug && Natsuki.getInstance().getConfig().UTILS.packetDebugger)
            System.out.println(channel.channel().remoteAddress() + " -> " + Arrays.toString(buf.array()) + " | [bytes: " + buf.readableBytes() + ", packet: " + packetState + ", handshake: " + handshakeIntent + "]");

        try {
            if (!buf.isReadable()) {
                throw new NatsukiException("BIG CHUNGUS WILL FIND YOU!");
            }

            if (packetState < 4) {
                if (packetState == 0 || (packetState == 1 && handshakeIntent == 2)) {
                    final PacketDataSerializer serializer = new PacketDataSerializer(buf.copy());
                    if (packetState == 0) {
                        handshakeIntent = PacketUtil.checkHandshake(serializer);
                    } else {
                        PacketUtil.checkLogin(serializer);
                    }
                } else if (handshakeIntent == 1 && packetState == 1) {
                    if (buf.readableBytes() >= 3) {
                        throw new NatsukiException("Yo boi wa ta fak ju du?");
                    }
                }
                packetState++;
            }

            final PacketDataSerializer serializer = new PacketDataSerializer(buf.copy());
            final int packetId = serializer.e();
            final Packet<?> packet = channel.channel().attr(NetworkManager.c).get().a(this.direction, packetId);
            if (packet == null) {
                buf.skipBytes(buf.readableBytes());
                serializer.skipBytes(serializer.readableBytes());
            } else {
                packet.a(serializer);
                if (serializer.isReadable()) {
                    throw new NatsukiException("Packet still readable?");
                }

                objects.add(packet);
                ++packetState;
            }
        } catch (final IndexOutOfBoundsException | NatsukiException e) {
            channel.pipeline().remove(this);
            throw new NatsukiException(e);
        }
    }
}
