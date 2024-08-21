package com.skyveo.foodpouch.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;

public class FoodPouchItem extends BundleItem {
    public FoodPouchItem(Properties properties) {
        super(properties);
    }

    public static boolean isFood(ItemStack stack) {
        return !(stack.getItem() instanceof FoodPouchItem) && stack.get(DataComponents.FOOD) != null;
    }

    public ItemStack getFirstFood(ItemStack foodPouch) {
        BundleContents bundlecontents = foodPouch.get(DataComponents.BUNDLE_CONTENTS);
        return bundlecontents == null || bundlecontents.isEmpty() ? ItemStack.EMPTY : bundlecontents.getItemUnsafe(0);
    }

    public boolean isEmpty(ItemStack foodPouch) {
        return this.getFirstFood(foodPouch).isEmpty();
    }

    protected void onContentUpdate(ItemStack foodPouch) {
        if (!this.isEmpty(foodPouch)) {
            foodPouch.set(DataComponents.FOOD, getFirstFood(foodPouch).get(DataComponents.FOOD));
        } else {
            foodPouch.remove(DataComponents.FOOD);
        }
    }

    protected ItemStack consumeFirstFood(ItemStack stack, Level level, LivingEntity livingEntity) {
        BundleContents bundlecontents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundlecontents == null) {
            return stack;
        }

        BundleContents.Mutable builder = new BundleContents.Mutable(bundlecontents);
        ItemStack food = builder.removeOne();
        if (food == null) {
            return stack;
        }

        ItemStack leftovers = food.getItem().finishUsingItem(food, level, livingEntity);
        if (isFood(leftovers)) {
            builder.tryInsert(leftovers);
        } else if (livingEntity instanceof Player player && !player.hasInfiniteMaterials()) {
            if (!player.getInventory().add(leftovers)) {
                player.drop(leftovers, false);
            }
        }

        stack.set(DataComponents.BUNDLE_CONTENTS, builder.toImmutable());
        this.onContentUpdate(stack);

        return stack;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        ItemStack itemStack = slot.getItem();
        if (!itemStack.isEmpty() && !isFood(itemStack)) {
            return false;
        }
        boolean result = super.overrideStackedOnOther(stack, slot, action, player);
        if (result) {
            this.onContentUpdate(stack);
        }
        return result;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (!other.isEmpty() && !isFood(other)) {
            return false;
        }
        boolean result = super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
        if (result) {
            this.onContentUpdate(stack);
        }
        return result;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        ItemStack food = this.getFirstFood(stack);
        return food.getItem().getUseDuration(food, user);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        ItemStack food = this.getFirstFood(stack);
        return food.getItem().getUseAnimation(food);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack foodPouch = player.getItemInHand(usedHand);
        return this.getFirstFood(foodPouch).getItem().use(level, player, usedHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return this.consumeFirstFood(stack, level, livingEntity);
    }
}
