package com.bukkit.gemo.FalseBook.Cart;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartCore;
import com.bukkit.gemo.FalseBook.Cart.FalseBookMinecart;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Catcher;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FalseBookCartVehicleListener implements Listener {

   public static int getItemIDFromName(String name) {
      Material[] all = Material.values();
      if(name == null) {
         return 0;
      } else {
         String[] args = name.split(":");

         for(int i = 0; i < all.length; ++i) {
            if(all[i].name().equalsIgnoreCase(args[0])) {
               if(all[i].getId() == 35 && args.length > 1) {
                  if(Integer.valueOf(args[1]).intValue() >= 0 && Integer.valueOf(args[1]).intValue() <= 15) {
                     return all[i].getId();
                  }
               } else if(all[i].getId() == 44 && args.length > 1) {
                  if(Integer.valueOf(args[1]).intValue() >= 0 && Integer.valueOf(args[1]).intValue() <= 3) {
                     return all[i].getId();
                  }
               } else {
                  if(all[i].getId() != 17 || args.length <= 1) {
                     return all[i].getId();
                  }

                  if(Integer.valueOf(args[1]).intValue() >= 0 && Integer.valueOf(args[1]).intValue() <= 2) {
                     return all[i].getId();
                  }
               }
            }
         }

         return 0;
      }
   }

   @EventHandler
   public void onVehicleDamage(VehicleDamageEvent event) {
      if(event.getVehicle() instanceof Minecart || event.getVehicle() instanceof PoweredMinecart || event.getVehicle() instanceof StorageMinecart) {
         FalseBookMinecart FBCart = CartHandler.getFalseBookMinecart((Minecart)event.getVehicle());
         if(!FBCart.getOwner().equalsIgnoreCase("")) {
            if(!(event.getAttacker() instanceof Player)) {
               event.setDamage(0);
               event.setCancelled(true);
            } else {
               Player player = (Player)event.getAttacker();
               if(!FBCart.getOwner().equalsIgnoreCase(player.getName()) && !UtilPermissions.playerCanUseCommand(player, "falsebook.admin.cart.canlookintoallcarts")) {
                  ChatUtils.printError(player, "[FB-Cart]", "You are not the owner of this storagecart!");
                  event.setDamage(0);
                  event.setCancelled(true);
               }
            }
         }
      }
   }

   @EventHandler
   public void onVehicleMove(VehicleMoveEvent event) {
      if(event.getVehicle() instanceof Minecart) {
         if(!BlockUtils.LocationEquals(event.getFrom(), event.getTo())) {
            FalseBookMinecart cart = CartHandler.getFalseBookMinecart((Minecart)event.getVehicle());
            int ID = BlockUtils.getRawTypeID(event.getTo());
            if(cart.isOnTracks(ID)) {
               if(cart.isOnPressureplates(ID)) {
                  if(!cart.wasOnPlates()) {
                     cart.getMinecart().setDerailedVelocityMod(new Vector(0.95D, 0.95D, 0.95D));
                  }
               } else if(cart.wasOnPlates()) {
                  cart.getMinecart().setDerailedVelocityMod(new Vector(0.5D, 0.5D, 0.5D));
               }

               Location loc = event.getTo();
               Block underBlock = loc.getBlock().getRelative(BlockFace.DOWN);
               Block railBlock = loc.getBlock();
               CartWorldSettings thisSettings = FalseBookCartCore.getOrCreateSettings(cart.getMinecart().getWorld().getName());
               CartMechanic mechanic = thisSettings.getMechanic(underBlock);
               if(mechanic != null) {
                  Block speed = underBlock.getRelative(BlockFace.DOWN);
                  thisSettings.executeMechanic(cart.getMinecart(), railBlock, underBlock, speed);
               }

               if(thisSettings.isDoFrictionFix() && !cart.isInConstantSpeedMode()) {
                  cart.doFrictionFix();
                  if((railBlock.getData() != 5 || cart.getMotionZ() <= 0.0D) && (railBlock.getData() != 4 || cart.getMotionZ() >= 0.0D)) {
                     if(railBlock.getData() == 2 && cart.getMotionX() > 0.0D || railBlock.getData() == 3 && cart.getMotionX() < 0.0D) {
                        cart.setMotionX(cart.getMotionX() * 1.25D);
                        if(cart.getMotionX() > 0.5D) {
                           cart.setMotionX(0.5D);
                        } else if(cart.getMotionX() < -0.5D) {
                           cart.setMotionX(-0.5D);
                        }
                     }
                  } else {
                     cart.setMotionZ(cart.getMotionZ() * 1.25D);
                     if(cart.getMotionZ() > 0.5D) {
                        cart.setMotionZ(0.5D);
                     } else if(cart.getMotionZ() < -0.5D) {
                        cart.setMotionZ(-0.5D);
                     }
                  }
               }

               if(cart.isInConstantSpeedMode()) {
                  double speed1 = thisSettings.getConstantSpeed();
                  if(cart.getMotionZ() > 0.0D) {
                     cart.setMotionZ(speed1);
                  }

                  if(cart.getMotionZ() < 0.0D) {
                     cart.setMotionZ(-speed1);
                  }

                  if(cart.getMotionX() > 0.0D) {
                     cart.setMotionX(speed1);
                  }

                  if(cart.getMotionX() < 0.0D) {
                     cart.setMotionX(-speed1);
                  }
               }

               thisSettings = null;
               cart.update(ID);
            }
         }
      }
   }

   private boolean isBlockPowered(Block block, boolean checkSign) {
      return block.isBlockIndirectlyPowered() || block.getRelative(BlockFace.UP).isBlockIndirectlyPowered() || block.isBlockPowered() || block.getRelative(BlockFace.UP).isBlockPowered() || (block.getRelative(BlockFace.DOWN).isBlockPowered() || block.getRelative(BlockFace.DOWN).isBlockIndirectlyPowered()) && checkSign;
   }

   @EventHandler
   public void onVehicleExit(VehicleExitEvent event) {
      if(event.getVehicle() instanceof Minecart) {
         CartWorldSettings thisSettings = FalseBookCartCore.getOrCreateSettings(event.getVehicle().getWorld().getName());
         if(thisSettings.isRemoveCartOnExit()) {
            FalseBookMinecart cart = CartHandler.getFalseBookMinecart((Minecart)event.getVehicle());
            if(cart.getPassenger() instanceof Player) {
               cart.kill();
               ItemStack cartstack = new ItemStack(Material.MINECART, 1);
               ((Player)cart.getPassenger()).getInventory().addItem(new ItemStack[]{cartstack});
            }
         }

      }
   }

   @EventHandler
   public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
      if(event.getVehicle() instanceof Minecart) {
         CartWorldSettings thisSettings = FalseBookCartCore.getOrCreateSettings(event.getVehicle().getWorld().getName());
         if(!(event.getEntity() instanceof Player) && thisSettings.isKillCartsOnCollision()) {
            FalseBookMinecart cart;
            if(event.getVehicle() instanceof Minecart && !(event.getVehicle() instanceof StorageMinecart) && !(event.getVehicle() instanceof PoweredMinecart) && event.getEntity() instanceof Minecart && !(event.getEntity() instanceof StorageMinecart) && !(event.getEntity() instanceof PoweredMinecart) && ((Minecart)event.getVehicle()).getPassenger() == null) {
               cart = CartHandler.getFalseBookMinecart((Minecart)event.getVehicle());
               cart.kill();
               event.setCollisionCancelled(true);
               event.setPickupCancelled(true);
               event.setCancelled(true);
            }

            if(!(event.getEntity() instanceof Player)) {
               if(event.getEntity() instanceof Minecart && !(event.getEntity() instanceof StorageMinecart) && !(event.getEntity() instanceof PoweredMinecart)) {
                  if(((Minecart)event.getEntity()).getPassenger() == null) {
                     event.getEntity().remove();
                     event.setCollisionCancelled(true);
                     event.setPickupCancelled(true);
                     event.setCancelled(true);
                  }
               } else if(event.getEntity() instanceof Minecart && !(event.getEntity() instanceof StorageMinecart) && !(event.getEntity() instanceof PoweredMinecart)) {
                  cart = CartHandler.getFalseBookMinecart((Minecart)event.getVehicle());
                  cart.kill();
                  event.setCancelled(true);
                  event.setCollisionCancelled(true);
                  event.setPickupCancelled(true);
               }
            }
         }

         Minecart cart1 = (Minecart)event.getVehicle();
         World world = cart1.getWorld();
         Block under = world.getBlockAt(cart1.getLocation().getBlockX(), cart1.getLocation().getBlockY() - 1, cart1.getLocation().getBlockZ());
         Block signBlock = world.getBlockAt(cart1.getLocation().getBlockX(), cart1.getLocation().getBlockY() - 2, cart1.getLocation().getBlockZ());
         byte newData = 0;
         if(under.getTypeId() == 35 || under.getTypeId() == 17 || under.getTypeId() == 44) {
            newData = under.getData();
         }

         if(under.getTypeId() == thisSettings.getStationBlock() && newData == thisSettings.getStationBlockValue() && !this.isBlockPowered(under, true) && signBlock.getType().equals(Material.SIGN_POST)) {
            event.setCancelled(true);
            event.setCollisionCancelled(true);
            event.setPickupCancelled(true);
            Catcher catcher = (Catcher)thisSettings.getMechanic(under);
            if(catcher == null) {
               return;
            }

            catcher.resetCart(cart1, cart1.getLocation().getBlock());
         }

      }
   }

   @EventHandler
   public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
      Vehicle vehicle = event.getVehicle();
      if(vehicle instanceof Minecart) {
         Block eventBlock = event.getBlock();
         Block under1Block = eventBlock.getRelative(BlockFace.DOWN);
         Block under2Block = under1Block.getRelative(BlockFace.DOWN);
         Minecart cart = (Minecart)vehicle;
         CartWorldSettings thisSettings = FalseBookCartCore.getOrCreateSettings(cart.getWorld().getName());
         if(eventBlock.getTypeId() == Material.CHEST.getId() && under1Block.getTypeId() == thisSettings.getCollectDepositBlock() && under1Block.getData() == thisSettings.getCollectDepositBlockValue() && under2Block.getType().equals(Material.SIGN_POST)) {
            Sign signBlock = (Sign)under2Block.getState();
            Chest chest;
            ItemStack newItem;
            FalseBookMinecart FBCart;
            if(signBlock.getLine(1).equalsIgnoreCase("[Cart]")) {
               chest = (Chest)eventBlock.getState();
               newItem = new ItemStack(Material.MINECART, 1);
               chest.getInventory().addItem(new ItemStack[]{newItem});
               FBCart = CartHandler.getFalseBookMinecart((Minecart)event.getVehicle());
               FBCart.kill();
            }

            if(signBlock.getLine(1).equalsIgnoreCase("[StorageCart]")) {
               chest = (Chest)eventBlock.getState();
               if(!(cart instanceof StorageMinecart)) {
                  return;
               }

               StorageMinecart var16 = (StorageMinecart)cart;
               ItemStack[] var14;
               int var13 = (var14 = var16.getInventory().getContents()).length;

               ItemStack var15;
               for(int FBCart1 = 0; FBCart1 < var13; ++FBCart1) {
                  var15 = var14[FBCart1];
                  if(var15 != null && var15.getTypeId() != Material.AIR.getId()) {
                     var16.getWorld().dropItem(var16.getLocation(), var15);
                  }
               }

               var15 = new ItemStack(Material.STORAGE_MINECART, 1);
               chest.getInventory().addItem(new ItemStack[]{var15});
               FalseBookMinecart var17 = CartHandler.getFalseBookMinecart((Minecart)event.getVehicle());
               var17.kill();
            }

            if(signBlock.getLine(1).equalsIgnoreCase("[PoweredCart]")) {
               chest = (Chest)eventBlock.getState();
               if(!(cart instanceof PoweredMinecart)) {
                  return;
               }

               newItem = new ItemStack(Material.POWERED_MINECART, 1);
               chest.getInventory().addItem(new ItemStack[]{newItem});
               FBCart = CartHandler.getFalseBookMinecart((Minecart)event.getVehicle());
               FBCart.kill();
            }
         }

      }
   }

   @EventHandler
   public void onVehicleCreate(VehicleCreateEvent event) {
      Vehicle vehicle = event.getVehicle();
      if(vehicle instanceof Minecart) {
         CartHandler.getFalseBookMinecart((Minecart)event.getVehicle());
      }
   }

   @EventHandler
   public void onVehicleDestroy(VehicleDestroyEvent event) {
      if(!event.isCancelled()) {
         Vehicle vehicle = event.getVehicle();
         if(vehicle instanceof Minecart) {
            CartHandler.getFalseBookMinecart((Minecart)event.getVehicle()).kill();
         }
      }
   }

   public static boolean isSetSlowWhenEmpty(String worldName) {
      return FalseBookCartCore.getOrCreateSettings(worldName).isSetSlowWhenEmpty();
   }

   public static double getMaxSpeed(String worldName) {
      return FalseBookCartCore.getOrCreateSettings(worldName).getMaxSpeed();
   }

   public static double getLaunchSpeed(String worldName) {
      return FalseBookCartCore.getOrCreateSettings(worldName).getLaunchSpeed();
   }

   public static double getConstantSpeed(String worldName) {
      return FalseBookCartCore.getOrCreateSettings(worldName).getConstantSpeed();
   }
}
