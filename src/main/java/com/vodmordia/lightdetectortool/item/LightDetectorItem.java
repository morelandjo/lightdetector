package com.vodmordia.lightdetectortool.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LightDetectorItem extends Item {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightDetectorItem.class);
    
    public LightDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // called every tick for items in inventory
        if (!level.isClientSide && entity instanceof Player player) {
            // Update every 10 ticks (0.5 seconds)
            if (level.getGameTime() % 10 == 0) {
                // Add debug for first few ticks to confirm detection is running
                if (level.getGameTime() % 100 == 0) { // Every 5 seconds
                    LOGGER.info("[LIGHT DETECTOR] Light detection active for player {} at game time {}", 
                               player.getName().getString(), level.getGameTime());
                }
                updateLightLevel(stack, level, player);
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
    
    private void updateLightLevel(ItemStack stack, Level level, Player player) {
        BlockPos playerPos = player.blockPosition();
        
        // Get the light level
        int blockLight = level.getLightEngine().getLayerListener(LightLayer.BLOCK).getLightValue(playerPos);
        int skyLight = level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(playerPos);
        int lightLevel = Math.max(blockLight, skyLight);
        
        // Only update if the light level has changed
        int currentStored = getCurrentLightLevel(stack);
        if (currentStored != lightLevel) {
            LOGGER.info("[LIGHT DETECTOR] Light level changed: {} -> {} (Block: {}, Sky: {}) for player {} at {}", 
                       currentStored, lightLevel, blockLight, skyLight, player.getName().getString(), playerPos);
            
            // Store the light level in custom data
            CompoundTag tag = new CompoundTag();
            tag.putInt("LightLevel", lightLevel);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            
            // Set custom model data based on sprite index for model selection
            String customModelString = getLightLevelCustomModelString(lightLevel);
            LOGGER.info("[LIGHT DETECTOR] Mapping light level {} to custom model string: {}", lightLevel, customModelString);
            
            if (customModelString != null) {
                // Set custom_model_data with string identifier for 1.21.4+ format
                CustomModelData customModelData = new CustomModelData(List.of(), List.of(), List.of(customModelString), List.of());
                stack.set(DataComponents.CUSTOM_MODEL_DATA, customModelData);
                LOGGER.info("[LIGHT DETECTOR] Applied custom_model_data with string: {}", customModelString);
            } else {
                // Remove custom_model_data to use default model for sprite index 0
                stack.remove(DataComponents.CUSTOM_MODEL_DATA);
                LOGGER.info("[LIGHT DETECTOR] Using default model (removed custom_model_data for light level 0-1)");
            }
            
            // Verify the model data was set correctly
            CustomModelData verifyCustomModelData = stack.get(DataComponents.CUSTOM_MODEL_DATA);
            if (verifyCustomModelData != null) {
                LOGGER.info("[LIGHT DETECTOR] Item now has custom_model_data: {}", verifyCustomModelData);
            } else {
                LOGGER.info("[LIGHT DETECTOR] Item now uses default model (no custom_model_data)");
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int lightLevel = getCurrentLightLevel(stack);
        tooltip.add(Component.translatable("lightdetectortool.light_level", lightLevel));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    /**
     * Gets the custom model data string for the light level (1.21.4+ format)
     * Returns null for light levels 13-15 to use the default fallback model (brightest)
     * Reversed mapping: higher light levels use lower sprite indices
     */
    public static String getLightLevelCustomModelString(int lightLevel) {
        return switch (lightLevel) {
            case 13, 14, 15 -> null;         // Use fallback model for top 3 levels (brightest, sprite 0)
            case 12 -> "light_level_1";      // sprite 1
            case 11 -> "light_level_2";      // sprite 2
            case 10 -> "light_level_3";      // sprite 3
            case 9 -> "light_level_4";       // sprite 4
            case 8 -> "light_level_5";       // sprite 5
            case 7 -> "light_level_6";       // sprite 6
            case 6 -> "light_level_7";       // sprite 7
            case 5 -> "light_level_8";       // sprite 8
            case 4 -> "light_level_9";       // sprite 9
            case 3 -> "light_level_10";      // sprite 10
            case 2 -> "light_level_11";      // sprite 11
            case 0, 1 -> "light_level_12";   // Bottom 2 levels (darkest, sprite 12)
            default -> null; // Invalid values default to fallback model
        };
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
                return tag.getInt("LightLevel");
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
