package com.skyveo.foodpouch.item;

import com.skyveo.foodpouch.FoodPouch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Objects;

public class ModItemTags {
    public static final TagKey<Item> FOOD_POUCH_CRAFTING_FOOD_INGREDIENTS = create("food_pouch_crafting_food_ingredients");

    private static TagKey<Item> create(String name) {
        return ItemTags.create(Objects.requireNonNull(ResourceLocation.tryBuild(FoodPouch.MOD_ID, name)));
    }
}
