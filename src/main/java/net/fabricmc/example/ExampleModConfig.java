package net.fabricmc.example;

import me.roundaround.roundalib.config.ModConfig;
import me.roundaround.roundalib.config.option.BooleanConfigOption;

public class ExampleModConfig extends ModConfig {
  public final BooleanConfigOption MOD_ENABLED;

  protected ExampleModConfig() {
    super("modid");

    MOD_ENABLED = registerConfigOption(BooleanConfigOption.builder(this,
            "modEnabled",
            "pickupnotifications.mod_enabled.label")
        .setComment("Simple toggle for the mod! Set to false to disable.")
        .build());
  }
}
