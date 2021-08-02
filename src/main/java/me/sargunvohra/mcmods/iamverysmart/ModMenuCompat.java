// package me.sargunvohra.mcmods.iamverysmart;
//
// import io.github.prospector.modmenu.api.ModMenuApi;
// import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
// import me.sargunvohra.mcmods.iamverysmart.config.ClientConfig;
// import net.minecraft.client.gui.screen.Screen;
//
// import java.util.function.Function;
//
// @SuppressWarnings("unused")
// public class ModMenuCompat implements ModMenuApi {
//
//     @Override
//     public String getModId() {
//         return "iamverysmart";
//     }
//
//     @Override
//     public Function<Screen, ? extends Screen> getConfigScreenFactory() {
//         return (screen) -> AutoConfig.getConfigScreen(ClientConfig.class, screen).get();
//     }
// }
