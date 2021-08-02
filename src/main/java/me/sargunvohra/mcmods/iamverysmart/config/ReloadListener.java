package me.sargunvohra.mcmods.iamverysmart.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.sargunvohra.mcmods.iamverysmart.match.ComposedMatcher;
import me.sargunvohra.mcmods.iamverysmart.match.SingleMatcher;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class ReloadListener implements SimpleSynchronousResourceReloadListener {

    public static final ReloadListener INSTANCE = new ReloadListener();

    private Gson gson = new Gson();
    private ComposedMatcher matcher = new ComposedMatcher(new SingleMatcher(), new SingleMatcher());

    private ReloadListener() {
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier("iamverysmart", "reload_listener");
    }

    private SingleMatcher buildMatcher(ResourceManager resourceManager, String fileName) {
        SingleMatcher ret = new SingleMatcher();
        resourceManager
            .findResources("iamverysmart", path -> path.endsWith(fileName)).stream()
            .map(resourceId -> {
                Resource resource;
                try {
                    resource = resourceManager.getResource(resourceId);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null; // skip the ones we can't read
                }
                return gson.<List<String>>fromJson(
                    new InputStreamReader(resource.getInputStream()),
                    new TypeToken<List<String>>() {}.getType()
                );
            })
            .filter(Objects::nonNull)
            .forEach(list -> list.forEach(ret::add));
        return ret;
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        matcher = new ComposedMatcher(
            buildMatcher(resourceManager, "include_recipes.json"),
            buildMatcher(resourceManager, "exclude_recipes.json")
        );
        LogManager.getLogger().info("Loaded {}", matcher);
    }

    public ComposedMatcher getMatcher() {
        return matcher;
    }
}
