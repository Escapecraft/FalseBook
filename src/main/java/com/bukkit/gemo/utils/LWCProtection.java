package com.bukkit.gemo.utils;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LWCProtection {

   private static LWC LWC = null;
   private static boolean initFinished = false;


   private static void initLWC() {
      if(!initFinished) {
         Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("LWC");
         if(plugin != null) {
            LWC = ((LWCPlugin)plugin).getLWC();
            if(LWC != null && !plugin.isEnabled()) {
               LWC = null;
            }
         }

         initFinished = true;
      }

   }

   public static boolean canAccess(String playerName, Block block) {
      initLWC();
      if(LWC != null) {
         Protection protection = LWC.findProtection(block);
         if(protection != null) {
            if(protection.typeToString().equalsIgnoreCase("public")) {
               return true;
            }

            if(protection.getOwner().equalsIgnoreCase(playerName)) {
               return true;
            }

            return false;
         }
      }

      return true;
   }

   public static boolean canAccessWithCModify(Player player, Block block) {
      initLWC();
      if(LWC != null) {
         Protection protection = LWC.findProtection(block);
         if(protection != null) {
            return LWC.canAccessProtection(player, block);
         }
      }

      return true;
   }

   public static String getProtectionOwner(Block block) {
      initLWC();
      if(LWC != null) {
         Protection protection = LWC.findProtection(block);
         if(protection != null) {
            return protection.getOwner();
         }
      }

      return "";
   }

   public static String getProtection(Block block) {
      initLWC();
      if(LWC != null) {
         Protection protection = LWC.findProtection(block);
         if(protection != null) {
            return protection.typeToString();
         }
      }

      return "";
   }

   public static boolean protectionsAreEqual(Block block, Block otherBlock) {
      initLWC();
      if(LWC != null) {
         Protection chestProtection = LWC.findProtection(block);
         Protection signProtection = LWC.findProtection(otherBlock);
         boolean hasChestProtection = chestProtection != null;
         boolean hasSignProtection = signProtection != null;
         if(hasChestProtection != hasSignProtection) {
            return false;
         }

         if(hasChestProtection && hasSignProtection) {
            if(!signProtection.getOwner().equalsIgnoreCase(chestProtection.getOwner())) {
               return false;
            }

            if(!signProtection.typeToString().equalsIgnoreCase(chestProtection.typeToString())) {
               return false;
            }
         }
      }

      return true;
   }
}
