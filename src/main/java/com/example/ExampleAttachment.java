package com.example;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;

@SuppressWarnings("UnstableApiUsage")
public record ExampleAttachment(int value) {
  public static final Codec<ExampleAttachment> CODEC =
      RecordCodecBuilder.create((instance) -> instance.group(Codec.INT.fieldOf(
      "value").forGetter((inst) -> inst.value)).apply(instance, ExampleAttachment::new));
  public static final PacketCodec<ByteBuf, ExampleAttachment> PACKET_CODEC = PacketCodecs.codec(CODEC);
  public static final AttachmentType<ExampleAttachment> TYPE = AttachmentRegistry.create(
      Identifier.of(ExampleMod.MOD_ID, "example"),
      (builder) -> builder.initializer(() -> new ExampleAttachment(0))
          .persistent(CODEC)
          .syncWith(PACKET_CODEC, AttachmentSyncPredicate.all())
  );

  public static void init() {
    // Empty init to force loading & type registration
  }
}
