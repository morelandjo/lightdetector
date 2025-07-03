package com.vodmordia.lightdetectortool.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import com.vodmordia.lightdetectortool.LightDetectorMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventBusSubscriber(modid = LightDetectorMod.MODID, value = Dist.CLIENT)
public class ClientSetup {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSetup.class);
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Light Detector Mod Client Setup");
        LOGGER.info("Completed client setup for light detector");
    }
}
