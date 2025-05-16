package com.example.mixin;

import com.example.ExampleAttachment;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSignBlock.class)
public abstract class AbstractSignBlockMixin {
  @Inject(
      method = "onUse", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/block/entity/SignBlockEntity;runCommandClickEvent" +
               "(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;" +
               "Lnet/minecraft/util/math/BlockPos;Z)Z",
      shift = At.Shift.AFTER
  ), cancellable = true
  )
  private void beforeRunCommandClickEvent(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      BlockHitResult hit,
      CallbackInfoReturnable<ActionResult> cir,
      @Local SignBlockEntity signBlockEntity
  ) {
    if (!player.isSneaking()) {
      return;
    }

    ExampleAttachment attachment = signBlockEntity.getAttachedOrCreate(ExampleAttachment.TYPE);
    signBlockEntity.setAttached(ExampleAttachment.TYPE, new ExampleAttachment((attachment.value() + 1) % 16));
    world.playSound(null, signBlockEntity.getPos(), SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.BLOCKS, 0.5f, 1f);
    cir.setReturnValue(ActionResult.SUCCESS_SERVER);
  }
}
