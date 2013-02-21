package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBBlockType;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1205 extends BaseIC {

   public MC1205() {
      this.ICName = "SET BLOCK ABOVE";
      this.ICNumber = "[MC1205]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("BlockID[:SubID]", "FORCE to set the block even if there is already a block there.");
      this.ICDescription = "The MC1205 sets a block of a specified type two blocks above the block behind the IC sign. <a href=\"MC1206.html\">MC1206</a> is the version of the IC that sets the block below.";
   }

   public void checkCreation(SignChangeEvent event) {
      if(!Parser.isBlock(event.getLine(2))) {
         SignUtils.cancelSignCreation(event, "Item not found.");
      } else {
         FBBlockType item = Parser.getBlock(event.getLine(2));
         if(!BlockUtils.isValidBlock(item.getItemID())) {
            SignUtils.cancelSignCreation(event, "This is not a valid blocktype.");
         } else if(!Parser.isStringOrEmpty(event.getLine(3), "force")) {
            SignUtils.cancelSignCreation(event, "Line 4 must be empty or \'FORCE\'.");
         } else {
            event.setLine(3, event.getLine(3).toUpperCase());
         }
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         if(!Parser.isBlock(signBlock.getLine(2))) {
            return;
         }

         FBBlockType item = Parser.getBlock(signBlock.getLine(2));
         if(!BlockUtils.isValidBlock(item.getItemID())) {
            return;
         }

         if(!Parser.isStringOrEmpty(signBlock.getLine(3), "force")) {
            return;
         }

         boolean force = Parser.isString(signBlock.getLine(3), "force");
         Location newBlockLoc = getICBlock(signBlock).getBlock().getRelative(0, 2, 0).getLocation();
         if(newBlockLoc.getBlock().getType().equals(Material.AIR) || force) {
            newBlockLoc.getBlock().setTypeIdAndData(item.getItemID(), item.getItemDataAsByte(), true);
            this.switchLever(Lever.BACK, signBlock, true);
         }
      } else {
         this.switchLever(Lever.BACK, signBlock, false);
      }

   }
}
