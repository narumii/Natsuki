package pw.narumi.recode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Config {

    @SerializedName(value = "EnginePrefix")
    private String prefix = "\u00267(*) \u0026f(o) \u0026dNatsuki \u0026f(o) \u00267(*)";

    @SerializedName(value = "Api")
    private final Api api = new Api();

    @SerializedName(value = "Connection")
    private final Connection connection = new Connection();

    @SerializedName(value = "PacketSettings")
    private final PacketSettings packetSettings = new PacketSettings();

    @SerializedName(value = "Utils")
    private final Utils utils = new Utils();

    @SerializedName(value = "Messages")
    private final HashMap<String, String> messages = new HashMap<>();

    public class Api {
        @SerializedName(value = "GeoLite2FilePath")
        private String getLiteFile = "Natsuki/GeoLite2-Country.mmdb";

        @SerializedName(value = "ProxyCheckApiKey")
        private String proxyCheckKey = "Your Key";
    }

    public class Connection {
        @SerializedName(value = "MaxConnectionsPerAddress")
        private int maxConnections = 2;

        @SerializedName(value = "MaxChannelsPerAddressPerSecond")
        private int channelsPerAddress = 10;

        @SerializedName(value = "CheckPlayerAddress")
        private boolean addressCheck = true;

        @SerializedName(value = "Region")
        private Region region = new Region();

        public class Region {
            @SerializedName(value = "Check")
            private boolean check = true;

            @SerializedName(value = "AllowedRegions")
            private List<String> allowedRegions = Arrays.asList("PL", "DE");
        }
    }


    public class PacketSettings {
        @SerializedName(value = "MaxPacketsPerSecond")
        private int maxPackets = 400;

        @SerializedName(value = "CustomPacketDecoder")
        private boolean customDecoder = true;

        @SerializedName(value = "NbtSettings")
        private NBT nbt = new NBT();

        public class NBT {
            @SerializedName(value = "SkipNbtReading")
            private boolean skipNbt = true;

            @SerializedName(value = "FixSkullExploit")
            private boolean fixSkull = true;

            @SerializedName(value = "MaxNbtSize")
            private int maxNbtSize = 10000;
        }

        @SerializedName(value = "PayloadSettings")
        private CustomPayload payload = new CustomPayload();

        public class CustomPayload {
            @SerializedName(value = "SkipPayloadReading")
            private boolean skipPayload = true;

            @SerializedName(value = "MaxChannelLength")
            private int maxChannelLength = 64;

            @SerializedName(value = "MaxDataLength")
            private int maxDataLength = 1000;

            @SerializedName(value = "BlockChannels")
            private boolean blockChannels = false;

            @SerializedName(value = "BlockedChannels")
            private List<String> blockedChannels = Arrays.asList("FML", "WDL");
        }
    }

    public class Utils {
        @SerializedName(value = "TickPerSecondCommandPermissionCheck")
        private boolean tpsCommandPermission = false;

        @SerializedName(value = "AntiRedStoneLagger")
        private boolean antiRedStone = true;

        @SerializedName(value = "AntiBot")
        private boolean antiBot = true;

        @SerializedName(value = "PacketDebugger")
        private boolean packetDebugger = false;

        @SerializedName(value = "Debug")
        private boolean debug = false;
    }

    {
        messages.put("AntiBotKick", "\u0026cSystem wykryl ze jestes botem\n\u00267Jesli to blad zglos to administracji");
        messages.put("BlockedCrashKick", "\u0026cZostales wyrzucony za probe crashu");
        messages.put("BlockedPayLoadKick", "\u0026cWysylanie tego payloadu zostalo zablokowane");
        messages.put("ProxyOrVpnKick", "\u0026cWykrylismy ze uzywasz VPN lub proxy\n\u00267Jesli to blad zrestartuj router lub zglos to administarcji");
        messages.put("RegionKick", "\u0026cPrzyjmujemy polaczenia tylko z Polski\n\u00267Jesli mieszkasz za granica zglos to administracji");
        messages.put("MaxConnectionsPerIp", "\u0026cPrzekroczyles limit polaczen z 1 ip");
    }
}
