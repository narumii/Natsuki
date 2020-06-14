package pw.narumi.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Config {

    @SerializedName(value = "EnginePrefix")
    public String PREFIX = "\u00267(*) \u0026f(o) \u0026dNatsuki \u0026f(o) \u00267(*)";

    @SerializedName(value = "Api")
    public final Api API = new Api();

    @SerializedName(value = "Connection")
    public final Connection CONNECTION = new Connection();

    @SerializedName(value = "PacketSettings")
    public final PacketSettings PACKET = new PacketSettings();

    @SerializedName(value = "Utils")
    public final Utils UTILS = new Utils();

    @SerializedName(value = "Messages")
    public final HashMap<String, String> MESSAGES = new HashMap<String, String>() {{
        put("AntiBotKick", "\u0026cSystem wykryl ze jestes botem\n\u00267Jesli to blad zglos to administracji");
        put("BlockedCrashKick", "\u0026cZostales wyrzucony za probe crashu");
        put("BlockedPayLoadKick", "\u0026cWysylanie tego payloadu zostalo zablokowane");
        put("ProxyOrVpnKick", "\u0026cWykrylismy ze uzywasz VPN lub proxy\n\u00267Jesli to blad zrestartuj router lub zglos to administarcji");
        put("RegionKick", "\u0026cPrzyjmujemy polaczenia tylko z Polski\n\u00267Jesli mieszkasz za granica zglos to administracji");
        put("MaxConnectionsPerIp", "\u0026cPrzekroczyles limit polaczen z 1 ip");
    }};

    public class Api {
        @SerializedName(value = "GeoLite2FilePath")
        public String getGeoLiteFile = "Natsuki/GeoLite2-Country.mmdb";

        @SerializedName(value = "ProxyCheckApiKey")
        public String proxyCheckKey = "Your Key";
    }

    public class Connection {
        @SerializedName(value = "MaxConnectionsPerAddress")
        public int maxConnections = 2;

        @SerializedName(value = "MaxChannelsPerAddressPerSecond")
        public int channelsPerAddress = 10;

        @SerializedName(value = "CheckPlayerAddress")
        public boolean addressCheck = true;

        @SerializedName(value = "Region")
        public final Region REGION = new Region();

        public class Region {
            @SerializedName(value = "Check")
            public boolean check = true;

            @SerializedName(value = "AllowedRegions")
            public final List<String> allowedRegions = Arrays.asList("PL", "DE");
        }
    }


    public class PacketSettings {
        @SerializedName(value = "MaxPacketsPerSecond")
        public int maxPackets = 400;

        @SerializedName(value = "CustomPacketDecoder")
        public boolean customDecoder = true;

        @SerializedName(value = "NbtSettings")
        public final NBT NBT = new NBT();

        public class NBT {
            @SerializedName(value = "SkipNbtReading")
            public boolean skipNbt = true;

            @SerializedName(value = "FixSkullExploit")
            public boolean fixSkull = true;

            @SerializedName(value = "MaxNbtSize")
            public int maxNbtSize = 10000;
        }

        @SerializedName(value = "PayloadSettings")
        public final CustomPayload PAYLOAD = new CustomPayload();

        public class CustomPayload {
            @SerializedName(value = "SkipPayloadReading")
            public boolean skipPayload = true;

            @SerializedName(value = "MaxChannelLength")
            public int maxChannelLength = 64;

            @SerializedName(value = "MaxDataLength")
            public int maxDataLength = 1000;

            @SerializedName(value = "BlockChannels")
            public boolean blockChannels;

            @SerializedName(value = "BlockedChannels")
            public final List<String> blockedChannels = Arrays.asList("FML", "WDL");
        }
    }

    public class Utils {
        @SerializedName(value = "TickPerSecondCommandPermissionCheck")
        public boolean tpsCommandPermission;

        @SerializedName(value = "AntiRedStoneLagger")
        public boolean antiRedStone = true;

        @SerializedName(value = "AntiBot")
        public boolean antiBot = true;

        @SerializedName(value = "PacketDebugger")
        public boolean packetDebugger;

        @SerializedName(value = "Debug")
        public boolean debug;
    }

    @SerializedName(value = "PacketLimiter")
    public final HashMap<String, Integer> PACKETS = new HashMap<String, Integer>() {{
        put("Name of packet / max packet per second (-1 = disable)", -1);
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
