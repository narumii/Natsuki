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
            System.out.println(channel.channel().remoteAddress() + " -> " + Arrays.toString(buf.array()));

        try {

            if (buf.readableBytes() <= 0) { //HEHE
                channel.pipeline().remove(this);
                throw new NatsukiException("Null readable bytes received");
            }

            if (packetState >= 3)
                handshakeIntent = 0;

            //SPRAWDZANIE PAKIEUT HANDSHAKE
            if (packetState == 0) {
                final PacketDataSerializer handshakeBuf = new PacketDataSerializer(buf.duplicate()); //TWORZENIE KOPII ABY NIE INGEROWAC W ORGINALNA DATA PAKIETU
                if (handshakeBuf.readableBytes() > 256) {
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Too big packet");
                }

                final int packetId = handshakeBuf.readVarInt();
                if (handshakeBuf.readableBytes() <= 0 || packetId != 0) { //1 PAKIET TO ZAWSZE HANDSHAKE
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Invalid handshake packet");
                }

                final int protocol = handshakeBuf.readVarInt();
                if (handshakeBuf.readableBytes() <= 0 || protocol != 47) { //PROTOCOL 47 BO TO 1.8.8
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Invalid protocol version");
                }

                final byte[] host = new byte[handshakeBuf.readVarInt()];
                handshakeBuf.readBytes(host);
                if (handshakeBuf.readableBytes() <= 2) { //MAGIA
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Invalid host address");
                }

                final int port = handshakeBuf.readUnsignedShort();
                if (handshakeBuf.readableBytes() <= 0 || port <= 0) { //PORT ZAWSZE WIEKSZY OD 0
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Invalid server port");
                }

                final int state = handshakeBuf.readVarInt();
                if (state != 1 && state != 2) { //STATE ZAWSZE MUSI BYC 1 LUB 2
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Invalid handshake state");
                }

                if (handshakeBuf.readableBytes() > 0) { //NO JAKBY KTOS MUTANTA WYSLAL
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Invalid packet data");
                }

                handshakeIntent = state;
            }

            if (packetState == 1 && handshakeIntent == 2) {
                final PacketDataSerializer handshakeBuf = new PacketDataSerializer(buf.duplicate()); //TWORZENIE KOPII ABY NIE INGEROWAC W ORGINALNA DATA PAKIETU
                if (handshakeBuf.readableBytes() > 64) {
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Too big packet");
                }

                final int id = handshakeBuf.readVarInt();

                if (handshakeBuf.readableBytes() <= 0 || id != 0) { //1 PAKIET TO ZAWSZE HANDSHAKE
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Invalid packet");
                }

                final byte[] host = new byte[handshakeBuf.readVarInt()];
                handshakeBuf.readBytes(host);
                final String string = new String(host);
                if (handshakeBuf.readableBytes() > 0) { //MAGIA
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Still readable?");
                }

                if (string.length() > 16 || string.length() <= 2) {
                    channel.pipeline().remove(this);
                    throw new NatsukiException("Invalid name");
                }
            }

            final PacketDataSerializer serializer = new PacketDataSerializer(buf);
            if (handshakeIntent == 2 && (serializer.readableBytes() <= 0) && (packetState < 3)) { //SPRAWDZANIE DATY
                channel.pipeline().remove(this);
                throw new NatsukiException("Empty packet");
            }

            if (handshakeIntent == 1 && (serializer.readableBytes() <= 0) && (packetState == 0 || packetState == 2)) { //SPRAWDZANIE DATY
                channel.pipeline().remove(this);
                throw new NatsukiException("Empty Packet");
            }

            if (handshakeIntent == 1 && serializer.readableBytes() > 0 && packetState == 1) { //SPRAWDZANIE DATY
                channel.pipeline().remove(this);
                throw new NatsukiException("Too big packet data");
            }

            final Packet<?> packet = channel.channel().attr(NetworkManager.c).get().a(this.direction, serializer.readVarInt()); //POBIERANIE PAKIEUT
            if (packet == null) { //JAK PAKIET JEST NULLEM TO NIECH SPIERDALA
                channel.pipeline().remove(this);
                throw new NatsukiException("Null packtet");
            }

            packet.a(serializer); //CZYTANIE PAKIETU

            if (serializer.readableBytes() > 0) { //JESLI PAKIET MA DATA PO ODCZYTANIU TO JEST ZJEBANY
                channel.pipeline().remove(this);
                throw new NatsukiException("Packet is too big");
            }

            objects.add(packet); //CHUJ WIE

            ++packetState;
        }catch (final IndexOutOfBoundsException e) {
            throw new NatsukiException("Invalid data");
        }
    }
}
