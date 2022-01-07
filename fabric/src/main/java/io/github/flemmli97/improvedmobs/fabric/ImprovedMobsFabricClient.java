package io.github.flemmli97.improvedmobs.fabric;

import io.github.flemmli97.improvedmobs.client.ClientEvents;
import io.github.flemmli97.improvedmobs.fabric.config.ConfigLoader;
import io.github.flemmli97.improvedmobs.fabric.config.ConfigSpecs;
import io.github.flemmli97.improvedmobs.fabric.network.PacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ImprovedMobsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> ClientEvents.showDifficulty(matrixStack));
        PacketHandler.register();
        ConfigSpecs.initClientConfig();
        ConfigLoader.loadClient();
    }
}
