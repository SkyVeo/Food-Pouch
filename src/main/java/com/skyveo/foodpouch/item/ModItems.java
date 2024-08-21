package com.skyveo.foodpouch.item;

import com.skyveo.foodpouch.FoodPouch;
import com.skyveo.foodpouch.item.custom.FoodPouchItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FoodPouch.MOD_ID);

    public static final DeferredItem<Item> FOOD_POUCH = ITEMS.register("food_pouch", () -> new FoodPouchItem(new Item.Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)));

    public static void load(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
