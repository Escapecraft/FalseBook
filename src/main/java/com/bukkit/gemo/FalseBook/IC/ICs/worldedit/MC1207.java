package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1207 extends BaseIC {

   public MC1207() {
      this.ICName = "SET BLOCK";
      this.ICNumber = "[MC1207]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("BlockIDOn[:SubID][-BlockIDOff[:SubID]] (Examples: \'wool:15-stone\' or \'grass\' or \'dirt-44:2\')", "Y offset, with 0 being the IC block.");
      this.ICDescription = "The MC1207 sets the specified block to the specified blocktype whenever the input (the \"clock\") changes.";
   }

   public void checkCreation(SignChangeEvent event) {
      ArrayList itemList = SignUtils.parseLineToItemListWithSize(event.getLine(2), "-", true, 1, 2);
      if(itemList == null) {
         SignUtils.cancelSignCreation(event, "Line 3 is not valid. Usage: BlockIDOn[:SubID][-BlockIDOff[:SubID]]");
      } else {
         Iterator var4 = itemList.iterator();

         while(var4.hasNext()) {
            FBItemType item = (FBItemType)var4.next();
            if(!BlockUtils.isValidBlock(item.getItemID())) {
               SignUtils.cancelSignCreation(event, "\'" + Material.getMaterial(item.getItemID()).name() + "\' is not a block.");
               return;
            }
         }

         if(!Parser.isInteger(event.getLine(3))) {
            SignUtils.cancelSignCreation(event, "Line 4 must be a number.");
         }
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      ArrayList itemList = SignUtils.parseLineToItemListWithSize(signBlock.getLine(2), "-", true, 1, 2);
      if(itemList != null) {
         Iterator var6 = itemList.iterator();

         while(var6.hasNext()) {
            FBItemType newBlockLoc = (FBItemType)var6.next();
            if(!BlockUtils.isValidBlock(newBlockLoc.getItemID())) {
               return;
            }
         }

         if(itemList.size() == 1) {
            itemList.add(new FBItemType(0));
         }

         if(Parser.isInteger(signBlock.getLine(3))) {
            Location newBlockLoc1 = getICBlock(signBlock).getBlock().getRelative(0, Parser.getInteger(signBlock.getLine(3), 1), 0).getLocation();
            if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
               newBlockLoc1.getBlock().setTypeIdAndData(((FBItemType)itemList.get(0)).getItemID(), ((FBItemType)itemList.get(0)).getItemDataAsByte(), true);
               this.switchLever(Lever.BACK, signBlock, true);
            } else {
               newBlockLoc1.getBlock().setTypeIdAndData(((FBItemType)itemList.get(1)).getItemID(), ((FBItemType)itemList.get(1)).getItemDataAsByte(), true);
               this.switchLever(Lever.BACK, signBlock, false);
            }

            itemList.clear();
            itemList = null;
         }
      }
   }
}
