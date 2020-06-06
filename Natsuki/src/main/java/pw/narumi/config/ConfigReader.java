package pw.narumi.config;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class ConfigReader {

    private final Gson gson;
    private final String filePath;
    private JsonObject jsonObject;
    private JsonObject originalJson;

    public JsonObject getJsonObject() {
        return this.jsonObject;
    }

    public ConfigReader() {
        this.filePath = "Natsuki/config.json";
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    //NO TAK SREDNIO BYM POWIEDZIAL
    public void createConfig() throws IOException {
        final JsonObject api = new JsonObject();

        final JsonObject geoLite = new JsonObject();
        geoLite.addProperty("CountryDbFilePath", "Natsuki/GeoLite2-Country.mmdb");

        final JsonObject proxyCheck = new JsonObject();
        proxyCheck.addProperty("ApiKey", "Your Key");

        api.add("GeoLite2", geoLite);
        api.add("ProxyCheckIO", proxyCheck);

        final JsonObject region = new JsonObject();
        region.addProperty("Check", true);

        final JsonArray regions = new JsonArray();
        regions.add(new JsonPrimitive("PL"));
        regions.add(new JsonPrimitive("DE"));
        region.add("AllowedRegions", regions);

        final JsonObject connection = new JsonObject();
        connection.addProperty("MaxConnectionPerAddress", 1);
        connection.addProperty("CheckPlayerAddress", true);
        connection.add("Region", region);

        final JsonObject payload = new JsonObject();
        payload.addProperty("MaxChannelLength", 20);
        payload.addProperty("MaxDataLength", 500);

        final JsonObject packetData = new JsonObject();
        packetData.addProperty("MaxNbtDataSize", 7000);
        packetData.addProperty("FixSkullNbt", true);
        packetData.addProperty("SkipNBT", false);
        packetData.add("CustomPayload", payload);

        final JsonObject payloadTwo = new JsonObject();
        payloadTwo.addProperty("BlockChannels", false);

        final JsonArray channels = new JsonArray();
        channels.add(new JsonPrimitive("WDL"));
        channels.add(new JsonPrimitive("FML"));
        payloadTwo.add("BlockedChannels", channels);

        final JsonObject packetSettings = new JsonObject();
        packetSettings.addProperty("CustomPacketDecoder", true);
        packetSettings.addProperty("ReadCustomPayload", true);
        packetSettings.addProperty("MaxPacketsPerSecond", 300);
        packetSettings.add("CustomPayload", payloadTwo);

        final JsonObject utils = new JsonObject();
        utils.addProperty("TickPerSecondCommandPermissionCheck", false);
        utils.addProperty("AntiRedStone", true);
        utils.addProperty("AntiBotCheck", true);
        utils.addProperty("McProtocolLibCheck", true);
        utils.addProperty("PacketDebugger", false);
        utils.addProperty("Debug", false);

        final JsonObject fuk = new JsonObject();
        fuk.addProperty("MaxChannelsPerSecondPerAddress", 7);

        final JsonObject messages = new JsonObject();
        messages.addProperty("McProtocolLibKick", "&c&lDobra wypierdalaj jebany ulancu");
        messages.addProperty("AntiBotKick", "&cSystem wykryl ze jestes botem\n&7Jesli to blad zglos to administracji");
        messages.addProperty("BlockedCrashKick", "&cZostales wyrzucony za probe crashu");
        messages.addProperty("BlockedPayLoadKick", "&cWysylanie tego payloadu zostalo zablokowane");
        messages.addProperty("ProxyOrVpnKick", "&cWykrylismy ze uzywasz VPN lub proxy\n&7Jesli to blad zrestartuj router lub zglos to administarcji");
        messages.addProperty("RegionKick", "&cPrzyjmujemy polaczenia tylko z Polski\n&7Jesli mieszkasz za granica zglos to administracji");
        messages.addProperty("MaxConnectionsPerIp", "&cPrzekroczyles limit polaczen z 1 ip");
        
        final JsonObject natsuki = new JsonObject();
        natsuki.addProperty("Prefix", "&7(*) &f(o) &dNatsuki &f(o) &7(*)");
        natsuki.add("API", api);
        natsuki.add("Connection", connection);
        natsuki.add("PacketData", packetData);
        natsuki.add("PacketSettings", packetSettings);
        natsuki.add("Utils", utils);
        natsuki.add("DoNotTouchIfYouAreDoNotKnowWhatAreYouDoing", fuk);
        natsuki.add("Messages", messages);

        originalJson = natsuki;

        try (final Writer writer = new FileWriter(filePath)) {
            gson.toJson(natsuki, writer);
        }
    }

    public void readConfig() throws FileNotFoundException {
        jsonObject = gson.fromJson(new JsonReader(new FileReader(filePath)), JsonObject.class);
    }

    public void saveConfig() throws IOException {
        final JsonObject object = gson.fromJson(new JsonReader(new FileReader(filePath)), JsonObject.class);
        try (final Writer writer = new FileWriter(filePath)) {
            gson.toJson(object, writer);
        }
    }

    public JsonObject getOriginalJson() {
        return this.originalJson;
    }
}
