package com.vodmordia.lightdetectortool.data;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import com.vodmordia.lightdetectortool.LightDetectorMod;

@EventBusSubscriber(modid = LightDetectorMod.MODID)
public class DataGenerators {
    
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Server event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Server-side data generation (recipes, loot tables, etc.)
        generator.addProvider(true, new LightDetectorRecipeProvider.Runner(packOutput, lookupProvider));
    }
}
