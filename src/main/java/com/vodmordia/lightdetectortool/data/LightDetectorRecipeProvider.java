package com.vodmordia.lightdetectortool.data;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import com.vodmordia.lightdetectortool.LightDetectorMod;

public class LightDetectorRecipeProvider extends RecipeProvider {
    
    public LightDetectorRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, LightDetectorMod.LIGHT_DETECTOR.get())
            .pattern(" L ")
            .pattern("LRL")
            .pattern(" L ")
            .define('L', Items.LAPIS_LAZULI)
            .define('R', Items.REDSTONE)
            .unlockedBy("has_redstone", has(Items.REDSTONE))
            .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI))
            .save(recipeOutput);
    }
}
