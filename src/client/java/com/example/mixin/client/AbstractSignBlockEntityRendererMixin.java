package com.example.mixin.client;

import com.example.ExampleAttachment;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.AbstractSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignBlockEntityRenderer.class)
public abstract class AbstractSignBlockEntityRendererMixin {
  @Shadow
  @Final
  private TextRenderer textRenderer;
  @Unique
  private BlockEntityRenderDispatcher issue4658_dispatcher;

  @Inject(method = "<init>", at = @At("RETURN"))
  private void atEndOfConstructor(BlockEntityRendererFactory.Context context, CallbackInfo ci) {
    this.issue4658_dispatcher = context.getRenderDispatcher();
  }

  @Inject(
      method = "render(Lnet/minecraft/block/entity/SignBlockEntity;Lnet/minecraft/client/util/math/MatrixStack;" +
               "Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/block/BlockState;" +
               "Lnet/minecraft/block/AbstractSignBlock;Lnet/minecraft/block/WoodType;" +
               "Lnet/minecraft/client/model/Model;)V", at = @At(
      value = "TAIL"
  )
  )
  protected void beforeMatrixStackPop(
      SignBlockEntity blockEntity,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      int overlay,
      BlockState state,
      AbstractSignBlock block,
      WoodType woodType,
      Model model,
      CallbackInfo ci
  ) {
    Text text = Text.literal("X").formatted(Formatting.RED);
    ExampleAttachment attachment = blockEntity.getAttached(ExampleAttachment.TYPE);
    if (attachment != null) {
      text = Text.literal(String.valueOf(attachment.value())).formatted(Formatting.GREEN);
    }

    matrices.push();
    matrices.translate(0.5f, 1.5f, 0.5f);
    matrices.multiply(this.issue4658_dispatcher.camera.getRotation());
    matrices.scale(0.025f, -0.025f, 0.025f);
    Matrix4f matrix4f = matrices.peek().getPositionMatrix();
    float x = -this.textRenderer.getWidth(text) / 2f;
    int a = (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f) * 255f) << 24;
    this.textRenderer.draw(
        text,
        x,
        0,
        -2130706433,
        false,
        matrix4f,
        vertexConsumers,
        TextRenderer.TextLayerType.SEE_THROUGH,
        a,
        light
    );
    this.textRenderer.draw(
        text,
        x,
        0,
        Colors.WHITE,
        false,
        matrix4f,
        vertexConsumers,
        TextRenderer.TextLayerType.NORMAL,
        0,
        LightmapTextureManager.applyEmission(light, 2)
    );
    matrices.pop();
  }
}
