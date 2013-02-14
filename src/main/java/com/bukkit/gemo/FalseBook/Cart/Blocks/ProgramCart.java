package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.FalseBook.Cart.FalseBookMinecart;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class ProgramCart implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(event.getLine(1).equalsIgnoreCase("[ProgramCart]")) {
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.programcart")) {
            SignUtils.cancelSignCreation(event, "You are not allowed to build programsigns.");
            return false;
         }

         event.setLine(1, "[ProgramCart]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Programsign created.");
      }

      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.programcart")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build Programblocks.");
         return true;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Programblocks created.");
         ChatUtils.printLine(player, ChatColor.GRAY, "Wire it up and place a programsign under it.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         return false;
      } else if(signBlock.getTypeId() != Material.SIGN_POST.getId()) {
         return false;
      } else {
         Sign sign = (Sign)signBlock.getState();
         if(!sign.getLine(1).equalsIgnoreCase("[ProgramCart]")) {
            return false;
         } else if(!(cart instanceof StorageMinecart)) {
            return false;
         } else {
            FalseBookMinecart FBCart = CartHandler.getFalseBookMinecart(cart);
            String signOwner = LWCProtection.getProtectionOwner(sign.getBlock());
            if(!FBCart.getOwner().equalsIgnoreCase(signOwner)) {
               return false;
            } else {
               FBItemType item;
               Iterator var12;
               if(sign.getLine(2).equalsIgnoreCase("Multiply")) {
                  if(!Parser.isDouble(sign.getLine(3))) {
                     return false;
                  }

                  double itemList1 = Parser.getDouble(sign.getLine(3));
                  if(itemList1 <= 0.0D) {
                     itemList1 = 1.0D;
                  }

                  var12 = FBCart.getProgrammedItems().iterator();

                  while(var12.hasNext()) {
                     item = (FBItemType)var12.next();
                     if(item.getAmount() != -1) {
                        item.setAmount((int)((double)item.getAmount() * itemList1));
                     }
                  }
               } else {
                  ArrayList itemList11 = SignUtils.parseLineToItemList(sign.getLine(2), "-", false);
                  ArrayList itemList2 = SignUtils.parseLineToItemList(sign.getLine(3), "-", false);
                  if(itemList11 == null || itemList2 == null) {
                     FBCart.clearProgrammedItems();
                     return true;
                  }

                  FBCart.setOwner(signOwner);
                  if(itemList11 != null) {
                     var12 = itemList11.iterator();

                     while(var12.hasNext()) {
                        item = (FBItemType)var12.next();
                        FBCart.addProgrammedItem(item);
                     }
                  }

                  if(itemList2 != null) {
                     var12 = itemList2.iterator();

                     while(var12.hasNext()) {
                        item = (FBItemType)var12.next();
                        FBCart.addProgrammedItem(item);
                     }
                  }
               }

               return true;
            }
         }
      }
   }
}
