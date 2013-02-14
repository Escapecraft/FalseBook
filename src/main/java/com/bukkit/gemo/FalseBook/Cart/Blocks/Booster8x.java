package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class Booster8x implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.booster8x")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build Booster8x-Blocks.");
         return false;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "8x BoosterBlock created.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         CartHandler.getFalseBookMinecart(cart).setConstantSpeedMode(false);
         Vector nowSpeed = cart.getVelocity();
         nowSpeed.setX(nowSpeed.getX() * 8.0D);
         nowSpeed.setZ(nowSpeed.getZ() * 8.0D);
         if(nowSpeed.getX() < 0.0D && nowSpeed.getX() < -settings.getMaxSpeed()) {
            nowSpeed.setX(-settings.getMaxSpeed());
         }

         if(nowSpeed.getX() > 0.0D && nowSpeed.getX() > settings.getMaxSpeed()) {
            nowSpeed.setX(settings.getMaxSpeed());
         }

         if(nowSpeed.getZ() < 0.0D && nowSpeed.getZ() < -settings.getMaxSpeed()) {
            nowSpeed.setZ(-settings.getMaxSpeed());
         }

         if(nowSpeed.getZ() > 0.0D && nowSpeed.getZ() > settings.getMaxSpeed()) {
            nowSpeed.setZ(settings.getMaxSpeed());
         }

         cart.setMaxSpeed(settings.getMaxSpeed());
         cart.setVelocity(nowSpeed.clone());
         return true;
      } else {
         return false;
      }
   }
}
