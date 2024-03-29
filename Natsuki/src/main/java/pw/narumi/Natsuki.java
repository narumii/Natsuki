package pw.narumi;

import com.maxmind.geoip2.DatabaseReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import pw.narumi.common.Holder;
import pw.narumi.config.Config;
import pw.narumi.config.ConfigReader;

public class Natsuki {

  private final String version = "1.6.9-BIG_CHUNGUS";
  private DatabaseReader databaseReader;
  private ConfigReader configReader;
  private Config config;

  public void load() {
    configReader = new ConfigReader();
    config = new Config();

    initConfig();
    loadFiles();
    //loadGeoLite();

    Holder.startTask();
    Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

    System.out.println("Pomyslnie zaladowano Natsuki " + version + " [PaperSpigot 1.8.8]");
    System.out.println(" ");
  }

  public void reload() {
    initConfig();
    stop();
    loadFiles();
    //loadGeoLite();
  }

  private void stop() {
    try {
      saveFiles();
    } catch (final Exception e) {
      System.err.println("Nie mozna zapisac plikow");
    }
  }

  private void initConfig() {
    final File file = new File("Natsuki");

    if (!file.exists()) {
      file.mkdirs();
    }

    try {
      if (!new File(file, "config.json").exists()) {
        System.err.println("Nie znaleziono pliku konfuguracji, tworzenie pliku");
        configReader.createConfig();
      }

      System.out.println("Ladowanie konfiguracji silnika");
      configReader.readConfig();
    } catch (final Exception e) {
      System.err.println(
          "Wystapil blad podczas ladowania pliku konfiguracyjnego. Usun plik konfuguracyjny i uruchom silnik ponownie.");
      System.exit(1);
    }
  }

    /*private void loadGeoLite() {
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
    }*/

  private void loadFiles() {
    try {
      final File whiteList = new File("Natsuki/whitelist.txt");

      if (!whiteList.exists()) {
        whiteList.createNewFile();
      }

      final Scanner wh = new Scanner(whiteList);
      while (wh.hasNext()) {
        final String address = wh.next().replace(" ", "");
        Holder.getWhitelist().add((address.contains(":") ? address.split(":")[0] : address));
      }
      wh.close();
    } catch (final Exception e) {
      System.err.println("Nie mozna zaladowac plikow");
    }
  }

  private void saveFiles() throws IOException {
    final File whiteList = new File("Natsuki/whitelist.txt");
    final Path path = Paths.get(whiteList.getPath());
    Files.write(path, Holder.getWhitelist(), StandardCharsets.UTF_8);
  }

  private static Natsuki instance;

  private Natsuki() {
    if (instance != null) {
      throw new IllegalStateException("Cos ci sie chyba popierdolilo :D");
    }
  }

  public static Natsuki getInstance() {
    if (instance == null) {
      instance = new Natsuki();
    }

    return instance;
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
