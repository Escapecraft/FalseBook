package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC3021 extends BaseIC {

   public MC3021() {
      this.ICName = "2-INPUT XNOR";
      this.ICNumber = "[MC3021]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, false, "Input A", "Input B", "");
      this.chipState.setOutputs("Output", "", "");
      this.ICDescription = "The MC3021 outputs a high if and only if the first two inputs are the same.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() != currentInputs.isInputTwoHigh()) {
         this.switchLever(Lever.BACK, signBlock, false);
      } else {
         this.switchLever(Lever.BACK, signBlock, true);
      }

   }
}
