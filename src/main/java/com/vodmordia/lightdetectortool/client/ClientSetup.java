package com.vodmordia.lightdetectortool.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import com.vodmordia.lightdetectortool.LightDetectorMod;
import com.vodmordia.lightdetectortool.item.LightDetectorItem;

public class ClientSetup {
    
    public static void registerItemProperties() {
        // smooth transitions
        ItemProperties.register(
            LightDetectorMod.LIGHT_DETECTOR.get(),
            ResourceLocation.fromNamespaceAndPath(LightDetectorMod.MODID, "light_level"),
            (stack, level, entity, seed) -> LightDetectorItem.getNormalizedLightLevel(stack)
        );
    }
}
