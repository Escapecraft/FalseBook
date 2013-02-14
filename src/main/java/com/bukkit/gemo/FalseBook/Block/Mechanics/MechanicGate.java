package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MechanicGate extends MechanicListener {

   private HashMap GateAreas = new HashMap();


   public MechanicGate(FalseBookBlockCore plugin) {
      plugin.getMechanicHandler().registerEvent(BlockBreakEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonExtendEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonRetractEvent.class, this);
      plugin.getMechanicHandler().registerEvent(SignChangeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityChangeBlockEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityExplodeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(PlayerInteractEvent.class, this);
   }

   public boolean isActivatedByRedstone(Block block, BlockRedstoneEvent event) {
      return ConfigHandler.isRedstoneAllowedForGates(block.getWorld().getName()) && ConfigHandler.isGateEnabled(block.getWorld().getName());
   }

   public void reloadMechanic() {
      this.saveGates();
      this.GateAreas = new HashMap();
      this.loadGates();
   }

   private boolean loadGates() {
      File file = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "Gates.db");
      if(file.exists()) {
         try {
            FileInputStream e = new FileInputStream(file);
            DataInputStream in = new DataInputStream(e);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = br.readLine();
            if(strLine == null) {
               FalseBookBlockCore.printInConsole("No Gates loaded");
               return false;
            } else {
               String[] split = strLine.split("=");
               int count = Integer.valueOf(split[1]).intValue();

               for(int i = 0; i < count; ++i) {
                  strLine = br.readLine();
                  if(strLine != null) {
                     String[] splitData = strLine.split(";");
                     if(splitData.length == 2) {
                        String[] sign1 = splitData[0].split(",");
                        String worldName = splitData[1];
                        Block block = Bukkit.getServer().getWorld(worldName).getBlockAt(Integer.valueOf(sign1[0]).intValue(), Integer.valueOf(sign1[1]).intValue(), Integer.valueOf(sign1[2]).intValue());
                        if(this.isBlockTypeAllowed(block.getTypeId(), worldName)) {
                           this.GateAreas.put(block.getLocation().toString(), block);
                        }
                     }
                  }
               }

               FalseBookBlockCore.printInConsole(this.GateAreas.values().size() + " protected gateblocks successfully loaded.");
               return true;
            }
         } catch (Exception var13) {
            var13.printStackTrace();
            FalseBookBlockCore.printInConsole("Error while reading file: plugins/FalseBook/Gates.db");
            return false;
         }
      } else {
         return false;
      }
   }

   private void saveGates() {
      File folder = new File("plugins" + System.getProperty("file.separator") + "FalseBook");
      folder.mkdirs();

      try {
         BufferedWriter e = new BufferedWriter(new FileWriter("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "Gates.db"));
         e.write("GateCount=" + this.GateAreas.values().size() + "\r\n");

         for(int i = 0; i < this.GateAreas.values().size(); ++i) {
            String str = "";
            str = ((Block)this.GateAreas.values().toArray()[i]).getX() + "," + ((Block)this.GateAreas.values().toArray()[i]).getY() + "," + ((Block)this.GateAreas.values().toArray()[i]).getZ() + ";";
            str = str + ((Block)this.GateAreas.values().toArray()[i]).getWorld().getName() + ";";
            e.write(str + "\r\n");
         }

         e.close();
      } catch (IOException var5) {
         FalseBookBlockCore.printInConsole("Error while saving file: plugins/FalseBook/Gates.db");
         var5.printStackTrace();
      }

   }

   private boolean isBlockBreakable(List blockList) {
      for(int j = 0; j < blockList.size(); ++j) {
         try {
            if(this.GateAreas.containsKey(((Block)blockList.get(j)).getLocation().toString())) {
               return false;
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return true;
   }

   private boolean isBlockProtected(Block block) {
      try {
         if(this.GateAreas.containsKey(block.getLocation().toString())) {
            return true;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return false;
   }

   public void onLoad() {
      this.loadGates();
   }

   public void onUnload() {
      this.saveGates();
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
         if(this.isBlockProtected(event.getBlock())) {
            event.setCancelled(true);
         }

      }
   }

   public void onBlockBreak(BlockBreakEvent event) {
      if(!event.isCancelled()) {
         if(ConfigHandler.isGateEnabled(event.getBlock().getWorld().getName())) {
            Block block = event.getBlock();
            if(this.isBlockProtected(event.getBlock())) {
               Player signList = event.getPlayer();
               if(!UtilPermissions.playerCanUseCommand(signList, "falsebook.blocks.destroy")) {
                  signList.sendMessage(ChatColor.RED + "You are not allowed to destroy gates.");
                  event.setCancelled(true);
                  return;
               }
            }

            if(block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)) {
               Sign signList1 = (Sign)block.getState();
               if(!signList1.getLine(1).equalsIgnoreCase("[Gate]") && !signList1.getLine(1).equalsIgnoreCase("[DGate]")) {
                  return;
               }

               Player liftFound = event.getPlayer();
               if(!UtilPermissions.playerCanUseCommand(liftFound, "falsebook.blocks.gate")) {
                  liftFound.sendMessage(ChatColor.RED + "You are not allowed to destroy gates.");
                  event.setCancelled(true);
                  return;
               }
            }

            if(SignUtils.isSignAnchor(block)) {
               ArrayList signList2 = SignUtils.getAdjacentWallSigns(block);
               if(signList2.size() > 0) {
                  boolean liftFound1 = false;
                  Iterator var6 = signList2.iterator();

                  while(var6.hasNext()) {
                     Sign player = (Sign)var6.next();
                     if(player.getLine(1).equalsIgnoreCase("[Gate]") || player.getLine(1).equalsIgnoreCase("[DGate]")) {
                        liftFound1 = true;
                        break;
                     }
                  }

                  signList2.clear();
                  if(!liftFound1) {
                     return;
                  }

                  Player player1 = event.getPlayer();
                  if(!UtilPermissions.playerCanUseCommand(player1, "falsebook.destroy.blocks")) {
                     player1.sendMessage(ChatColor.RED + "You are not allowed to destroy gates.");
                     event.setCancelled(true);
                     return;
                  }
               }

            }
         }
      }
   }

   public void onBlockPistonExtend(BlockPistonExtendEvent event) {
      if(!event.isCancelled()) {
         if(ConfigHandler.isGateEnabled(event.getBlock().getWorld().getName())) {
            if(!this.isBlockBreakable(event.getBlocks())) {
               event.setCancelled(true);
            }

         }
      }
   }

   public void onBlockPistonRetract(BlockPistonRetractEvent event) {
      if(!event.isCancelled()) {
         if(event.isSticky()) {
            if(ConfigHandler.isGateEnabled(event.getBlock().getWorld().getName())) {
               if(this.isBlockProtected(event.getRetractLocation().getBlock())) {
                  event.setCancelled(true);
               }

            }
         }
      }
   }

   public void onSignChange(SignChangeEvent event) {
      if(!event.isCancelled()) {
         if(ConfigHandler.isGateEnabled(event.getBlock().getWorld().getName())) {
            Player player = event.getPlayer();
            if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.gate")) {
               SignUtils.cancelSignCreation(event, "You are not allowed to build gates.");
            } else {
               if(event.getLine(1).equalsIgnoreCase("[Gate]")) {
                  event.setLine(1, "[Gate]");
               } else {
                  event.setLine(1, "[DGate]");
               }

               if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.gate")) {
                  SignUtils.cancelSignCreation(event, "You are not allowed to build gates.");
               } else {
                  try {
                     int e = ConfigHandler.getAllowedGateBlocks(event.getBlock().getWorld().getName()).getValue(0);
                     if(event.getLine(2).length() > 0) {
                        e = Integer.valueOf(event.getLine(2)).intValue();
                     }

                     if(!this.isBlockTypeAllowed(e, event.getBlock().getWorld().getName())) {
                        SignUtils.cancelSignCreation(event, "This blocktype is not allowed for building gates.");
                        return;
                     }
                  } catch (Exception var4) {
                     SignUtils.cancelSignCreation(event, "Line 3 must be an integer or blank.");
                     return;
                  }

                  ChatUtils.printSuccess(player, "[FB-Block]", "Gatesign created.");
               }
            }
         }
      }
   }

   public void onPlayerInteract(PlayerInteractEvent event, boolean isWallSign, boolean isSignPost) {
      if(!event.isCancelled()) {
         if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if(ConfigHandler.isGateEnabled(block.getWorld().getName())) {
               if(isWallSign || isSignPost) {
                  Sign sign = (Sign)block.getState();
                  if(sign.getLine(1).equalsIgnoreCase("[Gate]") || sign.getLine(1).equalsIgnoreCase("[DGate]")) {
                     event.setUseInteractedBlock(Result.DENY);
                     event.setUseItemInHand(Result.DENY);
                     event.setCancelled(true);
                     Player player = event.getPlayer();

                     try {
                        int e = ConfigHandler.getAllowedGateBlocks(block.getWorld().getName()).getValue(0);
                        if(sign.getLine(2).length() > 0) {
                           e = Integer.valueOf(sign.getLine(2)).intValue();
                        }

                        if(!this.isBlockTypeAllowed(e, sign.getBlock().getWorld().getName())) {
                           player.sendMessage(ChatColor.RED + "This blocktype is not allowed for building gates.");
                        } else if(ConfigHandler.isRespectLWCProtections(player.getWorld().getName()) && !LWCProtection.canAccessWithCModify(player, block) && !UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.ignoreLWCProtections")) {
                           player.sendMessage(ChatColor.RED + "This gate is protected!");
                        } else {
                           int result = this.toggle(SignUtils.getSignAnchor(sign).getBlock(), sign.getLine(1).equalsIgnoreCase("[DGate]"), false, false, e);
                           switch(result) {
                           case -1:
                              player.sendMessage(ChatColor.RED + "No Gate found.");
                              break;
                           case 0:
                              player.sendMessage(ChatColor.RED + "This blocktype is not allowed for building gates.");
                              break;
                           case 1:
                              player.sendMessage(ChatColor.GOLD + "Gate moved.");
                           }

                        }
                     } catch (Exception var9) {
                        ;
                     }
                  }
               }
            }
         }
      }
   }

   private boolean isBlockTypeAllowed(int ID, String worldName) {
      return ConfigHandler.getAllowedGateBlocks(worldName).hasValue(ID);
   }

   public int toggle(Block origin, boolean DGate, boolean override, boolean newStatus, int thisID) {
      if(!this.isBlockTypeAllowed(thisID, origin.getWorld().getName())) {
         return 0;
      } else {
         HashMap visited = new HashMap();
         byte ySearch = 16;
         byte yMiniSearch = 5;
         byte xzSearch = 3;
         if(DGate) {
            xzSearch = 1;
            ySearch = 2;
         }

         boolean gateFound = false;
         int yOrigin = origin.getY();

         int x;
         int z;
         for(x = 0; x <= xzSearch; ++x) {
            for(z = 0; z <= xzSearch; ++z) {
               if(!visited.containsKey(x + "_" + z)) {
                  if(this.getTopFence(origin, x, yOrigin, z, yMiniSearch, thisID) != null) {
                     this.searchColumn(origin, x, yOrigin, z, override, newStatus, visited, (Block)null, thisID);
                     gateFound = true;
                     return 1;
                  }

                  if(!visited.containsKey(-x + "_" + -z)) {
                     if(this.getTopFence(origin, -x, yOrigin, -z, yMiniSearch, thisID) != null) {
                        this.searchColumn(origin, -x, yOrigin, -z, override, newStatus, visited, (Block)null, thisID);
                        gateFound = true;
                        return 1;
                     }

                     if(!visited.containsKey(x + "_" + -z)) {
                        if(this.getTopFence(origin, x, yOrigin, -z, yMiniSearch, thisID) != null) {
                           this.searchColumn(origin, x, yOrigin, -z, override, newStatus, visited, (Block)null, thisID);
                           gateFound = true;
                           return 1;
                        }

                        if(!visited.containsKey(-x + "_" + z) && this.getTopFence(origin, -x, yOrigin, z, yMiniSearch, thisID) != null) {
                           this.searchColumn(origin, -x, yOrigin, z, override, newStatus, visited, (Block)null, thisID);
                           gateFound = true;
                           return 1;
                        }
                     }
                  }
               }
            }
         }

         gateFound = false;
         yOrigin = origin.getY();

         for(x = 0; x <= xzSearch; ++x) {
            for(z = 0; z <= xzSearch; ++z) {
               if(!visited.containsKey(x + "_" + z)) {
                  if(this.getTopFence(origin, x, yOrigin, z, ySearch, thisID) != null) {
                     this.searchColumn(origin, x, yOrigin, z, override, newStatus, visited, (Block)null, thisID);
                     gateFound = true;
                     return 1;
                  }

                  if(!visited.containsKey(-x + "_" + -z)) {
                     if(this.getTopFence(origin, -x, yOrigin, -z, ySearch, thisID) != null) {
                        this.searchColumn(origin, -x, yOrigin, -z, override, newStatus, visited, (Block)null, thisID);
                        gateFound = true;
                        return 1;
                     }

                     if(!visited.containsKey(x + "_" + -z)) {
                        if(this.getTopFence(origin, x, yOrigin, -z, ySearch, thisID) != null) {
                           this.searchColumn(origin, x, yOrigin, -z, override, newStatus, visited, (Block)null, thisID);
                           gateFound = true;
                           return 1;
                        }

                        if(!visited.containsKey(-x + "_" + z) && this.getTopFence(origin, -x, yOrigin, z, ySearch, thisID) != null) {
                           this.searchColumn(origin, -x, yOrigin, z, override, newStatus, visited, (Block)null, thisID);
                           gateFound = true;
                           return 1;
                        }
                     }
                  }
               }
            }
         }

         visited.clear();
         visited = null;
         if(gateFound) {
            return 1;
         } else {
            return -1;
         }
      }
   }

   private void searchColumn(Block origin, int xOffset, int y, int zOffset, boolean override, boolean newStatus, HashMap visited, Block lastTop, int GateTypeID) {
      Block topFence = this.getTopFence(origin, xOffset, y, zOffset, 16, GateTypeID);
      if(topFence != null) {
         if(lastTop == null || Math.abs(lastTop.getY() - topFence.getY()) <= 5) {
            if(!visited.containsKey(topFence.getX() + "_" + topFence.getZ())) {
               visited.put(topFence.getX() + "_" + topFence.getZ(), Boolean.valueOf(false));
               if(Math.abs(xOffset) <= ConfigHandler.getMaxGateWidth(origin.getWorld().getName()) && Math.abs(zOffset) <= ConfigHandler.getMaxGateWidth(origin.getWorld().getName())) {
                  boolean TypeID = true;
                  Block block = null;
                  boolean off = newStatus;
                  if(!override) {
                     off = topFence.getRelative(0, -1, 0).getTypeId() == GateTypeID;
                  }

                  int tX;
                  for(tX = 1; tX <= ConfigHandler.getMaxGateHeight(origin.getWorld().getName()); ++tX) {
                     block = topFence.getRelative(0, -tX, 0);
                     int var16 = block.getTypeId();
                     if(var16 != Material.AIR.getId() && var16 != GateTypeID && var16 != Material.WATER.getId() && var16 != Material.STATIONARY_WATER.getId() && var16 != Material.LAVA.getId() && var16 != Material.STATIONARY_LAVA.getId()) {
                        break;
                     }

                     if(off) {
                        block.setTypeIdAndData(Material.AIR.getId(), (byte)0, true);
                        this.GateAreas.remove(block.getLocation().toString());
                        this.GateAreas.remove(topFence.getLocation().toString());
                     } else {
                        block.setTypeIdAndData(GateTypeID, (byte)0, true);
                        this.GateAreas.put(block.getLocation().toString(), block);
                        this.GateAreas.put(topFence.getLocation().toString(), topFence);
                     }
                  }

                  for(tX = -1; tX <= 1; ++tX) {
                     for(int tZ = -1; tZ <= 1; ++tZ) {
                        this.searchColumn(origin, xOffset + tX, y, zOffset + tZ, override, newStatus, visited, topFence, GateTypeID);
                     }
                  }

               }
            }
         }
      }
   }

   private Block getTopFence(Block origin, int xOffset, int y, int zOffset, int searchWidth, int TypeID) {
      int i;
      for(i = 0; i <= searchWidth; ++i) {
         if(y + i <= origin.getWorld().getMaxHeight() - 1 && origin.getRelative(xOffset, i, zOffset).getTypeId() == TypeID && origin.getRelative(xOffset, i + 1, zOffset).getTypeId() != TypeID && (origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.AIR.getId() || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == TypeID || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.WATER.getId() || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.STATIONARY_WATER.getId() || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.LAVA.getId() || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.STATIONARY_LAVA.getId())) {
            return origin.getRelative(xOffset, i, zOffset);
         }
      }

      for(i = -1; i >= -searchWidth; --i) {
         if(y + i >= 2 && origin.getRelative(xOffset, i, zOffset).getTypeId() == TypeID && origin.getRelative(xOffset, i + 1, zOffset).getTypeId() != TypeID && (origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.AIR.getId() || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == TypeID || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.WATER.getId() || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.STATIONARY_WATER.getId() || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.LAVA.getId() || origin.getRelative(xOffset, i - 1, zOffset).getTypeId() == Material.STATIONARY_LAVA.getId())) {
            return origin.getRelative(xOffset, i, zOffset);
         }
      }

      return null;
   }
}
