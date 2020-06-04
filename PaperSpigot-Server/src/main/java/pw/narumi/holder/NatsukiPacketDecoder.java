package pw.narumi.holder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import net.minecraft.server.EnumProtocolDirection;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import pw.narumi.Natsuki;
import pw.narumi.exception.NatsukiException;

import java.io.IOException;
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

    protected void decode(ChannelHandlerContext channel, ByteBuf buf, List<Object> objects) throws Exception {
        if (buf.readableBytes() <= 0) { //HEHE
            channel.pipeline().remove(this);
            throw new NatsukiException("Null readable bytes received");
        }

        //SPRAWDZANIE PAKIEUT HANDSHAKE
        if (packetState == 0) {
            final ByteBuf handshakeBuf = buf.duplicate(); //TWORZENIE KOPII ABY NIE INGEROWAC W ORGINALNA DATA PAKIETU

            if (handshakeBuf.readableBytes() > 256) {
                channel.pipeline().remove(this);
                throw new NatsukiException("Too big packet");
            }

            final int packetId = readVarInt(handshakeBuf);
            if (handshakeBuf.readableBytes() <= 0 || packetId != 0) { //1 PAKIET TO ZAWSZE HANDSHAKE
                channel.pipeline().remove(this);
                throw new NatsukiException("Invalid handshake packet");
            }

            final int protocol = readVarInt(handshakeBuf);
            if (handshakeBuf.readableBytes() <= 0 || protocol != 47) { //PROTOCOL 47 BO TO 1.8.8
                channel.pipeline().remove(this);
                throw new NatsukiException("Invalid protocol version");
            }

            final byte[] host = new byte[readVarInt(handshakeBuf)];
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

            final int state = readVarInt(handshakeBuf);
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

        if (packetState > 3)
            handshakeIntent = 0;

        final PacketDataSerializer serializer = new PacketDataSerializer(buf); //PIERDOLONY SYF
        final Packet<?> packet = channel.channel().attr(NetworkManager.c).get().a(this.direction, serializer.readVarInt()); //POBIERANIE PAKIEUT

        if (handshakeIntent == 2 && serializer.readableBytes() <= 0 && (packetState < 3)) { //SPRAWDZANIE DATY
            channel.pipeline().remove(this);
            throw new NatsukiException("Empty packet");
        }

        if (handshakeIntent == 1 && serializer.readableBytes() <= 0 && (packetState == 0 || packetState == 2)) { //SPRAWDZANIE DATY
            channel.pipeline().remove(this);
            throw new NatsukiException("Empty packet");
        }

        if (handshakeIntent == 1 && serializer.readableBytes() > 0 && packetState == 1) { //SPRAWDZANIE DATY
            channel.pipeline().remove(this);
            throw new NatsukiException("Too big packet data");
        }

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
    }

    protected int readVarInt(final ByteBuf buf) {
        int out = 0;
        int bytes = 0;
        byte in;

        do {
            if (buf.readableBytes() <= 0)
                throw new NatsukiException("Null readable bytes");

            in = buf.readByte();
            out |= (in & 127) << bytes++ * 7;
            if (bytes > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((in & 128) == 128);

        return out;
    }
}
