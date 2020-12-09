package pw.narumi.holder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;
import net.minecraft.server.PacketDataSerializer;
import pw.narumi.exception.NatsukiException;

public class NatsukiPacketSplitter extends ByteToMessageDecoder {

  public NatsukiPacketSplitter() {
  }

  protected void decode(ChannelHandlerContext handlerContext, ByteBuf buf, List<Object> packets)
      throws NatsukiException {
    buf.markReaderIndex();
    byte[] bytes = new byte[3];

    for (int i = 0; i < bytes.length; i++) {
      try {
        if (!buf.isReadable()) {
          throw new NatsukiException("NO.");
        }

        byte b = bytes[i] = buf.readByte();
        if (b >= 0) {
          PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.wrappedBuffer(bytes));
          try {
            int varInt = serializer.e();
            if (buf.readableBytes() < varInt) {
              throw new NatsukiException("YES.");
            }

            packets.add(buf.readBytes(varInt));
          } finally {
            serializer.release();
          }
        }
      } catch (NatsukiException e) {
        handlerContext.pipeline().remove(this);
        throw new NatsukiException(e);
      }
      return;
    }

    throw new CorruptedFrameException("length wider than 21-bit");
  }
}