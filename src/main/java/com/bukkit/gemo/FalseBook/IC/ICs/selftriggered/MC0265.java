package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MC0265 extends SelftriggeredBaseIC {

   private boolean result;
   private int detectionRange = -1;
   private Vector offsetVector = null;
   private Location blockLoc = null;
   private ArrayList itemList;


   public MC0265() {
      this.setTypeID(ExternalICPackage.getUniqueID("[MC0265]"));
      this.ICName = "ITEM VANISHER";
      this.ICNumber = "[MC0265]";
      this.setICGroup(ICGroup.SELFTRIGGERED);
      this.chipState = new BaseChip(false, false, false, "", "", "");
      this.chipState.setOutputs("Output", "", "");
      this.chipState.setLines("Radius[=OffsetX:OffsetY:OffsetZ] (i.e: 0=0:2:0 will check 2 Blocks above the IC-Block)", "items to remove");
      this.ICDescription = "The MC0265 outputs high if at least one specified item is removed in the given distance around the ic.";
   }

   public void checkCreation(SignChangeEvent event) {
      if(!Parser.isIntegerWithOffset(event.getLine(2))) {
         SignUtils.cancelSignCreation(event, "Line 3 must be a number or a number with a vector.");
      } else {
         int radius = Parser.getIntegerFromOffsetLine(event.getLine(2), 0);
         Vector vector = Parser.getVectorFromOffsetLine(event.getLine(2));
         if(radius < 0) {
            radius = 0;
         }

         if(vector.getBlockX() == 0 && vector.getBlockY() == 0 && vector.getBlockZ() == 0) {
            event.setLine(2, "" + radius);
         } else {
            event.setLine(2, radius + "=" + vector.getBlockX() + ":" + vector.getBlockY() + ":" + vector.getBlockZ());
         }

         if(SignUtils.parseLineToItemListWithSize(event.getLine(3), "-", false, 1, 9999) == null) {
            SignUtils.cancelSignCreation(event, "Please enter at least one item in Line 4.");
         }
      }
   }

   public boolean onLoad(String[] lines) {
      try {
         if(!Parser.isIntegerWithOffset(lines[2])) {
            return false;
         } else {
            this.detectionRange = Parser.getIntegerFromOffsetLine(lines[2], 0);
            if(this.detectionRange < 0) {
               this.detectionRange = 0;
            }

            this.offsetVector = Parser.getVectorFromOffsetLine(lines[2]);
            this.itemList = SignUtils.parseLineToItemListWithSize(lines[3], "-", false, 1, 9999);
            return this.itemList != null;
         }
      } catch (Exception var3) {
         this.detectionRange = -1;
         return false;
      }
   }

   public void Execute() {
      this.result = false;
      this.blockLoc = getICBlock(this.signBlock, this.offsetVector);
      List liste = this.signBlock.getWorld().getEntities();
      ItemStack stack = null;
      Entity ent = null;

      for(int counter = liste.size() - 1; counter >= 0; --counter) {
         ent = (Entity)liste.get(counter);
         if(BlockUtils.isInRange(ent.getLocation(), this.blockLoc, this.detectionRange) && ent instanceof Item) {
            stack = ((Item)ent).getItemStack();

            for(int i = 0; i < this.itemList.size(); ++i) {
               if(stack.getTypeId() == ((FBItemType)this.itemList.get(i)).getItemID() && (((FBItemType)this.itemList.get(i)).usesWildcart() || stack.getDurability() == ((FBItemType)this.itemList.get(i)).getItemData())) {
                  ent.remove();
                  this.result = true;
               }
            }
         }
      }

      liste.clear();
      liste = null;
      this.blockLoc = null;
      if(this.result != this.oldStatus) {
         this.oldStatus = this.result;
         this.switchLever(Lever.BACK, this.signBlock, this.result);
      }

   }
}
