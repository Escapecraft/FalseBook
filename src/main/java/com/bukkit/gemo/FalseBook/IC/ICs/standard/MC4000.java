package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC4000 extends BaseIC {

   public MC4000() {
      this.ICName = "FULL ADDER";
      this.ICNumber = "[MC4000]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, true, "First bit to add", "Second bit to add", "Third bit to add (carry in)");
      this.chipState.setOutputs("Sum", "Carry out", "Carry out (always the same as Output2)");
      this.ICDescription = "The MC4000 implements a full adder. It adds the two input bits, adds the carry in (third bit), and returns the sum and the carry.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      boolean inputA = currentInputs.isInputOneHigh();
      boolean inputB = currentInputs.isInputTwoHigh();
      boolean inputC = currentInputs.isInputThreeHigh();
      boolean result = inputA ^ inputB ^ inputC;
      boolean carry = inputA & inputB | (inputA ^ inputB) & inputC;
      this.switchLever(Lever.BACK, signBlock, result, 3);
      this.switchLever(Lever.LEFT, signBlock, carry, 2);
      this.switchLever(Lever.RIGHT, signBlock, carry, 2);
   }
}
