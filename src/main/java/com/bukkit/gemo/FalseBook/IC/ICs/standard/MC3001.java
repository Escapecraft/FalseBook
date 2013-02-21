package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC3001 extends BaseIC {

   public MC3001() {
      this.ICName = "2-INPUT NAND";
      this.ICNumber = "[MC3001]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, false, "Input A", "Input B", "");
      this.chipState.setOutputs("Output", "", "");
      this.ICDescription = "The MC3001 outputs a high if and only if both inputs are not all high.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && currentInputs.isInputTwoHigh()) {
         this.switchLever(Lever.BACK, signBlock, false);
      } else {
         this.switchLever(Lever.BACK, signBlock, true);
      }

   }
}
