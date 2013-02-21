package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1282 extends BaseIC {

   public MC1282() {
      this.ICName = "IS IT STORMY";
      this.ICNumber = "[MC1282]";
      this.setICGroup(ICGroup.DETECTION);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output: High if it is raining and thundering", "", "");
      this.ICDescription = "The MC1282 outputs high if it is raining and thundering when the input (the \"clock\") goes from low to high.<br /><br />The <a href=\"MC0282.html\">MC0282</a> is the selftriggered version.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         this.switchLever(Lever.BACK, signBlock, signBlock.getWorld().hasStorm() && signBlock.getWorld().isThundering());
      }

   }
}
