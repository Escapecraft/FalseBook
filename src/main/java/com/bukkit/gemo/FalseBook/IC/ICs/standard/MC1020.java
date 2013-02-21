package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import java.util.Random;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1020 extends BaseIC {

   Random rGen;


   public MC1020() {
      this.ICName = "RANDOM BIT";
      this.ICNumber = "[MC1020]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Random bit", "", "");
      this.chipState.setLines("", "FORCE:LOW OR FORCE:HIGH");
      this.ICDescription = "The MC1020 generates a random state whenever the input (the \"clock\") goes from low to high. If line 4 is set, the output will be resetted when the input goes from high to low.<br /><br />The <a href=\"MC0020.html\">MC0020</a> is the selftriggered version.";
      this.rGen = new Random();
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      if(event.getLine(3).equalsIgnoreCase("force:high")) {
         event.setLine(3, "FORCE:HIGH");
      } else if(event.getLine(3).equalsIgnoreCase("force:low")) {
         event.setLine(3, "FORCE:LOW");
      } else {
         event.setLine(3, "");
      }

   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         this.switchLever(Lever.BACK, signBlock, this.rGen.nextBoolean());
      }

      if(currentInputs.isInputOneLow() && previousInputs.isInputOneHigh() && (signBlock.getLine(3).equalsIgnoreCase("force:high") || signBlock.getLine(3).equalsIgnoreCase("force:low"))) {
         boolean resetState = false;
         if(signBlock.getLine(3).equalsIgnoreCase("force:high")) {
            resetState = true;
         }

         this.switchLever(Lever.BACK, signBlock, resetState);
      }

   }
}
