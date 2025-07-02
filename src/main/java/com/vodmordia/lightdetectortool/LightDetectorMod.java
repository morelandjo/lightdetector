package com.vodmordia.lightdetectortool;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.vodmordia.lightdetectortool.item.LightDetectorItem;
import com.vodmordia.lightdetectortool.client.ClientSetup;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(LightDetectorMod.MODID)
public class LightDetectorMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "lightdetectortool";   
    public static final Logger LOGGER = LogUtils.getLogger();
      // Create a Deferred Register to hold Items
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);    
    public static final DeferredItem<LightDetectorItem> LIGHT_DETECTOR = ITEMS.registerItem("light_detector", 
        LightDetectorItem::new, new Item.Properties().stacksTo(1));

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public LightDetectorMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        
        LOGGER.info("Light Detector Mod Common Setup");
    }    
    // Add the light detector item to the redstone blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(LIGHT_DETECTOR);
        }
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Light Detector Mod Client Setup");
            
            // Register item properties for smooth transitions
            event.enqueueWork(() -> {
                ClientSetup.registerItemProperties();
            });
        }
    }
}
