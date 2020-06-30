package pw.narumi.common;

import com.maxmind.geoip2.record.Country;
import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import pw.narumi.Natsuki;
import json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static final com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static final DecimalFormat format = new DecimalFormat("##.##");
    private static final SystemInfo systemInfo = new SystemInfo();
    private static final Runtime runtime = Runtime.getRuntime();

    public static OperatingSystemMXBean getOsBean() {
        return osBean;
    }

    public static DecimalFormat getFormat() {
        return format;
    }

    public static SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public static Runtime getRuntime() {
        return runtime;
    }

    public static String getUpTime(final long time) {
        final long day = TimeUnit.MILLISECONDS.toDays(time);
        final long hr = TimeUnit.MILLISECONDS.toHours(time) % 24L;
        final long min = TimeUnit.MILLISECONDS.toMinutes(time) % 60L;
        final long sec = TimeUnit.MILLISECONDS.toSeconds(time) % 60L;
        return "§d" + day + " §7days §d" + hr + " §7hours §d" + min + " §7minutes §d" + sec + " §7seconds";
    }

    public static String humanReadableByteCount(final long bytes) {
        if (bytes < 1000)
            return bytes + " B";

        final int exp = (int) (Math.log(bytes) / Math.log(1000));
        final String pre = ("kMGTPE").charAt(exp-1) + ("");
        return String.format("%.1f %sB", bytes / Math.pow(1000, exp), pre);
    }

    public static String fixString(final String string) {
        return string
                .replace("&", "§")
                .replace("(o)", "●")
                .replace("(*)", "•");
    }

    public static boolean isProxy(final String address) {
        try {
            String url;

            if (Natsuki.getInstance().getConfig().API.proxyCheckKey.equalsIgnoreCase("Your Key") || Natsuki.getInstance().getConfig().API.proxyCheckKey.equalsIgnoreCase("none")) {
                url = "http://api.stopforumspam.org/api?ip=%address%&json".replace("%address%", address);
            } else {
                url = "http://proxycheck.io/v2/%address%?&vpn=1&asn=1&risk=1&key=%key%".replace("%address%", address).replace("%key%", Natsuki.getInstance().getConfig().API.proxyCheckKey);
            }

            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(4000);

            final String json = new String(IOUtils.toByteArray(connection));
            final JSONObject object = new JSONObject(json).getJSONObject(address);

            if (Natsuki.getInstance().getConfig().API.proxyCheckKey.equalsIgnoreCase("Your Key") || Natsuki.getInstance().getConfig().API.proxyCheckKey.equalsIgnoreCase("none"))
                return object.getInt("appears") == 1;
            else
                return object.getString("proxy").equalsIgnoreCase("yes") || object.getInt("risk") > 40;

        } catch (final Exception e) {
            return false;
        }
    }

    public static boolean isCountry(final InetAddress inetAddress) {
        try {
            final Country country = Natsuki.getInstance().getDatabaseReader().country(inetAddress).getCountry();
            if (Natsuki.getInstance().getConfig().CONNECTION.REGION.allowedRegions.contains(country.getIsoCode().toUpperCase()))
                return true;
        }catch (final Exception e){
            return false;
        }
        return false;
    }
}
