package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1050 extends BaseIC {

   public MC1050() {
      this.ICName = "SELECTOR";
      this.ICNumber = "[MC1050]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, true, "Input A", "Input B", "Input C");
      this.chipState.setOutputs("Output A - goes high if: A is high, B & C are low", "Output B - goes high if: B is high, A & C are low", "Output C - goes high if: C is high, A & B are low");
      this.ICDescription = "The MC1050 checks if only one of the three inputs is high. If more than one input is high, all three outputs are low.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      boolean inputA = currentInputs.isInputOneHigh();
      boolean inputB = currentInputs.isInputTwoHigh();
      boolean inputC = currentInputs.isInputThreeHigh();
      if(inputA && !inputB && !inputC) {
         this.switchLever(Lever.BACK, signBlock, true, 3);
         this.switchLever(Lever.LEFT, signBlock, false, 2);
         this.switchLever(Lever.RIGHT, signBlock, false, 2);
      } else if(!inputA && inputB && !inputC) {
         this.switchLever(Lever.BACK, signBlock, false, 3);
         this.switchLever(Lever.LEFT, signBlock, true, 2);
         this.switchLever(Lever.RIGHT, signBlock, false, 2);
      } else if(!inputA && !inputB && inputC) {
         this.switchLever(Lever.BACK, signBlock, false, 3);
         this.switchLever(Lever.LEFT, signBlock, false, 2);
         this.switchLever(Lever.RIGHT, signBlock, true, 2);
      } else {
         this.switchLever(Lever.BACK, signBlock, false, 3);
         this.switchLever(Lever.LEFT, signBlock, false, 2);
         this.switchLever(Lever.RIGHT, signBlock, false, 2);
      }

   }
}
