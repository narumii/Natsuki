package pw.narumi;

import com.maxmind.geoip2.DatabaseReader;
import pw.narumi.common.Utils;
import pw.narumi.config.Config;
import pw.narumi.config.ConfigReader;
import pw.narumi.common.Holder;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class Natsuki {

    private final Logger logger = Logger.getLogger("Natsuki");
    private final String[] serverAddress = new String[2];
    private final String[] UID = new String[1];
    private final String version = "0.9.9.6-Beta";
    private DatabaseReader databaseReader;
    private ConfigReader configReader;
    private Config config;

    public void load() {
        checkStatus();
        checkVersion();

        configReader = new ConfigReader();
        config = new Config();

        initConfig();
        loadFiles();
        loadGeoLite();

        Holder.startTask();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        System.out.println("Pomyslnie zaladowano Natsuki " + version + " [PaperSpigot 1.8.8]");
        System.out.println(" ");

        Utils.getRuntime().gc();
    }

    public void reload() {
        checkStatus();
        checkVersion();
        checkBlackList();

        initConfig();

        loadGeoLite();
    }

    private void stop() {
        try {
            saveFiles();
        }catch (final Exception e) {
            System.err.println("Nie mozna zapisac plikow");
        }
    }

    //:D
    public void checkBlackList() {
        try {
            System.out.println("Sprawdzanie statusu serwera");
            final String url = "https://raw.githubusercontent.com/narumii/d/master/server_blacklist";
            final HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            final Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                if (scanner.nextLine().equals(getUID()[0])) {
                    System.err.println("Serwer znajduje sie na blackliscie. Mozesz sie odwolac do blacklisty na naszym discord: https://discord.gg/amutHux");
                    System.exit(0);
                }
            }
            scanner.close();
        } catch (final Exception e) {
            System.err.println("Nie mozna polaczyc sie ze strona");
            System.exit(0);
        }
    }

    //:D
    private void checkVersion() {
        try {
            System.out.println("Sprawdzanie wersji silnika");
            final HttpsURLConnection connection = (HttpsURLConnection) new URL("https://raw.githubusercontent.com/narumii/d/master/d").openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            final Scanner scanner = new Scanner(connection.getInputStream());
            String readedVersion = null;

            while (scanner.hasNext()) {
                readedVersion = scanner.next();
            }

            if (!version.equals(readedVersion)) {
                System.err.println("Uzywasz starej wersji natsuki. Mozesz pobrac najnowsza wersje z discorda: https://discord.gg/amutHux");
                System.exit(0);
            }

            scanner.close();
            System.out.println("Posiadasz najnowsza wersje silnika");
        } catch (final Exception e) {
            System.err.println("Nie mozna polaczyc sie ze strona");
            System.exit(0);
        }
    }

    //:D
    private void checkStatus() {
        try {
            System.out.println("Sprawdzanie statusu silnika");
            final HttpsURLConnection connection = (HttpsURLConnection) new URL("https://raw.githubusercontent.com/narumii/d/master/fujne").openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            final Scanner scanner = new Scanner(connection.getInputStream());
            String bool = null;

            while (scanner.hasNext()) {
                bool = scanner.next();
            }

            if (bool == null || !bool.equals("true")) {
                System.err.println("Silnik zostal wylaczony. Wiecej informacji znajdziesz na discord: https://discord.gg/amutHux");
                System.exit(0);
            }

            scanner.close();
        } catch (final Exception e) {
            System.err.println("Nie mozna polaczyc sie ze strona");
            System.exit(0);
        }
    }

    private void initConfig() {
        final File file = new File("Natsuki"), configFile = new File("Natsuki/config.json");

        if (!file.exists())
            file.mkdirs();

        try {
            if (!configFile.exists()) {
                System.err.println("Nie znaleziono pliku konfuguracji, tworzenie pliku");
                configReader.createConfig();
            }

            System.out.println("Ladowanie konfiguracji silnika");
            configReader.readConfig();
        } catch (final Exception e) {
            System.err.println("Wystapil blad podczas ladowania pliku konfiguracyjnego. Prawdopodonie uzywasz starego configu.");
            try {
                configReader.createConfig();
            } catch (final IOException ioException) {
                System.err.println("Wystapil blad podczas tworzenia nowego pliku konfigurycajnego");
            }
            System.exit(1);
        }
    }

    public void loadGeoLite() {
        try {
            final File file = new File(getConfig().API.getGeoLiteFile);
            if (file.exists())
                databaseReader = new DatabaseReader.Builder(file).build();
            else {
                System.err.println("Nie mozna zaladowac bazy danych GeoLite2, wylaczam sprawdzanie regionu");
                getConfig().CONNECTION.REGION.check = false;
            }

        } catch (final IOException e) {
            getConfig().CONNECTION.REGION.check = false;
            System.err.println("Nie mozna zaladowac bazy danych GeoLite2, wylaczanie sprawdzanie regionu");
        }
    }

    private void loadFiles() {
        try {
            final File whiteList = new File("Natsuki/whitelist.txt");

            if (!whiteList.exists())
                whiteList.createNewFile();

            final Scanner wh = new Scanner(whiteList);
            while (wh.hasNext()) {
                final String address = wh.next().replace(" ", "");;
                Holder.getWhitelist().add((address.contains(":") ? address.split(":")[0] : address));
            }
            wh.close();
        }catch (final Exception e){
            System.err.println("Nie mozna zaladowac plikow");
        }
    }

    public void saveFiles() throws IOException {
        final File whiteList = new File("Natsuki/whitelist.txt");
        final Path path = Paths.get(whiteList.getPath());
        Files.write(path, Holder.getWhitelist(), StandardCharsets.UTF_8);
    }

    private static Natsuki instance;

    private Natsuki() {
        if (instance != null)
            throw new IllegalStateException("Cos ci sie chyba popierdolilo :D");
    }

    public static Natsuki getInstance() {
        if (instance == null)
            instance = new Natsuki();

        return instance;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public String[] getServerAddress() {
        return this.serverAddress;
    }

    public String[] getUID() {
        return this.UID;
    }

    public String getVersion() {
        return this.version;
    }

    public DatabaseReader getDatabaseReader() {
        return this.databaseReader;
    }

    public Config getConfig() {
        return this.config;
    }

    public void setConfig(final Config config) {
        this.config = config;
    }
}
