package pw.narumi.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import pw.narumi.Natsuki;

public class ConfigReader {

  private final Gson gson;
  private final String filePath;

  public ConfigReader() {
    this.filePath = "Natsuki/config.json";
    gson = new GsonBuilder().setPrettyPrinting().create();
  }

  public void createConfig() throws IOException {
    try (final Writer writer = new FileWriter(filePath)) {
      gson.toJson(new Config(), writer);
    }
  }

  public void readConfig() throws IOException {
    try (final Reader reader = new FileReader(filePath)) {
      Natsuki.getInstance().setConfig(gson.fromJson(reader, Config.class));
    }
  }
}
