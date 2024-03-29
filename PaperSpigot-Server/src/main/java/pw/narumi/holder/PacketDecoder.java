//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package pw.narumi.holder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;
import net.minecraft.server.EnumProtocolDirection;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import pw.narumi.exception.NatsukiException;

public class PacketDecoder extends ByteToMessageDecoder {

  private static final Logger a = LogManager.getLogger();
  private static final Marker b;
  private final EnumProtocolDirection c;

  public PacketDecoder(EnumProtocolDirection var1) {
    this.c = var1;
  }

  protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3)
      throws IOException, InstantiationException, IllegalAccessException {
    PacketDataSerializer var4 = new PacketDataSerializer(var2);
    int var5 = var4.e();

    Packet var6 = var1.channel().attr(NetworkManager.c).get().a(this.c, var5);
    if (var6 == null) {
      throw new NatsukiException("Bad packet id " + var5);
    } else {
      var6.a(var4);
      if (var4.readableBytes() > 0) {
        throw new NatsukiException(
            "Packet " + var1.channel().attr(NetworkManager.c).get().a() + "/" + var5 + " (" + var6
                .getClass().getSimpleName() + ") was larger than I expected, found " + var4
                .readableBytes() + " bytes extra whilst reading packet " + var5);
      } else {
        var3.add(var6);
        if (a.isDebugEnabled()) {
          a.debug(b, " IN: [{}:{}] {}", var1.channel().attr(NetworkManager.c).get(), var5,
              var6.getClass().getName());
        }
      }
    }
  }

  static {
    b = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.b);
  }
}
