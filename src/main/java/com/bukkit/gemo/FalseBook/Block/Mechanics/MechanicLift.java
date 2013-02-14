package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MechanicLift extends MechanicListener {

   public MechanicLift(FalseBookBlockCore plugin) {
      plugin.getMechanicHandler().registerEvent(BlockBreakEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonExtendEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonRetractEvent.class, this);
      plugin.getMechanicHandler().registerEvent(SignChangeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityChangeBlockEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityExplodeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(PlayerInteractEvent.class, this);
   }

   private boolean isBlockBreakable(List blockList) {
      ArrayList signList = new ArrayList();

      for(int liftFound = 0; liftFound < blockList.size(); ++liftFound) {
         SignUtils.addAdjacentWallSigns(signList, (Block)blockList.get(liftFound));
      }

      if(signList.size() > 0) {
         boolean var6 = false;
         Iterator var5 = signList.iterator();

         while(var5.hasNext()) {
            Sign sign = (Sign)var5.next();
            if(sign.getLine(1).equalsIgnoreCase("[Lift Up]") || sign.getLine(1).equalsIgnoreCase("[Lift Down]") || sign.getLine(1).equalsIgnoreCase("[Lift]")) {
               var6 = true;
               break;
            }
         }

         signList.clear();
         if(var6) {
            return false;
         }
      }

      return true;
   }

   private boolean isBlockBreakable(Block block) {
      ArrayList signList = new ArrayList();
      SignUtils.addAdjacentWallSigns(signList, block);
      if(signList.size() > 0) {
         boolean liftFound = false;
         Iterator var5 = signList.iterator();

         while(var5.hasNext()) {
            Sign sign = (Sign)var5.next();
            if(sign.getLine(1).equalsIgnoreCase("[Lift Up]") || sign.getLine(1).equalsIgnoreCase("[Lift Down]") || sign.getLine(1).equalsIgnoreCase("[Lift]")) {
               liftFound = true;
               break;
            }
         }

         signList.clear();
         if(liftFound) {
            return false;
         }
      }

      return true;
   }

   public void onEntityExplode(EntityExplodeEvent event) {
      if(!event.isCancelled()) {
         if(!this.isBlockBreakable(event.blockList())) {
            event.setYield(0.0F);
            event.setCancelled(true);
         }

      }
   }

   public void onEntityChangeBlock(EntityChangeBlockEvent event) {
      if(!event.isCancelled()) {
         ArrayList blockList = new ArrayList();
         blockList.add(event.getBlock());
         if(!this.isBlockBreakable((List)blockList)) {
            event.setCancelled(true);
         }

         blockList.clear();
         blockList = null;
      }
   }

   public void onBlockBreak(BlockBreakEvent event) {
      if(!event.isCancelled()) {
         Block block = event.getBlock();
         if(block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)) {
            Sign signList = (Sign)block.getState();
            if(!signList.getLine(1).equalsIgnoreCase("[Lift Up]") && !signList.getLine(1).equalsIgnoreCase("[Lift Down]") && !signList.getLine(1).equalsIgnoreCase("[Lift]")) {
               return;
            }

            Player liftFound = event.getPlayer();
            if(!UtilPermissions.playerCanUseCommand(liftFound, "falsebook.blocks.lift")) {
               liftFound.sendMessage(ChatColor.RED + "You are not allowed to destroy lifts.");
               event.setCancelled(true);
               return;
            }
         }

         if(SignUtils.isSignAnchor(block)) {
            ArrayList signList1 = SignUtils.getAdjacentWallSigns(block);
            if(signList1.size() > 0) {
               boolean liftFound1 = false;
               Iterator var6 = signList1.iterator();

               while(var6.hasNext()) {
                  Sign player = (Sign)var6.next();
                  if(player.getLine(1).equalsIgnoreCase("[Lift Up]") || player.getLine(1).equalsIgnoreCase("[Lift Down]") || player.getLine(1).equalsIgnoreCase("[Lift]")) {
                     liftFound1 = true;
                     break;
                  }
               }

               signList1.clear();
               if(!liftFound1) {
                  return;
               }

               Player player1 = event.getPlayer();
               if(!UtilPermissions.playerCanUseCommand(player1, "falsebook.blocks.lift")) {
                  player1.sendMessage(ChatColor.RED + "You are not allowed to destroy lifts.");
                  event.setCancelled(true);
                  return;
               }
            }

         }
      }
   }

   public void onBlockPistonExtend(BlockPistonExtendEvent event) {
      if(!event.isCancelled()) {
         if(!this.isBlockBreakable(event.getBlocks())) {
            event.setCancelled(true);
         }

      }
   }

   public void onBlockPistonRetract(BlockPistonRetractEvent event) {
      if(!event.isCancelled()) {
         if(event.isSticky()) {
            if(!this.isBlockBreakable(event.getRetractLocation().getBlock())) {
               event.setCancelled(true);
            }

         }
      }
   }

   public void onSignChange(SignChangeEvent event) {
      if(!event.isCancelled()) {
         Player player = event.getPlayer();
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.lift")) {
            SignUtils.cancelSignCreation(event, "You are not allowed to build lifts.");
         } else {
            if(event.getLine(1).equalsIgnoreCase("[Lift Up]")) {
               event.setLine(1, "[Lift Up]");
               if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.lift")) {
                  SignUtils.cancelSignCreation(event, "You are not allowed to build Elevators.");
                  return;
               }

               if(this.searchLiftDown(event.getBlock())) {
                  player.sendMessage(ChatColor.GREEN + "Elevatorsign created and linked.");
               } else {
                  player.sendMessage(ChatColor.GRAY + "Elevatorsign created but not linked yet.");
               }
            } else if(event.getLine(1).equalsIgnoreCase("[Lift Down]")) {
               event.setLine(1, "[Lift Down]");
               if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.lift")) {
                  SignUtils.cancelSignCreation(event, "You are not allowed to build Elevators.");
                  return;
               }

               if(this.searchLiftUp(event.getBlock())) {
                  player.sendMessage(ChatColor.GREEN + "Elevatorsign created and linked.");
               } else {
                  player.sendMessage(ChatColor.GRAY + "Elevatorsign created but not linked yet.");
               }
            } else if(event.getLine(1).equalsIgnoreCase("[Lift]")) {
               event.setLine(1, "[Lift]");
               if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.lift")) {
                  SignUtils.cancelSignCreation(event, "You are not allowed to build Elevators.");
                  return;
               }

               if(!this.searchLiftUp(event.getBlock()) && !this.searchLiftDown(event.getBlock())) {
                  player.sendMessage(ChatColor.GRAY + "Elevatorsign created but not linked yet.");
               } else {
                  player.sendMessage(ChatColor.GREEN + "Elevatorsign created and linked.");
               }
            }

         }
      }
   }

   public void onPlayerInteract(PlayerInteractEvent event, boolean isWallSign, boolean isSignPost) {
      if(!event.isCancelled()) {
         if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if(isWallSign || isSignPost) {
               Sign sign = (Sign)block.getState();
               if(sign.getLine(1).equalsIgnoreCase("[Lift Up]") || sign.getLine(1).equalsIgnoreCase("[Lift Down]") || sign.getLine(1).equalsIgnoreCase("[Lift]")) {
                  event.setUseInteractedBlock(Result.DENY);
                  event.setUseItemInHand(Result.DENY);
                  event.setCancelled(true);
                  Player player = event.getPlayer();
                  if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.lift") && !UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.lift.use")) {
                     player.sendMessage(ChatColor.RED + "You are not allowed to use lifts.");
                  } else if(ConfigHandler.isRespectLWCProtections(player.getWorld().getName()) && !LWCProtection.canAccessWithCModify(player, block) && !UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.ignoreLWCProtections")) {
                     player.sendMessage(ChatColor.RED + "This lift is protected!");
                  } else {
                     this.check(sign, player, block);
                  }
               }
            }
         }
      }
   }

   private boolean searchLiftUp(Block block) {
      World current = block.getWorld();

      for(int i = block.getLocation().getBlockY() - 1; i > 0; --i) {
         Block newBlock = current.getBlockAt(block.getX(), i, block.getZ());
         if(newBlock.getType().equals(Material.WALL_SIGN) || newBlock.getType().equals(Material.SIGN_POST)) {
            Sign sign2 = (Sign)newBlock.getState();
            if(sign2.getLine(1).equalsIgnoreCase("[Lift Up]") || sign2.getLine(1).equalsIgnoreCase("[Lift Down]") || sign2.getLine(1).equalsIgnoreCase("[Lift]")) {
               return true;
            }
         }
      }

      return false;
   }

   private Block getLiftUpBlock(Block block) {
      World current = block.getWorld();

      for(int i = block.getLocation().getBlockY() - 1; i > 0; --i) {
         Block newBlock = current.getBlockAt(block.getX(), i, block.getZ());
         if(newBlock.getType().equals(Material.WALL_SIGN) || newBlock.getType().equals(Material.SIGN_POST)) {
            Sign sign2 = (Sign)newBlock.getState();
            if(sign2.getLine(1).equalsIgnoreCase("[Lift Down]") || sign2.getLine(1).equalsIgnoreCase("[Lift Up]") || sign2.getLine(1).equalsIgnoreCase("[Lift]")) {
               return newBlock;
            }
         }
      }

      return null;
   }

   private boolean searchLiftDown(Block block) {
      World current = block.getWorld();

      for(int i = block.getLocation().getBlockY() + 1; i < block.getWorld().getMaxHeight() - 1; ++i) {
         Block newBlock = current.getBlockAt(block.getX(), i, block.getZ());
         if(newBlock.getType().equals(Material.WALL_SIGN) || newBlock.getType().equals(Material.SIGN_POST)) {
            Sign sign2 = (Sign)newBlock.getState();
            if(sign2.getLine(1).equalsIgnoreCase("[Lift Up]") || sign2.getLine(1).equalsIgnoreCase("[Lift Down]") || sign2.getLine(1).equalsIgnoreCase("[Lift]")) {
               return true;
            }
         }
      }

      return false;
   }

   private Block getLiftDownBlock(Block block) {
      World current = block.getWorld();

      for(int i = block.getLocation().getBlockY() + 1; i < block.getWorld().getMaxHeight() - 1; ++i) {
         Block newBlock = current.getBlockAt(block.getX(), i, block.getZ());
         if(newBlock.getType().equals(Material.WALL_SIGN) || newBlock.getType().equals(Material.SIGN_POST)) {
            Sign sign2 = (Sign)newBlock.getState();
            if(sign2.getLine(1).equalsIgnoreCase("[Lift Up]") || sign2.getLine(1).equalsIgnoreCase("[Lift Down]") || sign2.getLine(1).equalsIgnoreCase("[Lift]")) {
               return newBlock;
            }
         }
      }

      return null;
   }

   private void check(Sign sign, Player player, Block block) {
      Location playerPos = player.getLocation().clone();
      Block newBlock;
      boolean isFree;
      boolean isFreeAbove;
      int i;
      if(sign.getLine(1).equalsIgnoreCase("[Lift Up]")) {
         if(this.searchLiftDown(block)) {
            newBlock = this.getLiftDownBlock(block);
            isFree = false;
            isFreeAbove = false;

            for(i = newBlock.getY() + 1; i > newBlock.getY() - 3; --i) {
               if(BlockUtils.canPassThrough(block.getWorld().getBlockAt(playerPos.getBlockX(), i, playerPos.getBlockZ()).getTypeId())) {
                  if(isFreeAbove) {
                     isFree = true;
                     playerPos.setY((double)i);
                  } else {
                     isFreeAbove = true;
                  }
               } else {
                  isFreeAbove = false;
               }
            }

            if(isFree) {
               if(!BlockUtils.canPassThrough(block.getWorld().getBlockAt(playerPos.getBlockX(), playerPos.getBlockY() - 1, playerPos.getBlockZ()).getTypeId())) {
                  player.teleport(playerPos);
                  player.sendMessage(ChatColor.GOLD + "Lift Up");
                  if(((Sign)newBlock.getState()).getLine(0).length() > 0) {
                     player.sendMessage(ChatColor.GRAY + "Floor: " + ((Sign)newBlock.getState()).getLine(0));
                  }
               } else {
                  player.sendMessage(ChatColor.RED + "You would have nothing to stand on.");
               }
            } else {
               player.sendMessage(ChatColor.RED + "You would be obstructed.");
            }
         } else {
            player.sendMessage(ChatColor.RED + "No lift found.");
         }
      } else if(sign.getLine(1).equalsIgnoreCase("[Lift Down]")) {
         if(this.searchLiftUp(block)) {
            newBlock = this.getLiftUpBlock(block);
            isFree = false;
            isFreeAbove = false;

            for(i = newBlock.getY() + 1; i > newBlock.getY() - 3; --i) {
               if(BlockUtils.canPassThrough(block.getWorld().getBlockAt(playerPos.getBlockX(), i, playerPos.getBlockZ()).getTypeId())) {
                  if(isFreeAbove) {
                     isFree = true;
                     playerPos.setY((double)i);
                  } else {
                     isFreeAbove = true;
                  }
               } else {
                  isFreeAbove = false;
               }
            }

            if(isFree) {
               if(!BlockUtils.canPassThrough(block.getWorld().getBlockAt(playerPos.getBlockX(), playerPos.getBlockY() - 1, playerPos.getBlockZ()).getTypeId())) {
                  player.teleport(playerPos);
                  player.sendMessage(ChatColor.GOLD + "Lift Down");
                  if(((Sign)newBlock.getState()).getLine(0).length() > 0) {
                     player.sendMessage(ChatColor.GRAY + "Floor: " + ((Sign)newBlock.getState()).getLine(0));
                  }
               } else {
                  player.sendMessage(ChatColor.RED + "You would have nothing to stand on.");
               }
            } else {
               player.sendMessage(ChatColor.RED + "You would be obstructed.");
            }
         } else {
            player.sendMessage(ChatColor.RED + "No lift found.");
         }
      }

   }
}
