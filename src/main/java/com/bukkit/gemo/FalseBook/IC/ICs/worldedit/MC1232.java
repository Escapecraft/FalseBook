package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1232 extends BaseIC {

   public MC1232() {
      this.ICName = "SET-TIME";
      this.ICNumber = "[MC1232]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("worldtime in ticks", "");
      this.ICDescription = "The MC1232 sets the time to the specified time whenever the input (the \"clock\") goes from low to high.";
   }

   public void checkCreation(SignChangeEvent event) {
      try {
         Integer.valueOf(event.getLine(2));
      } catch (Exception var3) {
         SignUtils.cancelSignCreation(event, ChatColor.RED + "Line 3 must be a number.");
         return;
      }

      if(Integer.valueOf(event.getLine(2)).intValue() < 0) {
         event.setLine(2, "0");
      }

      if(Integer.valueOf(event.getLine(2)).intValue() >= 24000) {
         event.setLine(2, "24000");
      }

      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      boolean zeit = false;

      int zeit1;
      try {
         zeit1 = Integer.valueOf(signBlock.getLine(2)).intValue();
         if(zeit1 < 0 || zeit1 >= 24000) {
            zeit1 = 0;
         }
      } catch (Exception var6) {
         return;
      }

      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         signBlock.getWorld().setTime((long)zeit1);
         this.switchLever(Lever.BACK, signBlock, true);
      } else {
         this.switchLever(Lever.BACK, signBlock, false);
      }

   }
}
