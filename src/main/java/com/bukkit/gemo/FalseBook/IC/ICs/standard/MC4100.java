package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC4100 extends BaseIC {

   public MC4100() {
      this.ICName = "FULL SUBTRACTOR";
      this.ICNumber = "[MC4100]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, true, "Bit to be subtracted from (minuend)", "First bit to subract (subtrahend)", "Second bit to subtract (subtrahend)");
      this.chipState.setOutputs("Difference", "Borrow", "Borrow (always the same as Output2)");
      this.ICDescription = "The MC4100 implements a <a href=\"http://en.wikipedia.org/wiki/Subtractor#Full_subtractor\">full subtractor.</a>";
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
      boolean carry = inputC & !(inputA ^ inputB) | !inputA & inputB;
      this.switchLever(Lever.BACK, signBlock, result, 3);
      this.switchLever(Lever.LEFT, signBlock, carry, 2);
      this.switchLever(Lever.RIGHT, signBlock, carry, 2);
   }
}
