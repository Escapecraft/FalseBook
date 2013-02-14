package com.bukkit.gemo.FalseBook.Cart.Blocks;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class Eject implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(event.getLine(1).equalsIgnoreCase("[Eject]")) {
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.eject")) {
            SignUtils.cancelSignCreation(event, "You are not allowed to build ejectsigns.");
            return false;
         }

         event.setLine(1, "[Eject]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Ejectsign created.");
      }

      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.eject")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build Eject-Blocks.");
         return false;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "EjectBlock created.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         if(signBlock.getTypeId() != Material.SIGN_POST.getId()) {
            return false;
         } else {
            Sign sign = (Sign)signBlock.getState();
            if(!sign.getLine(1).equalsIgnoreCase("[Eject]")) {
               return false;
            } else if(cart.getPassenger() != null) {
               Entity e = cart.getPassenger();
               byte xNew = 0;
               byte zNew = 0;
               if(sign.getRawData() == 0) {
                  zNew = -1;
               } else if(sign.getRawData() == 4) {
                  xNew = 1;
               } else if(sign.getRawData() == 8) {
                  zNew = 1;
               } else if(sign.getRawData() == 12) {
                  xNew = -1;
               }

               Location loc = e.getLocation().clone();
               loc.setX((double)(signBlock.getX() + xNew) + 0.5D);
               loc.setZ((double)(signBlock.getZ() + zNew) + 0.5D);
               int maxY = Math.min(signBlock.getWorld().getMaxHeight() - 1, loc.getBlockY() + 10);

               for(int y = loc.getBlockY(); y <= maxY; ++y) {
                  if(BlockUtils.canPassThrough(sign.getWorld().getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).getTypeId()) && BlockUtils.canPassThrough(sign.getWorld().getBlockAt(loc.getBlockX(), y + 1, loc.getBlockZ()).getTypeId())) {
                     cart.eject();
                     loc.setY((double)y);
                     e.teleport(loc);
                     return true;
                  }
               }

               if(e instanceof Player) {
                  ChatUtils.printError((Player)e, "[FB-Cart]", "There is not enough space to get ejected!");
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }
}
