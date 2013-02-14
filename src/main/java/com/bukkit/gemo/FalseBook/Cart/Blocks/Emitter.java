package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class Emitter implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(event.getLine(0).equalsIgnoreCase("[Print]")) {
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.emitter")) {
            SignUtils.cancelSignCreation(event, "You are not allowed to build emittersigns.");
            return false;
         }

         event.setLine(0, "[Print]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Emittersign created.");
      }

      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.emitter")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build Emitter-Blocks.");
         return false;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "EmitterBlock created.");
         ChatUtils.printLine(player, ChatColor.GRAY, "Place a sign with [Print] in Line 1 and the message under it.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         if(signBlock.getTypeId() != Material.SIGN_POST.getId()) {
            return false;
         } else {
            Sign sign = (Sign)signBlock.getState();
            if(!sign.getLine(0).equalsIgnoreCase("[Print]")) {
               return false;
            } else {
               if(cart.getPassenger() != null && cart.getPassenger() instanceof Player) {
                  String[] lines = ((Sign)signBlock.getState()).getLines();
                  String txt = "";

                  for(int i = 1; i < 4; ++i) {
                     txt = txt + lines[i];
                  }

                  if(txt.length() > 0) {
                     ChatUtils.printInfo((Player)cart.getPassenger(), ">", ChatColor.GRAY, txt);
                     return true;
                  }
               }

               return false;
            }
         }
      } else {
         return false;
      }
   }
}
