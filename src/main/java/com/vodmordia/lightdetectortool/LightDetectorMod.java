package com.vodmordia.lightdetectortool;

import com.vodmordia.lightdetectortool.item.LightDetectorItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(LightDetectorMod.MODID)
public class LightDetectorMod {
    public static final String MODID = "lightdetectortool";
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<Item> LIGHT_DETECTOR_ITEM = ITEMS.registerItem("light_detector",
            LightDetectorItem::new, new Item.Properties());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LIGHT_DETECTOR_TAB = CREATIVE_MODE_TABS.register("light_detector_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.lightdetectortool.light_detector_tab"))
            .icon(() -> new ItemStack(LIGHT_DETECTOR_ITEM.get()))
            .displayItems((parameters, output) -> output.accept(LIGHT_DETECTOR_ITEM.get()))
            .build());

    public LightDetectorMod(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
