package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1280 extends BaseIC {

   public MC1280() {
      this.ICName = "IS IT SUNNY";
      this.ICNumber = "[MC1280]";
      this.setICGroup(ICGroup.DETECTION);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output: High if it is sunny", "", "");
      this.ICDescription = "The MC1280 outputs high if it is sunny when the input (the \"clock\") goes from low to high.<br /><br />The <a href=\"MC0280.html\">MC0280</a> is the selftriggered version.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         this.switchLever(Lever.BACK, signBlock, !signBlock.getWorld().hasStorm() && !signBlock.getWorld().isThundering());
      }

   }
}
