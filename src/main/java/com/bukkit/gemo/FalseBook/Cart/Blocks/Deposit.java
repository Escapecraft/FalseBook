package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.utils.ItemHandler;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.InventoryUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.SignUtils;
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
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class Deposit {

   public static boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.collectanddeposit")) {
         SignUtils.cancelSignCreation(event, "You are not allowed to build depositsigns.");
         return false;
      } else {
         event.setLine(1, "[Deposit]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Depositsign created.");
         return true;
      }
   }

   public static void Execute(Minecart cart, Sign sign) {
      StorageMinecart storage = (StorageMinecart)cart;
      if(InventoryUtils.countGeneralStackedFreeSpace(storage.getInventory()) >= 0) {
         String[] lines = new String[]{sign.getLine(2), sign.getLine(3)};
         ArrayList list = SignUtils.parseLinesToItemList(lines, "-", false);
         if(list.size() >= 1) {
            Location loc = sign.getBlock().getLocation();
            World w = loc.getWorld();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            ArrayList dispensers = BlockUtils.getAdjacentDispenser(w, x, y + 2, z);
            BlockUtils.addAdjacentDispenser(dispensers, w, x, y + 1, z);
            BlockUtils.addAdjacentDispenser(dispensers, w, x, y, z);
            ArrayList chests = BlockUtils.getAdjacentChests(w, x, y + 2, z);
            BlockUtils.addAdjacentChests(chests, w, x, y + 1, z);
            BlockUtils.addAdjacentChests(chests, w, x, y, z);
            w = null;
            loc = null;

            int j;
            int i;
            for(j = 0; j < list.size(); ++j) {
               if(((FBItemType)list.get(j)).getAmount() > -1) {
                  ((FBItemType)list.get(j)).setAmount(((FBItemType)list.get(j)).getAmount() - InventoryUtils.countItemInInventory(storage.getInventory(), (FBItemType)list.get(j)));
                  if(((FBItemType)list.get(j)).getAmount() < 0) {
                     ((FBItemType)list.get(j)).setAmount(0);
                  }
               }

               for(i = 0; i < chests.size(); ++i) {
                  if(((FBItemType)list.get(j)).getAmount() != 0 && LWCProtection.protectionsAreEqual(((Chest)chests.get(i)).getBlock(), sign.getBlock())) {
                     DepositItems((ContainerBlock)chests.get(i), ((Chest)chests.get(i)).getBlock(), storage, (FBItemType)list.get(j), false);
                  }
               }
            }

            for(j = 0; j < list.size(); ++j) {
               if(((FBItemType)list.get(j)).getAmount() > -1) {
                  ((FBItemType)list.get(j)).setAmount(((FBItemType)list.get(j)).getAmount() - InventoryUtils.countItemInInventory(storage.getInventory(), (FBItemType)list.get(j)));
                  if(((FBItemType)list.get(j)).getAmount() < 0) {
                     ((FBItemType)list.get(j)).setAmount(0);
                  }
               }

               for(i = 0; i < dispensers.size(); ++i) {
                  if(((FBItemType)list.get(j)).getAmount() != 0 && LWCProtection.protectionsAreEqual(((Dispenser)dispensers.get(i)).getBlock(), sign.getBlock())) {
                     DepositItems((ContainerBlock)dispensers.get(i), ((Dispenser)dispensers.get(i)).getBlock(), storage, (FBItemType)list.get(j), false);
                  }
               }
            }

            list.clear();
            chests.clear();
            dispensers.clear();
            list = null;
            chests = null;
            dispensers = null;
         }
      }
   }

   private static int DepositItems(ContainerBlock container, Block block, StorageMinecart storage, FBItemType itemType, boolean isDoubleChest) {
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
