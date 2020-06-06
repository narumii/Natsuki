package pw.narumi.config;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import pw.narumi.Natsuki;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
