package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.example.compat.RoundaLibControlRegister;

public class ExampleModClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    RoundaLibControlRegister.init();
  }
}
