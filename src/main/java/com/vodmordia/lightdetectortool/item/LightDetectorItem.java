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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class LightDetectorItem extends Item {
      public LightDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // called every tick for items in inventory
        if (!level.isClientSide && entity instanceof Player player) {
            // Update every 10 ticks (0.5 seconds)
            if (level.getGameTime() % 10 == 0) {
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
            // Store the light level
            CompoundTag tag = new CompoundTag();
            tag.putInt("LightLevel", lightLevel);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int lightLevel = getCurrentLightLevel(stack);
        tooltip.add(Component.translatable("lightdetectortool.light_level", lightLevel));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    /**
     * sprite index based on the light level
     */
    public static int getLightLevelSpriteIndex(int lightLevel) {
        return switch (lightLevel) {
            case 15 -> 0;
            case 14 -> 1;
            case 13 -> 2;
            case 12 -> 3;
            case 11 -> 4;
            case 10 -> 5;
            case 9 -> 6;
            case 8 -> 7;
            case 7 -> 8;
            case 6 -> 9;
            case 5 -> 9;
            case 4 -> 10;
            case 3 -> 11;
            case 2 -> 11;
            case 1 -> 12;
            default -> 12; // case 0 and any invalid values
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
        return spriteIndex / 12.0f; // Normalize to 0.0-1.0 based on sprite index
    }
}
