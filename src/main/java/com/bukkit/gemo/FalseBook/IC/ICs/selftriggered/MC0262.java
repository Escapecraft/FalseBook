package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;

public class MC0262 extends SelftriggeredBaseIC {

   private Block myBlock = null;
   private boolean result;
   private int minLight = 15;


   public MC0262() {
      this.setTypeID(9);
      this.ICName = "LIGHT SENSOR";
      this.ICNumber = "[MC0262]";
      this.setICGroup(ICGroup.SELFTRIGGERED);
      this.chipState = new BaseChip(false, false, false, "", "", "");
      this.chipState.setOutputs("Output: High if the threshold is reached", "", "");
      this.chipState.setLines("Minimum light level, 0 to 15.", "");
      this.ICDescription = "The MC0262 checks to see if the light level of the block above the block behind the IC sign is greater than or equal to a configurable threshold.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(3, "");
      if(event.getLine(2).length() < 1) {
         event.setLine(2, "15");
      }

      String minLight = event.getLine(2);

      try {
         if(minLight.length() > 0) {
            Integer.parseInt(minLight);
         }

      } catch (NumberFormatException var4) {
         SignUtils.cancelSignCreation(event, "The third line must indicate the minimum light level.");
      }
   }

   public boolean onLoad(String[] lines) {
      try {
         this.myBlock = getICBlock(this.signBlock).getBlock().getRelative(0, Integer.valueOf(lines[2]).intValue(), 0);
         return true;
      } catch (Exception var3) {
         this.myBlock = null;
         return false;
      }
   }

   public void Execute() {
      this.result = this.myBlock.getLightLevel() >= this.minLight;
      if(this.result != this.oldStatus) {
         this.oldStatus = this.result;
         this.switchLever(Lever.BACK, this.signBlock, this.result);
      }

   }
}
