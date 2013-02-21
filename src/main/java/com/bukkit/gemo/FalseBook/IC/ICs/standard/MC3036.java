package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC3036 extends BaseIC {

   public MC3036() {
      this.ICName = "D LEVL FLIPFLOP";
      this.ICNumber = "[MC3036]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, true, "Clock", "Data", "Reset");
      this.chipState.setOutputs("Output", "", "");
      this.ICDescription = "The MC3036 implements a flip flop that sets its output to the state of the D (\"data\") input whenever the clock input is high. If the D input changes state while the clock is low, the output will not change. Also, while the reset input is high the output state is forced to low.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputThreeHigh()) {
         this.switchLever(Lever.BACK, signBlock, false);
      } else if(currentInputs.isInputOneHigh()) {
         this.switchLever(Lever.BACK, signBlock, currentInputs.isInputTwoHigh());
      }

   }
}
