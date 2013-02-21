package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.ICUtils;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC3032 extends BaseIC {

   public MC3032() {
      this.ICName = "NEG JK-FF";
      this.ICNumber = "[MC3032]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(false, true, true, "Clock", "K-Input", "J-Input");
      this.chipState.setOutputs("Output", "", "");
      this.ICDescription = "The MC3032 implements a JK flip flop that updates its output based on the states of inputs J and K when the clock falls.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      boolean inputK = currentInputs.isInputTwoHigh();
      boolean inputJ = currentInputs.isInputThreeHigh();
      if(currentInputs.isInputOneLow() && previousInputs.isInputOneHigh()) {
         if(inputJ && !inputK) {
            this.switchLever(Lever.BACK, signBlock, true);
         } else if(!inputJ && inputK) {
            this.switchLever(Lever.BACK, signBlock, false);
         }

         if(inputJ && inputK) {
            this.switchLever(Lever.BACK, signBlock, !ICUtils.isLeverActive(signBlock));
         }
      }

   }
}
