package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1285 extends BaseIC {

   public MC1285() {
      this.ICName = "SET WEATHER";
      this.ICNumber = "[MC1285]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("SUN or RAIN or STORM", "");
      this.ICDescription = "The MC1285 sets the weather to the specified weather, whenever the input (the \"clock\") goes from low to high.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(3, "");
      if(!Parser.isString(event.getLine(2), "sun") && !Parser.isString(event.getLine(2), "rain") && !Parser.isString(event.getLine(2), "storm")) {
         SignUtils.cancelSignCreation(event, ChatColor.RED + "Line 3 must be sun, rain or storm");
      } else {
         event.setLine(2, event.getLine(2).toUpperCase());
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(Parser.isString(signBlock.getLine(2), "sun") || Parser.isString(signBlock.getLine(2), "rain") || Parser.isString(signBlock.getLine(2), "storm")) {
         if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
            if(signBlock.getLine(2).equalsIgnoreCase("sun")) {
               signBlock.getWorld().setStorm(false);
               signBlock.getWorld().setThundering(false);
            } else if(signBlock.getLine(2).equalsIgnoreCase("rain")) {
               signBlock.getWorld().setStorm(true);
               signBlock.getWorld().setThundering(false);
            } else if(signBlock.getLine(2).equalsIgnoreCase("storm")) {
               signBlock.getWorld().setStorm(true);
               signBlock.getWorld().setThundering(true);
            }

            this.switchLever(Lever.BACK, signBlock, true);
         } else {
            this.switchLever(Lever.BACK, signBlock, false);
         }

      }
   }
}
