package com.bukkit.gemo.FalseBook.Block;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicArea;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicBridge;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicDoor;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicGate;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class QueuedExecutionEvent {

   private FalseBookBlockCore plugin;
   private Block block = null;
   private Location redstoneLoc = null;


   public QueuedExecutionEvent(FalseBookBlockCore plugin, Block block, Location redstoneLoc) {
      this.plugin = plugin;
      this.block = block;
      this.redstoneLoc = redstoneLoc;
   }

   public void Execute() {
      if(this.block.getTypeId() == Material.WALL_SIGN.getId() || this.block.getTypeId() == Material.SIGN_POST.getId()) {
         if(!ConfigHandler.isRespectLWCProtections(this.block.getWorld().getName()) || !LWCProtection.getProtection(this.block).equalsIgnoreCase("private")) {
            Sign signBlock = (Sign)this.block.getState();
            String worldName = this.block.getWorld().getName();
            if(signBlock.getLine(1).equalsIgnoreCase("[Bridge]") && ConfigHandler.isBridgeEnabled(worldName)) {
               ((MechanicBridge)this.plugin.getMechanicHandler().getMechanic("BRIDGE")).toggle(signBlock);
            } else if(ConfigHandler.isDoorEnabled(worldName) && (signBlock.getLine(1).equalsIgnoreCase("[Door Up]") || signBlock.getLine(1).equalsIgnoreCase("[Door Down]"))) {
               ((MechanicDoor)this.plugin.getMechanicHandler().getMechanic("DOOR")).toggle(signBlock);
            } else if(!signBlock.getLine(1).equalsIgnoreCase("[Toggle]") && !signBlock.getLine(1).equalsIgnoreCase("[Area]")) {
               if(signBlock.getLine(1).equalsIgnoreCase("[Gate]") || signBlock.getLine(1).equalsIgnoreCase("[DGate]")) {
                  Block thisBlock = SignUtils.getSignAnchor(signBlock).getBlock();

                  try {
                     int e = ConfigHandler.getAllowedGateBlocks(this.block.getWorld().getName()).getValue(0);
                     if(signBlock.getLine(2).length() > 0) {
                        e = Integer.valueOf(signBlock.getLine(2)).intValue();
                     }

                     ((MechanicGate)this.plugin.getMechanicHandler().getMechanic("GATE")).toggle(thisBlock, signBlock.getLine(1).equalsIgnoreCase("[DGate]"), true, BlockUtils.isLow(this.redstoneLoc), e);
                  } catch (Exception var5) {
                     var5.printStackTrace();
                     return;
                  }

                  thisBlock = null;
               }
            } else {
               ((MechanicArea)this.plugin.getMechanicHandler().getMechanic("AREA")).toggle(this.plugin, signBlock, this.block, BlockUtils.isLow(this.redstoneLoc), false);
            }

         }
      }
   }

   public Location getLocation() {
      return this.block.getLocation();
   }
}
