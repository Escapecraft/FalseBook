package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1281 extends BaseIC {

   public MC1281() {
      this.ICName = "IS IT RAINY";
      this.ICNumber = "[MC1281]";
      this.setICGroup(ICGroup.DETECTION);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output: High if it is raining, but not thundering", "", "");
      this.ICDescription = "The MC1281 outputs high if it is raining, but not thundering, when the input (the \"clock\") goes from low to high.<br /><br />The <a href=\"MC0281.html\">MC0281</a> is the selftriggered version.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         this.switchLever(Lever.BACK, signBlock, signBlock.getWorld().hasStorm());
      }

   }
}
