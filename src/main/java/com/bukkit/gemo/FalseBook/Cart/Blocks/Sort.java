package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartCore;
import com.bukkit.gemo.FalseBook.Cart.FalseBookMinecart;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.InventoryUtils;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;

public class Sort implements CartMechanic {

   public boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(event.getLine(1).equalsIgnoreCase("[Sort]")) {
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.sort")) {
            SignUtils.cancelSignCreation(event, "You are not allowed to build sortsigns.");
            return false;
         }

         if(event.getBlock().getData() != 0 && event.getBlock().getData() != 4 && event.getBlock().getData() != 8 && event.getBlock().getData() != 12) {
            SignUtils.cancelSignCreation(event, "Sortsigns may only be created at specific angles (90 degrees).");
            return false;
         }

         if(getSortCommand(event.getLine(2)) < 0 && getSortCommand(event.getLine(3)) < 0) {
            SignUtils.cancelSignCreation(event, "Sort-Criteria not found.");
            return false;
         }

         event.setLine(1, "[Sort]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Sortsign created.");
      }

      return true;
   }

   public boolean checkRailCreation(BlockPlaceEvent event, Player player) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.sort")) {
         BlockUtils.cancelBlockPlace(event, "You are not allowed to build Sort-Blocks.");
         return false;
      } else {
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Sort-Block created.");
         ChatUtils.printLine(player, ChatColor.GRAY, "Place a sign with the Sort-Criteria under it.");
         return true;
      }
   }

   public boolean Execute(Minecart cart, Block railBlock, Block block, Block signBlock, CartWorldSettings settings) {
      if(!BlockUtils.isBlockPowered(railBlock) && !BlockUtils.isBlockPowered(block) && !BlockUtils.isBlockPowered(signBlock)) {
         if(signBlock.getTypeId() != Material.SIGN_POST.getId()) {
            return false;
         } else {
            Sign sign = (Sign)signBlock.getState();
            if(!sign.getLine(1).equalsIgnoreCase("[Sort]")) {
               return false;
            } else {
               int type_left = getSortCommand(sign.getLine(2));
               int type_right = getSortCommand(sign.getLine(3));
               boolean switched = this.checkSortCommand(cart, type_left, sign.getLine(2));
               if(switched) {
                  this.changeLeft(sign);
               }

               if(!switched) {
                  switched = this.checkSortCommand(cart, type_right, sign.getLine(3));
                  if(switched) {
                     this.changeRight(sign);
                  }
               }

               if(!switched) {
                  this.changeStraight(sign);
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   private boolean checkSortCommand(Minecart cart, int switchCommand, String line) {
      Player player;
      int i;
      FalseBookMinecart var7;
      switch(switchCommand) {
      case 0:
         return true;
      case 1:
         if(cart.getPassenger() == null) {
            return true;
         }
         break;
      case 2:
         if(cart.getPassenger() != null) {
            return true;
         }
         break;
      case 3:
         if(cart.getPassenger() != null && cart.getPassenger() instanceof Player) {
            return true;
         }
         break;
      case 4:
         if(cart.getPassenger() != null && cart.getPassenger() instanceof Monster) {
            return true;
         }
         break;
      case 5:
         if(cart.getPassenger() != null && cart.getPassenger() instanceof Animals) {
            return true;
         }
         break;
      case 6:
         if(cart instanceof Minecart && !(cart instanceof StorageMinecart) && !(cart instanceof PoweredMinecart)) {
            return true;
         }
         break;
      case 7:
         if(cart instanceof StorageMinecart) {
            return true;
         }
         break;
      case 8:
         if(cart instanceof PoweredMinecart) {
            return true;
         }
         break;
      case 9:
         if(cart.getPassenger() != null && cart.getPassenger() instanceof Player && ((Player)cart.getPassenger()).getItemInHand().getTypeId() == getItemID(line)) {
            return true;
         }
         break;
      case 10:
         if(cart.getPassenger() != null && cart.getPassenger() instanceof Player) {
            player = (Player)cart.getPassenger();
            if(UtilPermissions.getGroupName(player).toLowerCase().indexOf(get2ndPhrase(line).toLowerCase()) > -1) {
               return true;
            }
         }
         break;
      case 11:
         if(cart.getPassenger() != null) {
            if(cart.getPassenger() instanceof Player) {
               player = (Player)cart.getPassenger();
               if(player.getName().toLowerCase().indexOf(get2ndPhrase(line).toLowerCase()) > -1) {
                  return true;
               }
            }
            break;
         }
      case 12:
         if(cart instanceof StorageMinecart) {
            ArrayList var8 = getItemList(line);
            if(var8 != null) {
               StorageMinecart var11 = (StorageMinecart)cart;

               for(i = 0; i < var8.size(); ++i) {
                  if(((FBItemType)var8.get(i)).getItemID() != 0 && isItemInInventory(var11.getInventory(), (FBItemType)var8.get(i))) {
                     return true;
                  }
               }
            }
         }
         break;
      case 13:
         if(cart instanceof StorageMinecart) {
            StorageMinecart var10 = (StorageMinecart)cart;
            if(InventoryUtils.isInventoryEmpty(var10.getInventory())) {
               return true;
            }
         }
         break;
      case 14:
         if(cart instanceof StorageMinecart) {
            var7 = CartHandler.getFalseBookMinecart(cart);
            if(var7.isProgrammed()) {
               ArrayList var9 = getItemList(line);
               if(var9 != null) {
                  for(i = 0; i < var9.size(); ++i) {
                     if(((FBItemType)var9.get(i)).getItemID() != 0 && var7.isProgrammedItem((FBItemType)var9.get(i))) {
                        return true;
                     }
                  }
               }
            }
         }
         break;
      case 15:
         if(!(cart instanceof StorageMinecart)) {
            return true;
         }

         if(cart instanceof StorageMinecart) {
            var7 = CartHandler.getFalseBookMinecart(cart);
            if(!var7.isProgrammed() || var7.getProgrammedItems().size() < 1) {
               return true;
            }
         }
         break;
      case 16:
      case 17:
      case 18:
      case 19:
      default:
         return false;
      case 20:
         if(cart.getPassenger() != null && cart.getPassenger() instanceof Player) {
            player = (Player)cart.getPassenger();
            String station = FalseBookCartCore.getStation(player);
            if(get2ndPhrase(line).equals(station)) {
               return true;
            }
         }
      }

      return false;
   }

   private void changeLeft(Sign sign) {
      int dir = returnSignDir(sign);
      Location loc = sign.getBlock().getLocation();
      loc.setY(loc.getY() + 2.0D);
      if(dir == 0) {
         loc.setX(loc.getX() + 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)8);
         }
      }

      if(dir == 1) {
         loc.setZ(loc.getZ() + 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)9);
         }
      }

      if(dir == 2) {
         loc.setX(loc.getX() - 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)6);
         }
      }

      if(dir == 3) {
         loc.setZ(loc.getZ() - 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)7);
         }
      }

   }

   private void changeStraight(Sign sign) {
      int dir = returnSignDir(sign);
      Location loc = sign.getBlock().getLocation();
      loc.setY(loc.getY() + 2.0D);
      if(dir == 0) {
         loc.setX(loc.getX() + 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)1);
         }
      }

      if(dir == 1) {
         loc.setZ(loc.getZ() + 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)0);
         }
      }

      if(dir == 2) {
         loc.setX(loc.getX() - 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)1);
         }
      }

      if(dir == 3) {
         loc.setZ(loc.getZ() - 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)0);
         }
      }

   }

   private void changeRight(Sign sign) {
      int dir = returnSignDir(sign);
      Location loc = sign.getBlock().getLocation();
      loc.setY(loc.getY() + 2.0D);
      if(dir == 0) {
         loc.setX(loc.getX() + 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)7);
         }
      }

      if(dir == 1) {
         loc.setZ(loc.getZ() + 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)8);
         }
      }

      if(dir == 2) {
         loc.setX(loc.getX() - 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)9);
         }
      }

      if(dir == 3) {
         loc.setZ(loc.getZ() - 1.0D);
         if(sign.getWorld().getBlockAt(loc).getType().equals(Material.RAILS)) {
            sign.getWorld().getBlockAt(loc).setData((byte)6);
         }
      }

   }

   private static int returnSignDir(Sign sign) {
      return sign.getRawData() == 0?3:(sign.getRawData() == 4?0:(sign.getRawData() == 8?1:(sign.getRawData() == 12?2:-1)));
   }

   private static int getItemID(String line) {
      String[] split = line.split(":");
      if(split.length > 1) {
         try {
            return Integer.valueOf(split[1]).intValue();
         } catch (Exception var3) {
            return -1;
         }
      } else {
         return -1;
      }
   }

   private static String get2ndPhrase(String line) {
      String[] split = line.split(":");
      if(split.length > 1) {
         try {
            return split[1];
         } catch (Exception var3) {
            return "-1";
         }
      } else {
         return "-1";
      }
   }

   private static ArrayList getItemList(String text) {
      String[] split = text.split(":");
      return split.length != 2?null:SignUtils.parseLineToItemList(split[1], "-", false);
   }

   private static boolean isItemInInventory(Inventory inv, FBItemType blockType) {
      return InventoryUtils.countItemInInventory(inv, blockType) > 0;
   }

   private static int getSortCommand(String line) {
      if(line.length() < 1) {
         return -1;
      } else {
         line = line.trim();
         return line.equalsIgnoreCase("all")?0:(!line.equalsIgnoreCase("empty") && !line.equalsIgnoreCase("unoccupied")?(!line.equalsIgnoreCase("full") && !line.equalsIgnoreCase("occupied")?(!line.equalsIgnoreCase("player") && !line.equalsIgnoreCase("ply")?(line.equalsIgnoreCase("mob")?4:(line.equalsIgnoreCase("animal")?5:(!line.equalsIgnoreCase("minecart") && !line.equalsIgnoreCase("cart")?(!line.equalsIgnoreCase("storage") && !line.equalsIgnoreCase("storagecart")?(!line.equalsIgnoreCase("powered") && !line.equalsIgnoreCase("poweredcart")?(line.toLowerCase().startsWith("held:")?9:(!line.toLowerCase().startsWith("group:") && !line.toLowerCase().startsWith("grp:")?(line.toLowerCase().startsWith("ply:")?11:((line.toLowerCase().startsWith("item:") || line.toLowerCase().startsWith("i:")) && !line.toLowerCase().startsWith("item:empty")?12:(!line.toLowerCase().startsWith("item:empty") && !line.toLowerCase().startsWith("i:empty")?(line.toLowerCase().startsWith("p:") && !line.toLowerCase().startsWith("p:empty")?14:(line.toLowerCase().startsWith("p:empty")?15:(line.toLowerCase().startsWith("#st:")?20:(line.toLowerCase().startsWith("mob:")?-1:-1)))):13))):10)):8):7):6))):3):2):1);
      }
   }
}
