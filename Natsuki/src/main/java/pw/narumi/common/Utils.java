package pw.narumi.common;

import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import pw.narumi.Natsuki;

import java.lang.management.ManagementFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static final com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static final DecimalFormat format = new DecimalFormat("##.##");
    private static final SystemInfo systemInfo = new SystemInfo();

    public static OperatingSystemMXBean getOsBean() {
        return osBean;
    }

    public static DecimalFormat getFormat() {
        return format;
    }

    public static SystemInfo getSystemInfo() {
        return systemInfo;
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

    public static String humanReadableByteCountInternet(final long bytes) {
        if (bytes < 1000)
            return bytes + " B";

        final int exp = (int) (Math.log(bytes) / Math.log(1000));
        final String pre = ("kMGTPE").charAt(exp-1) + ("");
        return String.format("%.1f %sB/s", bytes / Math.pow(1000, exp), pre);
    }

    //YYY ZAJEBANE
    public static String generateUUID() {
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        final String input = systemInfo.getOperatingSystem().getFamily() +
                systemInfo.getOperatingSystem().getManufacturer() +
                systemInfo.getOperatingSystem().getVersionInfo().getCodeName() +
                systemInfo.getHardware().getComputerSystem() +
                System.getProperty("os.name") +
                System.getProperty("os.arch") +
                System.getProperty("os.version") +
                systemInfo.getHardware().getProcessor().getProcessorIdentifier().getFamily() +
                osBean.getName() +
                systemInfo.getHardware().getNetworkIFs()[0].getMacaddr() +
                Arrays.toString(Natsuki.getInstance().getServerAddress());
        return bytesToHex(hash.digest(Base64.getEncoder().encode(input.getBytes())));
    }

    //YYY ZAJEBANE
    private static String bytesToHex(final byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //YYY ZAJEBANE
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
}
