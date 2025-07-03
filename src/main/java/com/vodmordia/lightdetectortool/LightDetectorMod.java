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
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(LightDetectorMod.MODID)
public class LightDetectorMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "lightdetectortool";
    private static final Logger LOGGER = LoggerFactory.getLogger(LightDetectorMod.class);

    // Create a Deferred Register to hold Items which will all be registered under the "lightdetectortool" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "lightdetectortool" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Item with the id "lightdetectortool:light_detector", combining the namespace and path
    public static final DeferredItem<Item> LIGHT_DETECTOR_ITEM = ITEMS.registerItem("light_detector",
            LightDetectorItem::new, new Item.Properties());

    // Creates a creative tab with the id "lightdetectortool:light_detector_tab" for the light detector item
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LIGHT_DETECTOR_TAB = CREATIVE_MODE_TABS.register("light_detector_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.lightdetectortool.light_detector_tab"))
            .icon(() -> {
                LOGGER.debug("Creating icon for creative tab");
                return new ItemStack(LIGHT_DETECTOR_ITEM.get());
            })
            .displayItems((parameters, output) -> {
                LOGGER.info("Adding Light Detector item to creative tab");
                output.accept(LIGHT_DETECTOR_ITEM.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public LightDetectorMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Light Detector Mod constructor called");
        
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so items get registered
        LOGGER.info("Registering items deferred register");
        ITEMS.register(modEventBus);
        
        // Register the Deferred Register to the mod event bus so tabs get registered
        LOGGER.info("Registering creative mode tabs deferred register");
        CREATIVE_MODE_TABS.register(modEventBus);
        
        LOGGER.info("Light Detector Mod constructor completed");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Light Detector Mod Common Setup");
        
        // Verify our item was registered properly
        LOGGER.info("Light Detector item registration ID: {}", LIGHT_DETECTOR_ITEM.get().toString());
        LOGGER.info("Light Detector item class: {}", LIGHT_DETECTOR_ITEM.get().getClass().getName());
        
        // Check default item stack
        ItemStack defaultStack = LIGHT_DETECTOR_ITEM.get().getDefaultInstance();
        LOGGER.info("Default item stack: {}", defaultStack);
        LOGGER.info("Default item stack item: {}", defaultStack.getItem());
        
        LOGGER.info("Light Detector Mod Common Setup completed");
    }
}
