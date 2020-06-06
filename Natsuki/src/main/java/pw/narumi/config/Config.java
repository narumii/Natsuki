package pw.narumi.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pw.narumi.Natsuki;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private String prefix = "§7• §f● §dなつき §f● §7•";

    //API
    private String proxyCheckApiKey;

    private String geoLiteFile;

    //CONNECTION
    private int maxConnectionPerAddress;
    private boolean checkPlayerAddress;
    private boolean checkPlayerRegion;
    private final List<String> allowedRegions = new ArrayList<>();

    //PACKET DATA
    private int maxNbtDataLength;
    private boolean fixSkullNbt;
    private boolean skipNBT;

    //PAYLOAD
    private boolean readCustomPayload;
    private boolean blockChannels;
    private final List<String> blockedChannels = new ArrayList<>();;
    private int maxPyaLoadChannelLength;
    private int maxPayLoadDataLength;

    //PACKETS
    private int maxPacketsPerSecond;

    //UTILS
    private boolean checkTpsPermission;
    private boolean antiRedStone = false;
    private boolean antiBot;
    private boolean mcProtocolLibFix;
    private boolean packetDebugger;
    private boolean debug;

    private boolean customDecoder;

    //FUCKING NOT TOUCH XD
    private int maxChannelsPerAddress;

    private final Map<String, String> messages = new HashMap<>();

    public Config() {}

    //NO TAK SREDNIO BYM POWIEDZIAL
    public void init(final JsonObject json) {
        try {
            proxyCheckApiKey = json.getAsJsonObject("API").getAsJsonObject("ProxyCheckIO").get("ApiKey").getAsString();

            if (proxyCheckApiKey.equals("Your Key") || proxyCheckApiKey.equals("none")) {
                System.out.println("ApiKey od ProxyCheck nie zostal ustawiony. Uzywam zamiennego API.");
            }else {
                System.out.println("Mozesz uzyc zamiennego API, ustawiajac ProxyCheck ApiKey na: none");
            }

            geoLiteFile = json.getAsJsonObject("API").getAsJsonObject("GeoLite2").get("CountryDbFilePath").getAsString();

            final String p = json.get("Prefix").getAsString();

            if (p.contains("Â") || p.contains("€")) {
                prefix = "§7• §f● §dなつき §f● §7•";
            }else {
                if (!p.equals("none") && !p.equals("") && !p.equals(" ")) {
                    prefix = fixString(p);
                }
            }

            //CONNECTION
            maxConnectionPerAddress = json.getAsJsonObject("Connection").get("MaxConnectionPerAddress").getAsInt();
            checkPlayerAddress = json.getAsJsonObject("Connection").get("CheckPlayerAddress").getAsBoolean();
            checkPlayerRegion = json.getAsJsonObject("Connection").getAsJsonObject("Region").get("Check").getAsBoolean();
            for (final JsonElement element : json.getAsJsonObject("Connection")
                    .getAsJsonObject("Region").getAsJsonArray("AllowedRegions")) {
                allowedRegions.add(element.getAsString());
            }
            //PACKET DATA
            maxNbtDataLength = json.getAsJsonObject("PacketData").get("MaxNbtDataSize").getAsInt();
            fixSkullNbt = json.getAsJsonObject("PacketData").get("FixSkullNbt").getAsBoolean();
            skipNBT = json.getAsJsonObject("PacketData").get("SkipNBT").getAsBoolean();

            //PAYLOAD
            readCustomPayload = json.getAsJsonObject("PacketSettings").get("ReadCustomPayload").getAsBoolean();
            blockChannels = json.getAsJsonObject("PacketSettings").getAsJsonObject("CustomPayload").get("BlockChannels").getAsBoolean();
            for (final JsonElement element : json.getAsJsonObject("PacketSettings")
                    .getAsJsonObject("CustomPayload").getAsJsonArray("BlockedChannels")) {
                blockedChannels.add(element.getAsString());
            }

            maxPyaLoadChannelLength = json.getAsJsonObject("PacketData").getAsJsonObject("CustomPayload").get("MaxChannelLength").getAsInt();
            maxPayLoadDataLength = json.getAsJsonObject("PacketData").getAsJsonObject("CustomPayload").get("MaxDataLength").getAsInt();

            //PACKETS
            customDecoder = json.getAsJsonObject("PacketSettings").get("CustomPacketDecoder").getAsBoolean();;
            maxPacketsPerSecond = json.getAsJsonObject("PacketSettings").get("MaxPacketsPerSecond").getAsInt();

            //UTILS
            checkTpsPermission = json.getAsJsonObject("Utils").get("TickPerSecondCommandPermissionCheck").getAsBoolean();
            antiRedStone = json.getAsJsonObject("Utils").get("AntiRedStone").getAsBoolean();
            antiBot = json.getAsJsonObject("Utils").get("AntiBotCheck").getAsBoolean();
            mcProtocolLibFix = json.getAsJsonObject("Utils").get("McProtocolLibCheck").getAsBoolean();

            packetDebugger = json.getAsJsonObject("Utils").get("PacketDebugger").getAsBoolean();
            debug = json.getAsJsonObject("Utils").get("Debug").getAsBoolean();

            //FUCKING NOT TOUCH XD
            maxChannelsPerAddress = json.getAsJsonObject("DoNotTouchIfYouAreDoNotKnowWhatAreYouDoing").get("MaxChannelsPerSecondPerAddress").getAsInt();

            //MESSAGES
            messages.put("McProtocolLibKick", fixString(json.getAsJsonObject("Messages").get("McProtocolLibKick").getAsString()));
            messages.put("AntiBotKick", fixString(json.getAsJsonObject("Messages").get("AntiBotKick").getAsString()));
            messages.put("BlockedCrashKick", fixString(json.getAsJsonObject("Messages").get("BlockedCrashKick").getAsString()));
            messages.put("BlockedPayLoadKick", fixString(json.getAsJsonObject("Messages").get("BlockedPayLoadKick").getAsString()));
            messages.put("ProxyOrVpnKick", fixString(json.getAsJsonObject("Messages").get("ProxyOrVpnKick").getAsString()));
            messages.put("RegionKick", fixString(json.getAsJsonObject("Messages").get("RegionKick").getAsString()));
            messages.put("MaxConnectionsPerIp", fixString(json.getAsJsonObject("Messages").get("MaxConnectionsPerIp").getAsString()));
        }catch (final Exception e) {
            System.out.println("Uzywasz starej wersji konfigu, zastepuje go nowym");
            try {
                Natsuki.getInstance().getConfigReader().createConfig();
            } catch (final IOException ioException) {
                System.out.println("Nie mozna zrobic nowego konfigu");
            }
            System.exit(0);
        }
    }

    //HEH
    private String fixString(final String string) {
        return string
                .replace("&", "§")
                .replace("(o)", "●")
                .replace("(*)", "•");
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public String getProxyCheckApiKey() {
        return this.proxyCheckApiKey;
    }

    public void setProxyCheckApiKey(final String proxyCheckApiKey) {
        this.proxyCheckApiKey = proxyCheckApiKey;
    }

    public String getGeoLiteFile() {
        return this.geoLiteFile;
    }

    public void setGeoLiteFile(final String geoLiteFile) {
        this.geoLiteFile = geoLiteFile;
    }

    public int getMaxConnectionPerAddress() {
        return this.maxConnectionPerAddress;
    }

    public void setMaxConnectionPerAddress(final int maxConnectionPerAddress) {
        this.maxConnectionPerAddress = maxConnectionPerAddress;
    }

    public boolean isCheckPlayerAddress() {
        return this.checkPlayerAddress;
    }

    public void setCheckPlayerAddress(final boolean checkPlayerAddress) {
        this.checkPlayerAddress = checkPlayerAddress;
    }

    public boolean isCheckPlayerRegion() {
        return this.checkPlayerRegion;
    }

    public void setCheckPlayerRegion(final boolean checkPlayerRegion) {
        this.checkPlayerRegion = checkPlayerRegion;
    }

    public List<String> getAllowedRegions() {
        return this.allowedRegions;
    }

    public int getMaxNbtDataLength() {
        return this.maxNbtDataLength;
    }

    public void setMaxNbtDataLength(final int maxNbtDataLength) {
        this.maxNbtDataLength = maxNbtDataLength;
    }

    public boolean isFixSkullNbt() {
        return this.fixSkullNbt;
    }

    public void setFixSkullNbt(final boolean fixSkullNbt) {
        this.fixSkullNbt = fixSkullNbt;
    }

    public boolean isReadCustomPayload() {
        return this.readCustomPayload;
    }

    public void setReadCustomPayload(final boolean readCustomPayload) {
        this.readCustomPayload = readCustomPayload;
    }

    public boolean isBlockChannels() {
        return this.blockChannels;
    }

    public void setBlockChannels(final boolean blockChannels) {
        this.blockChannels = blockChannels;
    }

    public List<String> getBlockedChannels() {
        return this.blockedChannels;
    }

    public int getMaxPyaLoadChannelLength() {
        return this.maxPyaLoadChannelLength;
    }

    public void setMaxPyaLoadChannelLength(final int maxPyaLoadChannelLength) {
        this.maxPyaLoadChannelLength = maxPyaLoadChannelLength;
    }

    public int getMaxPayLoadDataLength() {
        return this.maxPayLoadDataLength;
    }

    public void setMaxPayLoadDataLength(final int maxPayLoadDataLength) {
        this.maxPayLoadDataLength = maxPayLoadDataLength;
    }

    public int getMaxPacketsPerSecond() {
        return this.maxPacketsPerSecond;
    }

    public void setMaxPacketsPerSecond(final int maxPacketsPerSecond) {
        this.maxPacketsPerSecond = maxPacketsPerSecond;
    }

    public boolean isCheckTpsPermission() {
        return this.checkTpsPermission;
    }

    public void setCheckTpsPermission(final boolean checkTpsPermission) {
        this.checkTpsPermission = checkTpsPermission;
    }

    public boolean isAntiRedStone() {
        return this.antiRedStone;
    }

    public void setAntiRedStone(final boolean antiRedStone) {
        this.antiRedStone = antiRedStone;
    }

    public boolean isAntiBot() {
        return this.antiBot;
    }

    public void setAntiBot(final boolean antiBot) {
        this.antiBot = antiBot;
    }

    public boolean isMcProtocolLibFix() {
        return this.mcProtocolLibFix;
    }

    public void setMcProtocolLibFix(final boolean mcProtocolLibFix) {
        this.mcProtocolLibFix = mcProtocolLibFix;
    }

    public int getMaxChannelsPerAddress() {
        return this.maxChannelsPerAddress;
    }

    public void setMaxChannelsPerAddress(final int maxChannelsPerAddress) {
        this.maxChannelsPerAddress = maxChannelsPerAddress;
    }

    public boolean isPacketDebugger() {
        return this.packetDebugger;
    }

    public void setPacketDebugger(final boolean packetDebugger) {
        this.packetDebugger = packetDebugger;
    }

    public boolean isSkipNBT() {
        return this.skipNBT;
    }

    public void setSkipNBT(final boolean skipNBT) {
        this.skipNBT = skipNBT;
    }

    public boolean isCustomDecoder() {
        return this.customDecoder;
    }

    public void setCustomDecoder(final boolean customDecoder) {
        this.customDecoder = customDecoder;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public Map<String, String> getMessages() {
        return this.messages;
    }
}
