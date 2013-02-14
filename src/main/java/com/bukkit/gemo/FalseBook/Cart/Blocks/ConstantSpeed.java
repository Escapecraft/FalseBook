package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartVehicleListener;
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

public class ConstantSpeed implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.constantspeed")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build ConstantSpeed-Blocks.");
         return false;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "ConstantSpeedBlock created.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         Vector nowSpeed = cart.getVelocity();
         double speed = FalseBookCartVehicleListener.getConstantSpeed(cart.getWorld().getName());
         if(nowSpeed.getX() < 0.0D) {
            nowSpeed.setX(-speed);
         }

         if(nowSpeed.getX() > 0.0D) {
            nowSpeed.setX(speed);
         }

         if(nowSpeed.getZ() < 0.0D) {
            nowSpeed.setZ(-speed);
         }

         if(nowSpeed.getZ() > 0.0D) {
            nowSpeed.setZ(speed);
         }

         cart.setVelocity(nowSpeed.clone());
         CartHandler.getFalseBookMinecart(cart).setConstantSpeedMode(true);
         return true;
      } else {
         return false;
      }
   }
}
