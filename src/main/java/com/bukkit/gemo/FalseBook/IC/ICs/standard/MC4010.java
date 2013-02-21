package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC4010 extends BaseIC {

   public MC4010() {
      this.ICName = "HALF ADDER";
      this.ICNumber = "[MC4010]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(false, true, true, "Always ignored (front input)", "First bit to add", "Second bit to add");
      this.chipState.setOutputs("Sum", "Carry out", "Carry out (always the same as Output2)");
      this.ICDescription = "The MC4010 implements a half adder. It takes two one-bit inputs, adds them, and returns the sum and the carry.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      boolean inputB = currentInputs.isInputTwoHigh();
      boolean inputC = currentInputs.isInputThreeHigh();
      boolean result = inputB ^ inputC;
      boolean carry = inputB & inputC;
      this.switchLever(Lever.BACK, signBlock, result, 3);
      this.switchLever(Lever.LEFT, signBlock, carry, 2);
      this.switchLever(Lever.RIGHT, signBlock, carry, 2);
   }
}
