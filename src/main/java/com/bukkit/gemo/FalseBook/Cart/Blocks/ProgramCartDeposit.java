package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.FalseBook.Cart.FalseBookMinecart;
import com.bukkit.gemo.FalseBook.Cart.utils.ItemHandler;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.InventoryUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class ProgramCartDeposit implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.programcartdeposit")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build programmed Depositblocks.");
         return false;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Programmed depositblock created.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block)) {
         if(!(cart instanceof StorageMinecart)) {
            return false;
         } else {
            StorageMinecart storage = (StorageMinecart)cart;
            if(InventoryUtils.countGeneralStackedFreeSpace(storage.getInventory()) < 0) {
               return false;
            } else {
               FalseBookMinecart FBCart = CartHandler.getFalseBookMinecart(cart);
               if(!FBCart.isProgrammed()) {
                  return false;
               } else {
                  Location loc = signBlock.getLocation();
                  World w = loc.getWorld();
                  int x = loc.getBlockX();
                  int y = loc.getBlockY();
                  int z = loc.getBlockZ();
                  ArrayList chests = BlockUtils.getAdjacentChests(w, x, y + 2, z);
                  BlockUtils.addAdjacentChests(chests, w, x, y + 1, z);
                  BlockUtils.addAdjacentChests(chests, w, x, y, z);
                  ArrayList dispensers = BlockUtils.getAdjacentDispenser(w, x, y + 2, z);
                  BlockUtils.addAdjacentDispenser(dispensers, w, x, y + 1, z);
                  BlockUtils.addAdjacentDispenser(dispensers, w, x, y, z);
                  w = null;
                  loc = null;
                  if(chests.size() < 1 && dispensers.size() < 1) {
                     return false;
                  } else {
                     ArrayList list = FBCart.getProgrammedItems();
                     String storageOwner = FBCart.getOwner();

                     int j;
                     int i;
                     for(j = 0; j < list.size(); ++j) {
                        for(i = 0; i < chests.size(); ++i) {
                           if(LWCProtection.getProtectionOwner(((Chest)chests.get(i)).getBlock()).equalsIgnoreCase(storageOwner)) {
                              DepositItems((ContainerBlock)chests.get(i), ((Chest)chests.get(i)).getBlock(), storage, (FBItemType)list.get(j), false);
                              if(InventoryUtils.countGeneralStackedFreeSpace(((StorageMinecart)cart).getInventory()) < 1) {
                                 return true;
                              }
                           }
                        }
                     }

                     for(j = 0; j < list.size(); ++j) {
                        for(i = 0; i < dispensers.size(); ++i) {
                           if(LWCProtection.getProtectionOwner(((Dispenser)dispensers.get(i)).getBlock()).equalsIgnoreCase(storageOwner)) {
                              DepositItems((ContainerBlock)dispensers.get(i), ((Dispenser)dispensers.get(i)).getBlock(), storage, (FBItemType)list.get(j), false);
                              if(InventoryUtils.countGeneralStackedFreeSpace(((StorageMinecart)cart).getInventory()) < 1) {
                                 return true;
                              }
                           }
                        }
                     }

                     list.clear();
                     chests.clear();
                     dispensers.clear();
                     list = null;
                     chests = null;
                     dispensers = null;
                     return true;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   private static int DepositItems(ContainerBlock container, Block block, StorageMinecart storage, FBItemType itemType, boolean isDoubleChest) {
      if(itemType.getAmount() == 0) {
         return 0;
      } else {
         for(int dChest = 0; dChest < container.getInventory().getSize(); ++dChest) {
            ItemStack itemStack = container.getInventory().getItem(dChest);
            if(itemStack != null && itemStack.getTypeId() != Material.AIR.getId() && itemStack.getTypeId() == itemType.getItemID() && (itemType.usesWildcart() || itemStack.getDurability() == itemType.getItemData())) {
               int wantToTransfer = itemStack.getAmount();
               if(wantToTransfer > 0) {
                  if(itemType.getAmount() == -1) {
                     ItemHandler.transferItem(itemStack, container.getInventory(), storage.getInventory(), dChest);
                  } else if(itemType.getAmount() > 0) {
                     itemType.setAmount(ItemHandler.transferItemWithAmount(itemStack, container.getInventory(), storage.getInventory(), dChest, itemType.getAmount()));
                  }
               }
            }
         }

         if(block.getTypeId() == Material.CHEST.getId() && !isDoubleChest) {
            Chest var8 = BlockUtils.isDoubleChest(block);
            if(var8 != null) {
               DepositItems(var8, var8.getBlock(), storage, itemType, true);
            }
         }

         return 0;
      }
   }
}
