package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC4200 extends BaseIC {

   public MC4200() {
      this.ICName = "DISPATCHER";
      this.ICNumber = "[MC4200]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, true, "Input", "Clock A", "Clock B");
      this.chipState.setOutputs("(unused)", "Output A", "Output B");
      this.ICDescription = "The MC4200 implements a dispatcher that works similar to two MC3036 level-triggered D flip flops sharing the same input and having no reset input.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      boolean value = currentInputs.isInputOneHigh();
      boolean TargetB = currentInputs.isInputTwoHigh();
      boolean TargetC = currentInputs.isInputThreeHigh();
      if(TargetB) {
         this.switchLever(Lever.LEFT, signBlock, value, 2);
      } else if(TargetC) {
         this.switchLever(Lever.RIGHT, signBlock, value, 2);
      }

   }
}
