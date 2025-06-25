package com.nstut.geco.common.mixins;

import com.nstut.geco.common.registry.ModItems;
import com.nstut.geco.common.util.BoatTypeHelper;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Minimal mixin to add ebony chest boat drop
 */
@Mixin(ChestBoat.class)
public class ChestBoatMixin {
    @Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
    private void addEbonyChestBoatDrop(CallbackInfoReturnable<Item> cir) {
        ChestBoat chestBoat = (ChestBoat) (Object) this;
        Boat.Type variant = chestBoat.getVariant();
        
        // Check if this is our custom ebony chest boat type
        if (variant == BoatTypeHelper.getEbonyType()) {
            cir.setReturnValue(ModItems.EBONY_CHEST_BOAT.get());
        }
        // For all other types, let vanilla handle it
    }
}