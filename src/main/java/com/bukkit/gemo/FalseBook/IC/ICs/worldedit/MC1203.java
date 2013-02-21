package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1203 extends BaseIC {

   public MC1203() {
      this.ICName = "ZEUS BOLT";
      this.ICNumber = "[MC1203]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("", "");
      this.ICDescription = "The MC1203 generates a lighting bolt on the first free block above the block behind the IC sign when the input (the \"clock\") goes from low to high. ";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         Location loc = getICBlock(signBlock);
         int maxY = Math.min(loc.getWorld().getMaxHeight() - 1, loc.getBlockY() + 15);

         for(int y = 1; y <= maxY - loc.getBlockY(); ++y) {
            if(BlockUtils.canPassThrough(loc.getBlock().getRelative(0, y, 0).getTypeId())) {
               signBlock.getWorld().strikeLightning(loc.getBlock().getRelative(0, y, 0).getLocation());
               this.switchLever(Lever.BACK, signBlock, true);
               return;
            }
         }
      } else {
         this.switchLever(Lever.BACK, signBlock, false);
      }

   }
}
