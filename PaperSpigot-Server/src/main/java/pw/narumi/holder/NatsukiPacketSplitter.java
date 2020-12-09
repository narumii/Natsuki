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

  private int packet;

  protected void decode(ChannelHandlerContext handlerContext, ByteBuf buf, List<Object> objects) throws Exception {
    buf.markReaderIndex();
    byte[] bytes = new byte[3];

    if (++packet < 3 && (buf.readableBytes() > 300 || buf.readableBytes() < 2)) {
      throw new NatsukiException("BIG CHUNGUS WILL FIND YOU");
    }

    for(int i = 0; i < bytes.length; ++i) {
      try {
        if (!buf.isReadable()) {
          buf.resetReaderIndex();
          return;
        }

        bytes[i] = buf.readByte();
        if (bytes[i] >= 0) {
          PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.wrappedBuffer(bytes));
          try {
            int size = serializer.e();
            if (buf.readableBytes() < size) {
              buf.resetReaderIndex();
              return;
            }

            objects.add(buf.readBytes(size));
          } finally {
            serializer.release();
          }
          return;
        }
      }catch (final NatsukiException e) {
        handlerContext.pipeline().remove(this);
        throw new NatsukiException(e);
      }
    }

    throw new CorruptedFrameException("length wider than 21-bit");
  }
}