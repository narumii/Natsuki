package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pw.narumi.Natsuki;
import pw.narumi.holder.NatsukiPacketDecoder;
import pw.narumi.holder.NatsukiPacketSplitter;
import pw.narumi.holder.PacketDecoder;

public class ServerConnection {

  private static final Logger e = LogManager.getLogger();
  public static final LazyInitVar<NioEventLoopGroup> a = new LazyInitVar() {
    protected NioEventLoopGroup a() {
      return new NioEventLoopGroup(0,
          (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true)
              .build());
    }

    protected Object init() {
      return this.a();
    }
  };
  public static final LazyInitVar<EpollEventLoopGroup> b = new LazyInitVar() {
    protected EpollEventLoopGroup a() {
      return new EpollEventLoopGroup(0,
          (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true)
              .build());
    }

    protected Object init() {
      return this.a();
    }
  };
  public static final LazyInitVar<LocalEventLoopGroup> c = new LazyInitVar() {
    protected LocalEventLoopGroup a() {
      return new LocalEventLoopGroup(0,
          (new ThreadFactoryBuilder()).setNameFormat("Netty Local Server IO #%d").setDaemon(true)
              .build());
    }

    protected Object init() {
      return this.a();
    }
  };
  private final MinecraftServer f;
  public volatile boolean d;
  private final List<ChannelFuture> g = Collections.synchronizedList(Lists.newArrayList());
  private final List<NetworkManager> h = Collections.synchronizedList(Lists.newArrayList());

  public ServerConnection(MinecraftServer minecraftserver) {
    this.f = minecraftserver;
    this.d = true;
  }

  public void a(final InetAddress address, final int i) throws IOException {
    synchronized (this.g) {
      final Class<? extends ServerChannel> clazz;
      final LazyInitVar<?> lazyInitVar;

      if (Epoll.isAvailable() && this.f.ai()) {
        clazz = EpollServerSocketChannel.class;
        lazyInitVar = ServerConnection.b;
        ServerConnection.e.info("Using epoll channel type");
      } else {
        clazz = NioServerSocketChannel.class;
        lazyInitVar = ServerConnection.a;
        ServerConnection.e.info("Using default channel type");
      }

      this.g.add(new ServerBootstrap().channel(clazz).childHandler(new ChannelInitializer() {

        @Override
        protected void initChannel(final Channel channel) {
          try {
            channel.config().setOption(ChannelOption.TCP_NODELAY, true);
          } catch (final ChannelException channelexception) {
          }

          channel.pipeline().addLast("timeout",
              new ReadTimeoutHandler(30))
              .addLast("legacy_query", new LegacyPingHandler(ServerConnection.this))
              .addLast("splitter", new NatsukiPacketSplitter())
              .addLast("decoder", (Natsuki.getInstance().getConfig().PACKET.customDecoder
                  ? new NatsukiPacketDecoder(EnumProtocolDirection.SERVERBOUND)
                  : new PacketDecoder(EnumProtocolDirection.SERVERBOUND)))
              .addLast("prepender", new PacketPrepender())
              .addLast("encoder", new PacketEncoder(EnumProtocolDirection.CLIENTBOUND));

          final NetworkManager networkManager = new NetworkManager(
              EnumProtocolDirection.SERVERBOUND);
          ServerConnection.this.h.add(networkManager);
          channel.pipeline().addLast("packet_handler", networkManager);
          networkManager.a(new HandshakeListener(ServerConnection.this.f, networkManager));
        }
      }).group((EventLoopGroup) lazyInitVar.c()).localAddress(address, i).bind()
          .syncUninterruptibly());
    }
  }

  public void b() {
    this.d = false;
    Iterator iterator = this.g.iterator();

    while (iterator.hasNext()) {
      ChannelFuture channelfuture = (ChannelFuture) iterator.next();

      try {
        channelfuture.channel().close().sync();
      } catch (InterruptedException interruptedexception) {
        ServerConnection.e.error("Interrupted whilst closing channel");
      }
    }

  }

  public void c() {
    List list = this.h;

    synchronized (this.h) {
      // Spigot Start
      // This prevents players from 'gaming' the server, and strategically relogging to increase their position in the tick order
      if (org.spigotmc.SpigotConfig.playerShuffle > 0
          && MinecraftServer.currentTick % org.spigotmc.SpigotConfig.playerShuffle == 0) {
        Collections.shuffle(this.h);
      }
      // Spigot End
      Iterator iterator = this.h.iterator();

      while (iterator.hasNext()) {
        final NetworkManager networkmanager = (NetworkManager) iterator.next();

        if (!networkmanager.h()) {
          if (!networkmanager.g()) {
            // Spigot Start
            // Fix a race condition where a NetworkManager could be unregistered just before connection.
            if (networkmanager.preparing) {
              continue;
            }
            // Spigot End
            iterator.remove();
            networkmanager.l();
          } else {
            try {
              networkmanager.a();
            } catch (Exception exception) {
              if (networkmanager.c()) {
                CrashReport crashreport = CrashReport.a(exception, "Ticking memory connection");
                CrashReportSystemDetails crashreportsystemdetails = crashreport
                    .a("Ticking connection");

                crashreportsystemdetails.a("Connection", new Callable() {
                  public String a() throws Exception {
                    return networkmanager.toString();
                  }

                  public Object call() throws Exception {
                    return this.a();
                  }
                });
                throw new ReportedException(crashreport);
              }

              ServerConnection.e
                  .warn("Failed to handle packet for " + networkmanager.getSocketAddress(),
                      exception);
              final ChatComponentText chatcomponenttext = new ChatComponentText(
                  "Internal server error");

              networkmanager.a(new PacketPlayOutKickDisconnect(chatcomponenttext),
                  new GenericFutureListener() {
                    public void operationComplete(Future future) throws Exception {
                      networkmanager.close(chatcomponenttext);
                    }
                  });
              networkmanager.k();
            }
          }
        }
      }

    }
  }

  public MinecraftServer d() {
    return this.f;
  }
}
