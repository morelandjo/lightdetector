package com.vodmordia.lightdetectortool.client;

import com.vodmordia.lightdetectortool.LightDetectorMod;
import com.vodmordia.lightdetectortool.item.LightDetectorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = LightDetectorMod.MODID, value = Dist.CLIENT)
public class ClientSetup {
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onClientPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        // Only process on client side for the local player
        if (!level.isClientSide() || player != Minecraft.getInstance().player) {
            return;
        }

        // Update every 10 ticks (0.5 seconds)
        tickCounter++;
        if (tickCounter < 10) {
            return;
        }
        tickCounter = 0;

        // Check all inventory slots for light detector items
        updateLightDetectorInSlot(player, level, player.getItemBySlot(EquipmentSlot.MAINHAND));
        updateLightDetectorInSlot(player, level, player.getItemBySlot(EquipmentSlot.OFFHAND));

        // Check rest of inventory
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof LightDetectorItem) {
                updateLightDetectorInSlot(player, level, stack);
            }
        }
    }

    private static void updateLightDetectorInSlot(Player player, Level level, ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof LightDetectorItem)) {
            return;
        }

        BlockPos playerPos = player.blockPosition();

        // Get the light level
        int blockLight = level.getLightEngine().getLayerListener(LightLayer.BLOCK).getLightValue(playerPos);
        int skyLight = level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(playerPos);
        int lightLevel = Math.max(blockLight, skyLight);

        // Only update if the light level has changed
        int currentStored = LightDetectorItem.getCurrentLightLevel(stack);
        if (currentStored != lightLevel) {
            // Store the light level in custom data
            CompoundTag tag = new CompoundTag();
            tag.putInt("LightLevel", lightLevel);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

            // Set custom model data with normalized float for range_dispatch
            float normalizedValue = LightDetectorItem.getNormalizedLightLevel(stack);
            List<Float> floats = new ArrayList<>();
            floats.add(normalizedValue);
            CustomModelData customModelData = new CustomModelData(
                floats,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
            );
            stack.set(DataComponents.CUSTOM_MODEL_DATA, customModelData);
        }
    }
}
