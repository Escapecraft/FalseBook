package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.FalseBook.Cart.Blocks.BenchOut;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Burn;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Collect;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Craft;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Deposit;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class MultiBlock implements CartMechanic {

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.collectanddeposit") && !UtilPermissions.playerCanUseCommand(player, "falsebook.cart.autocraft") && !UtilPermissions.playerCanUseCommand(player, "falsebook.cart.autobenchout") && !UtilPermissions.playerCanUseCommand(player, "falsebook.cart.autoburn")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build Collect/Deposit-Blocks.");
         return true;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Collect/Deposit-Block created.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!(cart instanceof StorageMinecart)) {
         return false;
      } else if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         if(signBlock.getTypeId() != Material.SIGN_POST.getId()) {
            return false;
         } else {
            Sign sign = (Sign)signBlock.getState();
            if(sign.getLine(1).equalsIgnoreCase("[Collect]")) {
               Collect.Execute(cart, sign);
               return true;
            } else if(sign.getLine(1).equalsIgnoreCase("[Deposit]")) {
               Deposit.Execute(cart, sign);
               return true;
            } else if(sign.getLine(0).equalsIgnoreCase("[BenchOut]")) {
               BenchOut.Execute(cart, sign);
               return true;
            } else if(sign.getLine(1).equalsIgnoreCase("[Burn]")) {
               Burn.Execute(cart, sign);
               return true;
            } else if(sign.getLine(1).equalsIgnoreCase("[Craft]")) {
               Craft.Execute(cart, sign);
               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      return event.getLine(1).equalsIgnoreCase("[Collect]")?Collect.checkSignCreation(event, player, sign):(event.getLine(1).equalsIgnoreCase("[Deposit]")?Deposit.checkSignCreation(event, player, sign):(event.getLine(0).equalsIgnoreCase("[BenchOut]")?BenchOut.checkSignCreation(event, player, sign):(event.getLine(1).equalsIgnoreCase("[Burn]")?Burn.checkSignCreation(event, player, sign):(event.getLine(1).equalsIgnoreCase("[Craft]")?Craft.checkSignCreation(event, player, sign):true))));
   }
}
