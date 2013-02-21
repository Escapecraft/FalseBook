package com.bukkit.gemo.FalseBook.IC.ICs;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.utils.ICUtils;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class SelftriggeredBaseIC extends BaseIC {

   private int TypeID = -1;
   protected boolean oldStatus = false;
   private boolean executeOnTick = true;


   public SelftriggeredBaseIC() {}

   public SelftriggeredBaseIC(FalseBookICCore plugin) {
      super(plugin);
   }

   public SelftriggeredBaseIC(FalseBookICCore plugin, Location location) {
      super(plugin);
   }

   public SelftriggeredBaseIC(FalseBookICCore plugin, SignChangeEvent event) {
      super(plugin);
      if(!event.getBlock().getType().equals(Material.WALL_SIGN)) {
         SignUtils.cancelSignCreation(event, "There was an internal error while creating this IC.");
      } else {
         this.signBlock = (Sign)event.getBlock().getState();
      }
   }

   public void onImport() {
      super.onImport();
   }

   public boolean initIC(FalseBookICCore plugin, Location location) {
      this.core = plugin;
      location.getChunk().load(true);
      if(!location.getBlock().getType().equals(Material.WALL_SIGN)) {
         return false;
      } else {
         this.signBlock = (Sign)location.getBlock().getState();
         if(!this.signBlock.getLine(1).equalsIgnoreCase(this.ICNumber)) {
            return false;
         } else {
            this.oldStatus = ICUtils.isLeverActive(this.signBlock);
            return true;
         }
      }
   }

   public void checkCreation(SignChangeEvent event) {
      super.checkCreation(event);
      this.signBlock = (Sign)event.getBlock().getState();
   }

   public void Execute() {}

   public boolean onLoad(String[] lines) {
      return true;
   }

   public int getTypeID() {
      return this.TypeID;
   }

   protected void setTypeID(int typeID) {
      this.TypeID = typeID;
   }

   public boolean isExecuteOnTick() {
      return this.executeOnTick;
   }

   protected void setExecuteOnTick(boolean executeOnTick) {
      this.executeOnTick = executeOnTick;
   }
}
