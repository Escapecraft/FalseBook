package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1231 extends BaseIC {

   public MC1231() {
      this.ICName = "TIME CONTROL";
      this.ICNumber = "[MC1231]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Set to day", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.ICDescription = "The MC1231 changes the time to day if the input goes from low to high and changes the time to night if the input goes from high to low.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         signBlock.getWorld().setTime(0L);
         this.switchLever(Lever.BACK, signBlock, true);
      } else if(currentInputs.isInputOneLow() && previousInputs.isInputOneHigh()) {
         signBlock.getWorld().setTime(13000L);
         this.switchLever(Lever.BACK, signBlock, false);
      }

   }
}
