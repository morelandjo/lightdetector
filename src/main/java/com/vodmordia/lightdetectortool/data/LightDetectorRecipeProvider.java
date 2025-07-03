package com.vodmordia.lightdetectortool.data;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import com.vodmordia.lightdetectortool.LightDetectorMod;

public class LightDetectorRecipeProvider extends RecipeProvider {
    
    // Construct the provider to run
    protected LightDetectorRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.REDSTONE, LightDetectorMod.LIGHT_DETECTOR_ITEM.get())
            .pattern(" L ")
            .pattern("LRL")
            .pattern(" L ")
            .define('L', Items.LAPIS_LAZULI)
            .define('R', Items.REDSTONE)
            .unlockedBy("has_redstone", has(Items.REDSTONE))
            .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI))
            .save(this.output);
    }
    
    // The runner to add to the data generator
    public static class Runner extends RecipeProvider.Runner {
        // Get the parameters from the `GatherDataEvent`s.
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new LightDetectorRecipeProvider(provider, output);
        }
        
        @Override
        public String getName() {
            return "Light Detector Recipes";
        }
    }
}
