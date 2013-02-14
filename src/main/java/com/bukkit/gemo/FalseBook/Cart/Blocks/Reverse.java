package com.bukkit.gemo.FalseBook.Cart.Blocks;

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

public class Reverse implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.reverse")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build Reverse-Blocks.");
         return false;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "ReverseBlock created.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         return false;
      } else {
         Vector nowSpeed = cart.getVelocity();
         nowSpeed.setX(-nowSpeed.getX());
         nowSpeed.setZ(-nowSpeed.getZ());
         cart.setVelocity(nowSpeed.clone());
         return true;
      }
   }
}
