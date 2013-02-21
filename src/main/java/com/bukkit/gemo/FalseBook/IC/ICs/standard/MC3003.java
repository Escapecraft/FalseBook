package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC3003 extends BaseIC {

   public MC3003() {
      this.ICName = "3-INPUT NAND";
      this.ICNumber = "[MC3003]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, true, "Input A", "Input B", "Input C");
      this.chipState.setOutputs("Output", "", "");
      this.ICDescription = "The MC3003 outputs a high if and only if all three inputs are not all high.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && currentInputs.isInputTwoHigh() && currentInputs.isInputThreeHigh()) {
         this.switchLever(Lever.BACK, signBlock, false);
      } else {
         this.switchLever(Lever.BACK, signBlock, true);
      }

   }
}
