package com.vodmordia.lightdetectortool.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;

public class LightDetectorItem extends Item {

    public LightDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        int lightLevel = getCurrentLightLevel(stack);
        tooltip.accept(Component.translatable("lightdetectortool.light_level", lightLevel));
        super.appendHoverText(stack, context, display, tooltip, flag);
    }


    /**
     * sprite index based on the light level (13 sprites total: 0-12)
     * Reversed mapping: higher light levels use lower sprite indices
     * - Sprite 0: Light levels 13-15 (top 3 levels, brightest)
     * - Sprites 1-11: Light levels 12-2 (one level each, decreasing brightness)  
     * - Sprite 12: Light levels 0-1 (bottom 2 levels, darkest)
     */
    public static int getLightLevelSpriteIndex(int lightLevel) {
        return switch (lightLevel) {
            case 13, 14, 15 -> 0;   // Top 3 levels (brightest)
            case 12 -> 1;
            case 11 -> 2;
            case 10 -> 3;
            case 9 -> 4;
            case 8 -> 5;
            case 7 -> 6;
            case 6 -> 7;
            case 5 -> 8;
            case 4 -> 9;
            case 3 -> 10;
            case 2 -> 11;
            case 0, 1 -> 12;        // Bottom 2 levels (darkest)
            default -> 0; // Invalid values default to sprite 0
        };
    }
    
    /**
     * Gets the current light level from the item's custom data
     */
    public static int getCurrentLightLevel(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("LightLevel")) {
                return tag.getIntOr("LightLevel", 0);
            }
        }
        return 0; // Default to 0 if no data
    }

    /**
     * Gets the normalized light level for item property (0.0 to 1.0)
     */
    public static float getNormalizedLightLevel(ItemStack stack) {
        int lightLevel = getCurrentLightLevel(stack);
        int spriteIndex = getLightLevelSpriteIndex(lightLevel);
        return spriteIndex / 12.0f; // Normalize to 0.0-1.0 based on 13 sprite indices (0-12)
    }
}
