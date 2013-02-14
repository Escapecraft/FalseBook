package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class Catcher implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(event.getLine(1).equalsIgnoreCase("[Station]")) {
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.station")) {
            SignUtils.cancelSignCreation(event, "You are not allowed to build stationsigns.");
            return false;
         }

         if(event.getBlock().getData() != 0 && event.getBlock().getData() != 4 && event.getBlock().getData() != 8 && event.getBlock().getData() != 12) {
            SignUtils.cancelSignCreation(event, "Station signs may only be created at specific angles (90 degrees).");
            return false;
         }

         event.setLine(1, "[Station]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Stationsign created.");
      }

      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.station")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build Station-Blocks.");
         return true;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "StationBlock created.");
         ChatUtils.printLine(player, ChatColor.GRAY, "Wire it up and place a station sign under it.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         if(signBlock.getTypeId() != Material.SIGN_POST.getId()) {
            return false;
         } else {
            Sign sign = (Sign)signBlock.getState();
            if(!sign.getLine(1).equalsIgnoreCase("[Station]")) {
               return false;
            } else if(sign.getRawData() != 0 && sign.getRawData() != 4 && sign.getRawData() != 8 && sign.getRawData() != 12) {
               return false;
            } else {
               CartHandler.getFalseBookMinecart(cart).setConstantSpeedMode(false);
               if(cart.getPassenger() != null && cart.getPassenger() instanceof Player && ((Sign)signBlock.getState()).getLine(0).length() > 0) {
                  ChatUtils.printInfo((Player)cart.getPassenger(), "", ChatColor.GRAY, "Current Stop: " + ((Sign)signBlock.getState()).getLine(0));
               }

               this.resetCart(cart, railBlock);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public void resetCart(Minecart cart, Block railBlock) {
      Location loc = railBlock.getLocation();
      loc.setX((double)loc.getBlockX() + 0.5D);
      loc.setZ((double)loc.getBlockZ() + 0.5D);
      cart.teleport(loc);
      cart.setVelocity(new Vector(0, 0, 0));
   }
}
