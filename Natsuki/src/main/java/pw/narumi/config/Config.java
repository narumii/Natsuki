package pw.narumi.config;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Config {

    @SerializedName(value = "Engine Prefix")
    public String PREFIX = "\u00267(*) \u0026f(o) \u0026dNatsuki \u0026f(o) \u00267(*)";

    @SerializedName(value = "Api")
    public final Api API = new Api();

    @SerializedName(value = "Connection")
    public final Connection CONNECTION = new Connection();

    @SerializedName(value = "Packet Settings")
    public final PacketSettings PACKET = new PacketSettings();

    @SerializedName(value = "AntiBot")
    public final AntiBot ANTIBOT = new AntiBot();

    @SerializedName(value = "Utils")
    public final Utils UTILS = new Utils();

    @SerializedName(value = "Messages")
    public final HashMap<String, String> MESSAGES = new HashMap<String, String>() {{
        put("AntiBotKick", "\u0026cSystem wykryl ze jestes botem\n\u00267Jesli to blad zglos to administracji");
        put("BlockedCrashKick", "\u0026cZostales wyrzucony za probe crashu");
        put("BlockedPayLoadKick", "\u0026cWysylanie tego payloadu zostalo zablokowane");
        put("ProxyOrVpnKick", "\u0026cWykrylismy ze uzywasz VPN lub proxy\n\u00267Jesli to blad zrestartuj router lub zglos to administarcji");
        //put("RegionKick", "\u0026cPrzyjmujemy polaczenia tylko z Polski\n\u00267Jesli mieszkasz za granica zglos to administracji");
        //put("MaxConnectionsPerIp", "\u0026cPrzekroczyles limit polaczen z 1 ip");
        put("PingKick", "\u0026cYou must first ping the server, after that join again.");
        put("DoubleJoin", "\u0026cJoin again to the server.");
    }};

    public class Api {
        //TODO: FIX
        //@SerializedName(value = "GeoLite2 File Path")
        //public String getGeoLiteFile = "Natsuki/GeoLite2-Country.mmdb";

        @SerializedName(value = "Proxy Check Api Key")
        public String proxyCheckKey = "Your Key";
    }

    public class Connection {
        //TODO: FIX
        //@SerializedName(value = "Max Connections Per Address")
        //public int maxConnections = 2;

        @SerializedName(value = "Check Player Address")
        public boolean addressCheck = true;

        //TODO: FIX
        //@SerializedName(value = "Region")
        //public final Region REGION = new Region();

        //public class Region {
        //    @SerializedName(value = "Check")
        //    public boolean check = false;

        //    @SerializedName(value = "Allowed Regions")
        //    public final List<String> allowedRegions = Arrays.asList("PL", "DE");
        //}
    }

    public class PacketSettings {

        @SerializedName(value = "Custom PacketDecoder")
        public boolean customDecoder = true;

        @SerializedName(value = "NBT Settings")
        public final NBT NBT = new NBT();

        public class NBT {
            @SerializedName(value = "Skip NBT Reading")
            public boolean skipNbt = true;

            @SerializedName(value = "Fix Skull Exploit")
            public boolean fixSkull = true;

            @SerializedName(value = "Max NBT Size")
            public int maxNbtSize = 10000;
        }

        @SerializedName(value = "Payload Settings")
        public final CustomPayload PAYLOAD = new CustomPayload();

        public class CustomPayload {
            @SerializedName(value = "Skip Payload Reading")
            public boolean skipPayload = true;

            @SerializedName(value = "Max Channel Length")
            public int maxChannelLength = 64;

            @SerializedName(value = "Max Data Length")
            public int maxDataLength = 1000;

            @SerializedName(value = "Block Channels")
            public boolean blockChannels;

            @SerializedName(value = "Blocked Channels")
            public final List<String> blockedChannels = Arrays.asList("FML", "WDL");
        }
    }

    public class AntiBot {
        @SerializedName(value = "Ping Check")
        public boolean pingCheck = true;

        @SerializedName(value = "Ping CPS Trigger")
        public int pingCheckTrigger = 15;

        @SerializedName(value = "Double Join")
        public boolean doubleJoin = true;

        @SerializedName(value = "Double Join CPS Trigger")
        public int doubleJoinTrigger = 15;
    }

    public class Utils {
        @SerializedName(value = "TickPerSecondCommand Permission Check")
        public boolean tpsCommandPermission;

        @SerializedName(value = "AntiRedStone Lagger")
        public boolean antiRedStone = true;

        @SerializedName(value = "Packet Debugger")
        public boolean packetDebugger;

        @SerializedName(value = "Debug")
        public boolean debug;
    }

    @SerializedName(value = "Packet Limiter")
    public final HashMap<String, Integer> PACKETS = new HashMap<String, Integer>() {{
        put("PacketPlayInFlying", 200);
        put("PacketPlayInBlockDig", 100);
        put("PacketPlayInBlockPlace", 100);
        put("PacketPlayInArmAnimation", 300);
        put("PacketPlayInEntityAction", 300);
        put("PacketPlayInUseEntity", 300);
        put("PacketPlayInCloseWindow", 100);
        put("PacketPlayInWindowClick", 70);
        put("PacketPlayInSetCreativeSlot", 120);
        put("PacketPlayInTransaction", 100);
        put("PacketPlayInUpdateSign", 30);
        put("PacketPlayInCustomPayload", 100);
    }};
}
