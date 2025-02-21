package net.blay09.mods.hardcorerevival.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.*;
import net.blay09.mods.hardcorerevival.HardcoreRevival;
import net.blay09.mods.hardcorerevival.config.HardcoreRevivalConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class KnockoutRestrictionHandler {

    public static void initialize() {
        Balm.getEvents().onEvent(UseBlockEvent.class, KnockoutRestrictionHandler::onUseBlock, EventPriority.Highest);
        Balm.getEvents().onEvent(UseItemEvent.class, KnockoutRestrictionHandler::onUseItem, EventPriority.Highest);
        Balm.getEvents().onEvent(TossItemEvent.class, KnockoutRestrictionHandler::onTossItem, EventPriority.Highest);
        Balm.getEvents().onEvent(PlayerAttackEvent.class, KnockoutRestrictionHandler::onAttack, EventPriority.Highest);
        Balm.getEvents().onEvent(DigSpeedEvent.class, KnockoutRestrictionHandler::onDigSpeed, EventPriority.Highest);
        Balm.getEvents().onEvent(LivingHealEvent.class, KnockoutRestrictionHandler::onHeal);
    }

    public static void onHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (HardcoreRevival.getRevivalData(player).isKnockedOut()) {
                event.setCanceled(false);
            }
        }
    }

    public static void onDigSpeed(DigSpeedEvent event) {
        Player player = event.getPlayer();
        if (player != null && HardcoreRevival.getRevivalData(player).isKnockedOut()) {
            event.setSpeedOverride(0f);
            event.setCanceled(true);
        }
    }

    public static void onUseBlock(UseBlockEvent event) {
        Player player = event.getPlayer();
        if (HardcoreRevival.getRevivalData(player).isKnockedOut()) {
            ItemStack itemStack = player.getItemInHand(event.getHand());
            if (!HardcoreRevivalConfig.getActive().allowBows || !(itemStack.getItem() instanceof BowItem)) {
                event.setCanceled(true);
            }
        }
    }

    public static void onUseItem(UseItemEvent event) {
        LivingEntity player = event.getPlayer();
        if (HardcoreRevival.getRevivalData(player).isKnockedOut()) {
            ItemStack itemStack = player.getItemInHand(event.getHand());
            if (!HardcoreRevivalConfig.getActive().allowBows || !(itemStack.getItem() instanceof BowItem)) {
                event.setCanceled(true);
            }
        }
    }

    public static void onTossItem(TossItemEvent event) {
        Player player = event.getPlayer();
        if (HardcoreRevival.getRevivalData(player).isKnockedOut()) {
            // We try to suppress the drop on the client too, but if that failed for some reason, just try to revert the action
            if (player.addItem(event.getItemStack())) {
                event.setCanceled(true);
            }
        }
    }

    public static void onAttack(PlayerAttackEvent event) {
        Player player = event.getPlayer();
        if (player != null && HardcoreRevival.getRevivalData(player).isKnockedOut()) {
            if (HardcoreRevivalConfig.getActive().allowUnarmedMelee && player.getMainHandItem().isEmpty()) {
                return;
            }

            event.setCanceled(true);
        }
    }
}
