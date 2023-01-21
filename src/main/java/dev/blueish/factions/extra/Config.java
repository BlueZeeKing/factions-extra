package dev.blueish.factions.extra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class Config {
    private static final File file = FabricLoader.getInstance().getGameDir().resolve("config").resolve("factions-extra.json").toFile();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static Config load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();

        try {
            if (!file.exists()) {
                file.getParentFile().mkdir();

                Config defaults = new Config();

                FileWriter writer = new FileWriter(file);
                gson.toJson(defaults, writer);
                writer.close();

                return defaults;
            }

            return gson.fromJson(new FileReader(file), Config.class);
        } catch (Exception e) {
            FactionsExtraMod.LOGGER.error("An error occurred reading the factions extra config file", e);
            return new Config();
        }
    }

    @SerializedName("costToCreate")
    public HashMap<String, Integer> CREATE_COST = new HashMap<>() {{
        put("minecraft:diamond", 2);
    }};

    @SerializedName("maxNumberOfFactions")
    public int MAX_FACTIONS = -1;
}