package net.fabricmc.example.compat;

import me.roundaround.roundalib.client.gui.widget.config.ConfigListWidget;
import me.roundaround.roundalib.client.gui.widget.config.ControlRegistry;
import me.roundaround.roundalib.client.gui.widget.config.ToggleControl;
import me.roundaround.roundalib.config.option.BooleanConfigOption;
import net.fabricmc.example.ExampleMod;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class RoundaLibControlRegister {
  private RoundaLibControlRegister() {
  }

  public static void init() {
    try {
      ControlRegistry.register(ExampleMod.CONFIG.MOD_ENABLED.getId(),
          RoundaLibControlRegister::customToggleFactory);
    } catch (ControlRegistry.RegistrationException e) {
      ExampleMod.LOGGER.error("Failed to register custom control for mod config!", e);
    }
  }

  private static ToggleControl customToggleFactory(
      ConfigListWidget.OptionEntry<Boolean, BooleanConfigOption> parent) {
    final ToggleControl control = new ToggleControl(parent);

    ((ButtonWidget) control.children().get(0)).setMessage(Text.literal(parent.getOption()
        .getValue()
        .toString()));

    parent.getOption().subscribeToValueChanges((prev, curr) -> {
      ((ButtonWidget) control.children().get(0)).setMessage(Text.literal(curr.toString()));
    });

    return control;
  }
}
