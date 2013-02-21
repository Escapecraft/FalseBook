package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC3034 extends BaseIC {

   public MC3034() {
      this.ICName = "D EDGE FLIPFLOP";
      this.ICNumber = "[MC3034]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(false, true, true, "Data", "Clock", "Reset");
      this.chipState.setOutputs("Output", "", "");
      this.ICDescription = "The MC3034 implements a flip flop that sets its output to the state of the D (\"data\") input whenever the clock input goes from low to high. If the D input changes state without the clock going from low to high, the output will not change. If the D input changes while the clock is high, the output will not change. Also, while the reset input is high the output state is forced to low.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputThreeHigh()) {
         this.switchLever(Lever.BACK, signBlock, false);
      } else if(currentInputs.isInputTwoHigh() && previousInputs.isInputTwoLow()) {
         this.switchLever(Lever.BACK, signBlock, currentInputs.isInputOneHigh());
      }

   }
}
