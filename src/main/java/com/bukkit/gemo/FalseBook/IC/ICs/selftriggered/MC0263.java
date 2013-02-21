package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;

public class MC0263 extends SelftriggeredBaseIC {

   private Block myBlock = null;
   private boolean result;
   private FBItemType item = null;


   public MC0263() {
      this.setTypeID(10);
      this.ICName = "BLOCK SENSOR";
      this.ICNumber = "[MC0263]";
      this.setICGroup(ICGroup.SELFTRIGGERED);
      this.chipState = new BaseChip(false, false, false, "", "", "");
      this.chipState.setOutputs("Output: High if the block is present", "", "");
      this.chipState.setLines("Y offset, with 0 being the IC block. Leave blank to default to the block below.", "BlockID[:SubID]");
      this.ICDescription = "The MC0263 checks for the presence of a specified block relative to the block behind the IC sign. By default it checks the block directly underneath but this can be changed.";
   }

   public void checkCreation(SignChangeEvent event) {
      if(event.getLine(2).length() < 1) {
         event.setLine(2, "-1");
      }

      String yOffset = event.getLine(2);

      try {
         if(yOffset.length() > 0) {
            Integer.parseInt(yOffset);
         }
      } catch (NumberFormatException var5) {
         SignUtils.cancelSignCreation(event, "The third line must be a number or be blank.");
         return;
      }

      if(event.getLine(2) == null) {
         SignUtils.cancelSignCreation(event, "Item not found");
      } else {
         String[] split = event.getLine(3).split(":");

         try {
            if(!BlockUtils.isValidBlock(Integer.valueOf(split[0]).intValue())) {
               SignUtils.cancelSignCreation(event, "Block not found");
               return;
            }
         } catch (Exception var6) {
            if(!BlockUtils.isValidBlock(BlockUtils.getItemIDFromName(event.getLine(3)))) {
               SignUtils.cancelSignCreation(event, "Block not found");
               return;
            }
         }

      }
   }

   public boolean onLoad(String[] lines) {
      try {
         this.myBlock = getICBlock(this.signBlock).getBlock().getRelative(0, Integer.valueOf(lines[2]).intValue(), 0);
         ArrayList e = SignUtils.parseLineToItemListWithSize(lines[3], "-", false, 1, 1);
         if(e != null) {
            this.item = (FBItemType)e.get(0);
            e.clear();
            e = null;
            return true;
         } else {
            return false;
         }
      } catch (Exception var3) {
         this.myBlock = null;
         return false;
      }
   }

   public void Execute() {
      this.result = this.myBlock.getTypeId() == this.item.getItemID() && (this.myBlock.getData() == this.item.getItemData() || this.item.usesWildcart());
      if(this.result != this.oldStatus) {
         this.oldStatus = this.result;
         this.switchLever(Lever.BACK, this.signBlock, this.result);
      }

   }
}
