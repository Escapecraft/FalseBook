package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1260 extends BaseIC {

   public MC1260() {
      this.ICName = "WATER SENSOR";
      this.ICNumber = "[MC1260]";
      this.setICGroup(ICGroup.DETECTION);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output: High if water is present", "", "");
      this.chipState.setLines("Y offset, with 0 being the IC block. Leave blank to default to the block below. ", "");
      this.ICDescription = "The MC1260 checks for the presence of water relative to the block behind the IC sign whenever the input goes from low to high. By default it checks the block directly underneath but this can be changed.<br /><br />The <a href=\"MC0260.html\">MC0260</a> is the selftriggered version.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(3, "");
      if(event.getLine(2).length() < 1) {
         event.setLine(2, "-1");
      }

      if(!Parser.isInteger(event.getLine(2))) {
         SignUtils.cancelSignCreation(event, "The third line must be a number or be blank.");
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         if(!Parser.isInteger(signBlock.getLine(2))) {
            return;
         }

         int offSet = Parser.getInteger(signBlock.getLine(2), -1);
         Block block = getICBlock(signBlock).getBlock().getRelative(0, offSet, 0);
         if(block.getTypeId() != 8 && block.getTypeId() != 9) {
            this.switchLever(Lever.BACK, signBlock, false);
         } else {
            this.switchLever(Lever.BACK, signBlock, true);
         }

         block = null;
      }

   }
}
