package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC3031 extends BaseIC {

   public MC3031() {
      this.ICName = "INV RS NAND LAT";
      this.ICNumber = "[MC3031]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, false, "Set", "Reset", "");
      this.chipState.setOutputs("Output", "", "");
      this.ICDescription = "The MC3031 implements a RS NAND latch with an inversed result for when both inputs are the same. Only the Q output is available. When the S (inverse \"set\") input exclusively goes high, the output (Q) goes high and stays high even if S goes low again. When the R (inverse \"reset\") input exclusively goes high, the output (Q) goes low and stays low even if R goes low again. If both go high at the same time, the output goes high. If both go low at the same time, the output does not change. <br /><br />See MC3033 for a regular RS NAND latch.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneLow() && previousInputs.isInputOneHigh() && currentInputs.isInputTwoLow() && previousInputs.isInputTwoHigh()) {
         this.switchLever(Lever.BACK, signBlock, true);
      } else if(currentInputs.isInputOneLow() && previousInputs.isInputOneHigh() && currentInputs.isInputTwoHigh()) {
         this.switchLever(Lever.BACK, signBlock, true);
      } else if(currentInputs.isInputTwoLow() && previousInputs.isInputTwoHigh() && currentInputs.isInputOneHigh()) {
         this.switchLever(Lever.BACK, signBlock, false);
      }

   }
}
