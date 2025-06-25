package com.nstut.geco.common.mixins;

import com.nstut.geco.common.registry.ModItems;
import com.nstut.geco.common.util.BoatTypeHelper;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Minimal mixin to add ebony boat drop to the vanilla getDropItem() switch statement
 */
@Mixin(Boat.class)
public class BoatMixin {
    
    @Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
    private void addEbonyBoatDrop(CallbackInfoReturnable<Item> cir) {
        Boat boat = (Boat) (Object) this;
        Boat.Type variant = boat.getVariant();
        
        // Check if this is our custom ebony boat type
        if (variant == BoatTypeHelper.getEbonyType()) {
            cir.setReturnValue(ModItems.EBONY_BOAT.get());
        }
        // For all other types, let vanilla handle it
    }
}