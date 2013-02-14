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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class Teleport implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(event.getLine(1).equalsIgnoreCase("[Teleport]")) {
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.teleport")) {
            SignUtils.cancelSignCreation(event, "You are not allowed to build teleportsigns.");
            return false;
         }

         if(event.getBlock().getData() != 0 && event.getBlock().getData() != 4 && event.getBlock().getData() != 8 && event.getBlock().getData() != 12) {
            SignUtils.cancelSignCreation(event, "Teleportsigns signs may only be created at specific angles (90 degrees).");
            return false;
         }

         if(!event.getLine(2).equalsIgnoreCase("UP") && !event.getLine(2).equalsIgnoreCase("DOWN")) {
            SignUtils.cancelSignCreation(event, "Line 3 must be \'UP\' OR \'DOWN\'.");
            return false;
         }

         event.setLine(1, "[Teleport]");
         if(event.getLine(2).equalsIgnoreCase("UP")) {
            event.setLine(2, "UP");
         } else {
            event.setLine(2, "DOWN");
         }

         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Teleportsign created.");
      }

      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.teleport")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build Teleport-Blocks.");
         return false;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "TeleportBlock created.");
         return true;
      }
   }

   private Sign getSignUpLocation(Block signBlock, CartWorldSettings settings) {
      Location loc = signBlock.getLocation();
      World world = loc.getWorld();

      for(int y = loc.getBlockY() + 2; y < 126; ++y) {
         if(BlockUtils.getRawTypeID(world, loc.getBlockX(), y, loc.getBlockZ()) == Material.SIGN_POST.getId()) {
            int signData = BlockUtils.getRawSubID(world, loc.getBlockX(), y, loc.getBlockZ());
            if(signData == 0 || signData == 4 || signData == 8 || signData == 12) {
               int railID = BlockUtils.getRawTypeID(world, loc.getBlockX(), y + 2, loc.getBlockZ());
               if(railID == Material.RAILS.getId() || railID == Material.DETECTOR_RAIL.getId() || railID == Material.POWERED_RAIL.getId()) {
                  int blockID = BlockUtils.getRawTypeID(world, loc.getBlockX(), y + 1, loc.getBlockZ());
                  int blockData = BlockUtils.getRawSubID(world, loc.getBlockX(), y + 1, loc.getBlockZ());
                  if(blockID == settings.getTeleportBlock() && blockData == settings.getTeleportBlockValue()) {
                     Sign sign = (Sign)world.getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).getState();
                     if(sign.getLine(1).equalsIgnoreCase("[Teleport]") && sign.getLine(2).equalsIgnoreCase("DOWN")) {
                        return (Sign)world.getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).getState();
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   private Sign getSignDownLocation(Block signBlock, CartWorldSettings settings) {
      Location loc = signBlock.getLocation();
      World world = loc.getWorld();

      for(int y = loc.getBlockY() - 3; y > 0; --y) {
         if(BlockUtils.getRawTypeID(world, loc.getBlockX(), y, loc.getBlockZ()) == Material.SIGN_POST.getId()) {
            int signData = BlockUtils.getRawSubID(world, loc.getBlockX(), y, loc.getBlockZ());
            if(signData == 0 || signData == 4 || signData == 8 || signData == 12) {
               int railID = BlockUtils.getRawTypeID(world, loc.getBlockX(), y + 2, loc.getBlockZ());
               if(railID == Material.RAILS.getId() || railID == Material.DETECTOR_RAIL.getId() || railID == Material.POWERED_RAIL.getId()) {
                  int blockID = BlockUtils.getRawTypeID(world, loc.getBlockX(), y + 1, loc.getBlockZ());
                  int blockData = BlockUtils.getRawSubID(world, loc.getBlockX(), y + 1, loc.getBlockZ());
                  if(blockID == settings.getTeleportBlock() && blockData == settings.getTeleportBlockValue()) {
                     Sign sign = (Sign)world.getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).getState();
                     if(sign.getLine(1).equalsIgnoreCase("[Teleport]") && sign.getLine(2).equalsIgnoreCase("UP")) {
                        return (Sign)world.getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).getState();
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         if(signBlock.getTypeId() != Material.SIGN_POST.getId()) {
            return false;
         } else {
            Sign sign = (Sign)signBlock.getState();
            if(!sign.getLine(1).equalsIgnoreCase("[Teleport]")) {
               return false;
            } else if(sign.getRawData() != 0 && sign.getRawData() != 4 && sign.getRawData() != 8 && sign.getRawData() != 12) {
               return false;
            } else if(!sign.getLine(2).equalsIgnoreCase("UP") && !sign.getLine(2).equalsIgnoreCase("DOWN")) {
               return true;
            } else {
               boolean teleportUp = true;
               if(sign.getLine(2).equalsIgnoreCase("DOWN")) {
                  teleportUp = false;
               }

               Sign otherSign = null;
               if(teleportUp) {
                  otherSign = this.getSignUpLocation(signBlock, settings);
               } else {
                  otherSign = this.getSignDownLocation(signBlock, settings);
               }

               if(otherSign == null) {
                  return true;
               } else {
                  Location otherLoc = otherSign.getBlock().getRelative(0, 2, 0).getLocation();
                  double tpX = (double)otherLoc.getBlockX() + 0.5D;
                  double tpY = (double)otherLoc.getBlockY();
                  double tpZ = (double)otherLoc.getBlockZ() + 0.5D;
                  cart.teleport(new Location(otherLoc.getWorld(), tpX, tpY, tpZ, 0.0F, 0.0F));
                  Vector nowVelocity = this.getNewVelocity(cart.getVelocity().clone(), otherSign);
                  cart.setVelocity(nowVelocity);
                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   private Vector getNewVelocity(Vector nowVelocity, Sign otherSign) {
      double oldSpeedX = Math.abs(nowVelocity.getX());
      double oldSpeedZ = Math.abs(nowVelocity.getZ());
      double speed = Math.max(oldSpeedX, oldSpeedZ);
      int direction = SignUtils.getDirection(otherSign);
      switch(direction) {
      case 1:
         nowVelocity.setZ(-speed);
         nowVelocity.setX(0);
         break;
      case 2:
         nowVelocity.setX(-speed);
         nowVelocity.setZ(0);
         break;
      case 3:
         nowVelocity.setZ(speed);
         nowVelocity.setX(0);
         break;
      case 4:
         nowVelocity.setX(speed);
         nowVelocity.setZ(0);
      }

      return nowVelocity.clone();
   }
}
