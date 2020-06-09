package pw.narumi.holder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import net.minecraft.server.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import pw.narumi.Natsuki;
import pw.narumi.common.PacketUtil;
import pw.narumi.exception.NatsukiException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class NatsukiPacketDecoder extends ByteToMessageDecoder {
    /*
    Zamiennik jebanego syfu ze spigota
     */

    private static final Logger logger = LogManager.getLogger();
    private static final Marker marker = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.b);
    private final EnumProtocolDirection direction;
    private int packetState = 0;
    private int handshakeIntent = 0;

    public NatsukiPacketDecoder(final EnumProtocolDirection direction) { this.direction = direction; }

    protected void decode(final ChannelHandlerContext channel, final ByteBuf buf, final List<Object> objects) throws Exception {
        if (Natsuki.getInstance().getConfig().UTILS.debug && Natsuki.getInstance().getConfig().UTILS.packetDebugger)
            System.out.println(channel.channel().remoteAddress() + " -> " + Arrays.toString(buf.array()) + " | [bytes: " + buf.readableBytes() + ", packet: " + packetState + ", handshake: " + handshakeIntent + "]");

        try {
            if (buf.readableBytes() <= 0) {
                channel.pipeline().remove(this);
                throw new NatsukiException("Null readable bytes received");
            }

            if (packetState >= 4)
                handshakeIntent = 0;

            if (packetState == 0 || (packetState == 1 && handshakeIntent == 2)) {
                try {
                    final PacketDataSerializer buffer = new PacketDataSerializer(buf.copy());
                    if (packetState == 0) {
                        handshakeIntent = PacketUtil.checkHandshake(buffer);
                    }

                    if (packetState == 1 && handshakeIntent == 2) {
                        PacketUtil.checkLogin(buffer);
                    }
                }catch (final NatsukiException e) {
                    channel.pipeline().remove(this);
                    e.printStackTrace();
                    throw new NatsukiException("Decoder exception");
                }
            }

            final PacketDataSerializer serializer = new PacketDataSerializer(buf);
            if ((handshakeIntent == 2 && packetState < 3) && serializer.readableBytes() <= 0) {
                channel.pipeline().remove(this);
                throw new NatsukiException("Empty packet");
            }

            if ((handshakeIntent == 1 && packetState == 1) && (serializer.readableBytes() >= 2)) {
                channel.pipeline().remove(this);
                throw new NatsukiException("Empty Packet");
            }

            if ((handshakeIntent == 1 && packetState == 2 || packetState == 0) && serializer.readableBytes() <= 0) {
                channel.pipeline().remove(this);
                throw new NatsukiException("Invalid packet data");
            }

            final int packetId = serializer.readVarInt();
            if (packetState == 2 && packetId != 1 && handshakeIntent == 1) {
                channel.pipeline().remove(this);
                throw new NatsukiException("Invalid packet");
            }

            final Packet<?> packet = channel.channel().attr(NetworkManager.c).get().a(this.direction, packetId);
            if (packet == null) {
                channel.pipeline().remove(this);
                throw new NatsukiException("Null packtet");
            }

            packet.a(serializer);
            if (serializer.isReadable()) {
                channel.pipeline().remove(this);
                throw new NatsukiException("Packet is too big");
            }

            objects.add(packet);
            ++packetState;
        }catch (final IndexOutOfBoundsException e) {
            channel.pipeline().remove(this);
            e.printStackTrace();
            throw new NatsukiException("Invalid data");
        }
    }
}
