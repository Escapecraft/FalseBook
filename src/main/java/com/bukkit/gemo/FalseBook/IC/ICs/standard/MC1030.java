package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1030 extends BaseIC {

   public MC1030() {
      this.ICName = "INPUT EQUALS";
      this.ICNumber = "[MC1030]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, false, "Input A", "Input B", "");
      this.chipState.setOutputs("Output: A == B", "Output: A high, B low", "Output: A low, B high");
      this.ICDescription = "The MC1030 outputs if Input A equals Input B. <br /><ul><li>Input A equals Input B: Output 1 goes high, Output 2 & Output 3 go low</li><li>Input A is High, Input B is low: Output 2 goes high, Output 1 & Output 3 go low</li><li>Input B is high, Input A is low: Output 3 goes high, Output 1 & Output 2 go low</li></ul>";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      boolean inputA = currentInputs.isInputOneHigh();
      boolean inputB = currentInputs.isInputTwoHigh();
      if((!inputA || !inputB) && (inputA || inputB)) {
         if(inputA && !inputB) {
            this.switchLever(Lever.BACK, signBlock, false, 3);
            this.switchLever(Lever.LEFT, signBlock, true, 2);
            this.switchLever(Lever.RIGHT, signBlock, false, 2);
         } else if(!inputA && inputB) {
            this.switchLever(Lever.BACK, signBlock, false, 3);
            this.switchLever(Lever.LEFT, signBlock, false, 2);
            this.switchLever(Lever.RIGHT, signBlock, true, 2);
         }
      } else {
         this.switchLever(Lever.BACK, signBlock, true, 3);
         this.switchLever(Lever.LEFT, signBlock, false, 2);
         this.switchLever(Lever.RIGHT, signBlock, false, 2);
      }

   }
}
