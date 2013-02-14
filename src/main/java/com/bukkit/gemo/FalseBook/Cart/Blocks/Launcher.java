package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartVehicleListener;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Launcher {

   public static ArrayList isCartOnBlock(Block signBlock) {
      ArrayList result = new ArrayList();
      if(!signBlock.getType().equals(Material.SIGN_POST)) {
         result.add(Boolean.valueOf(false));
         result.add((Object)null);
      }

      Entity[] ents = signBlock.getChunk().getEntities();

      for(int i = 0; i < ents.length; ++i) {
         if(ents[i] instanceof Minecart && ((Minecart)ents[i]).getLocation().getBlockX() == signBlock.getX() && ((Minecart)ents[i]).getLocation().getBlockZ() == signBlock.getZ() && ((Minecart)ents[i]).getLocation().getBlockY() == signBlock.getY() + 2) {
            result.add(Boolean.valueOf(true));
            result.add(ents[i]);
            return result;
         }
      }

      result.add(Boolean.valueOf(false));
      result.add((Object)null);
      return result;
   }

   public static ArrayList isPlayerOnBlock(Block signBlock) {
      ArrayList result = new ArrayList();
      if(!signBlock.getType().equals(Material.SIGN_POST)) {
         result.add(Boolean.valueOf(false));
         result.add((Object)null);
      }

      Entity[] ents = signBlock.getChunk().getEntities();

      for(int i = 0; i < ents.length; ++i) {
         if(ents[i] instanceof Player && ((Player)ents[i]).getLocation().getBlockX() == signBlock.getX() && ((Player)ents[i]).getLocation().getBlockZ() == signBlock.getZ() && ((Player)ents[i]).getLocation().getBlockY() == signBlock.getY() + 2) {
            result.add(Boolean.valueOf(true));
            result.add(ents[i]);
            return result;
         }
      }

      result.add(Boolean.valueOf(false));
      result.add((Object)null);
      return result;
   }

   public static void Execute(Minecart cart, Block signBlock) {
      if(signBlock.getType().equals(Material.SIGN_POST)) {
         if(((Sign)signBlock.getState()).getLine(1).equalsIgnoreCase("[Station]")) {
            Sign sign = (Sign)signBlock.getState();
            Vector speed = new Vector();
            speed.setX(0);
            speed.setZ(0);
            CartHandler.getFalseBookMinecart(cart).setConstantSpeedMode(false);
            if(sign.getRawData() == 0) {
               speed.setZ(-FalseBookCartVehicleListener.getLaunchSpeed(cart.getWorld().getName()));
               cart.setVelocity(speed);
            } else if(sign.getRawData() == 4) {
               speed.setX(FalseBookCartVehicleListener.getLaunchSpeed(cart.getWorld().getName()));
               cart.setVelocity(speed);
            } else if(sign.getRawData() == 8) {
               speed.setZ(FalseBookCartVehicleListener.getLaunchSpeed(cart.getWorld().getName()));
               cart.setVelocity(speed);
            } else if(sign.getRawData() == 12) {
               speed.setX(-FalseBookCartVehicleListener.getLaunchSpeed(cart.getWorld().getName()));
               cart.setVelocity(speed);
            }

         }
      }
   }
}
