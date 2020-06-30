package pw.narumi;

import com.maxmind.geoip2.DatabaseReader;
import pw.narumi.common.Utils;
import pw.narumi.config.Config;
import pw.narumi.config.ConfigReader;
import pw.narumi.common.Holder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class Natsuki {

    private final Logger logger = Logger.getLogger("Natsuki");
    private final String version = "1.0.0-Beta";
    private DatabaseReader databaseReader;
    private ConfigReader configReader;
    private Config config;

    public void load() {
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
