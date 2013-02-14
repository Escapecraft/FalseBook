package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Block.Mechanics.BridgeArea;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MechanicBridge extends MechanicListener {

   private ArrayList BridgeAreas = new ArrayList();


   public MechanicBridge(FalseBookBlockCore plugin) {
      plugin.getMechanicHandler().registerEvent(BlockBreakEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonExtendEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonRetractEvent.class, this);
      plugin.getMechanicHandler().registerEvent(SignChangeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityChangeBlockEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityExplodeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(PlayerInteractEvent.class, this);
   }

   public boolean isActivatedByRedstone(Block block, BlockRedstoneEvent event) {
      return ConfigHandler.isRedstoneAllowedForBridges(block.getWorld().getName()) && ConfigHandler.isBridgeEnabled(block.getWorld().getName());
   }

   public void onLoad() {
      this.loadBridges();
   }

   public void reloadMechanic() {
      this.saveBridges();
      this.BridgeAreas = new ArrayList();
      this.loadBridges();
   }

   public void onUnload() {
      this.saveBridges();
   }

   public void onEntityExplode(EntityExplodeEvent event) {
      if(!event.isCancelled()) {
         if(this.isBlockProtected(event.blockList())) {
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

   public void onSignChange(SignChangeEvent event) {
      if(!event.isCancelled()) {
         if(ConfigHandler.isBridgeEnabled(event.getBlock().getWorld().getName())) {
            Player player = event.getPlayer();
            if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.bridge")) {
               SignUtils.cancelSignCreation(event, "You are not allowed to build bridges.");
            } else {
               String worldName = event.getBlock().getWorld().getName();
               if(SignUtils.getDirection((Sign)event.getBlock().getState()) == -1) {
                  SignUtils.cancelSignCreation(event, "Bridgesigns may only be created at specific angles (90 degrees).");
               } else if(event.getBlock().getTypeId() != Material.SIGN_POST.getId()) {
                  SignUtils.cancelSignCreation(event, "Bridgesigns must be signposts.");
               } else {
                  if(event.getLine(1).equalsIgnoreCase("[Bridge]")) {
                     event.setLine(1, "[Bridge]");
                  }

                  if(event.getLine(1).equalsIgnoreCase("[Bridge End]")) {
                     event.setLine(1, "[Bridge End]");
                  }

                  if(Parser.isIntegerOrEmpty(event.getLine(2))) {
                     int width = Parser.getInteger(event.getLine(2), 1);
                     if(width < 0) {
                        SignUtils.cancelSignCreation(event, "Line 3 must be >= 0.");
                     } else if(width > ConfigHandler.getMaxBridgeSideWidth(worldName)) {
                        SignUtils.cancelSignCreation(event, "Line 3 must be <= " + ConfigHandler.getMaxBridgeSideWidth(worldName));
                     } else {
                        if(width == 1) {
                           event.setLine(2, "");
                        }

                        if(Parser.isIntegerOrEmpty(event.getLine(3))) {
                           width = Parser.getInteger(event.getLine(3), 1);
                           if(width < 0) {
                              SignUtils.cancelSignCreation(event, "Line 4 must be >= 0.");
                           } else if(width > ConfigHandler.getMaxBridgeSideWidth(worldName)) {
                              SignUtils.cancelSignCreation(event, "Line 4 must be <= " + ConfigHandler.getMaxBridgeSideWidth(worldName));
                           } else {
                              if(width == 1) {
                                 event.setLine(3, "");
                              }

                              ChatUtils.printSuccess(player, "[FB-Block]", "Bridgesign created.");
                           }
                        } else {
                           SignUtils.cancelSignCreation(event, "Line 4 must be a number >= 0, or leave it empty.");
                        }
                     }
                  } else {
                     SignUtils.cancelSignCreation(event, "Line 3 must be a number >= 0, or leave it empty.");
                  }
               }
            }
         }
      }
   }

   public void onBlockBreak(BlockBreakEvent event) {
      if(!event.isCancelled()) {
         if(ConfigHandler.isBridgeEnabled(event.getBlock().getWorld().getName())) {
            Block block = event.getBlock();
            if(!block.getType().equals(Material.SIGN_POST) && !block.getType().equals(Material.WALL_SIGN)) {
               boolean isOp1 = UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.destroy.blocks");
               if(this.isBlockProtected(block) && !isOp1) {
                  event.getPlayer().sendMessage(ChatColor.AQUA + "[ FalseBook ] " + ChatColor.RED + "You are not allowed to destroy bridgeblocks.!");
                  event.setCancelled(true);
                  return;
               }
            } else {
               Sign isOp = (Sign)block.getState();
               if(!isOp.getLine(1).equalsIgnoreCase("[Bridge]") && !isOp.getLine(1).equalsIgnoreCase("[Bridge End]")) {
                  return;
               }

               Player player = event.getPlayer();
               if(this.isBlockProtected(block) && !UtilPermissions.playerCanUseCommand(player, "falsebook.destroy.blocks")) {
                  player.sendMessage(ChatColor.RED + "You are not allowed to destroy bridgesigns.");
                  event.setCancelled(true);
                  return;
               }
            }

         }
      }
   }

   public void onBlockPistonExtend(BlockPistonExtendEvent event) {
      if(!event.isCancelled()) {
         if(ConfigHandler.isBridgeEnabled(event.getBlock().getWorld().getName())) {
            if(this.isBlockProtected(event.getBlocks())) {
               event.setCancelled(true);
            }

         }
      }
   }

   public void onBlockPistonRetract(BlockPistonRetractEvent event) {
      if(!event.isCancelled()) {
         if(event.isSticky()) {
            if(ConfigHandler.isBridgeEnabled(event.getBlock().getWorld().getName())) {
               if(this.isBlockProtected(event.getRetractLocation().getBlock())) {
                  event.setCancelled(true);
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
               if(sign.getLine(1).equalsIgnoreCase("[Bridge]") || sign.getLine(1).equalsIgnoreCase("[Bridge End]")) {
                  if(ConfigHandler.isBridgeEnabled(block.getWorld().getName())) {
                     event.setUseInteractedBlock(Result.DENY);
                     event.setUseItemInHand(Result.DENY);
                     event.setCancelled(true);
                     if(ConfigHandler.isRespectLWCProtections(event.getPlayer().getWorld().getName()) && !LWCProtection.canAccessWithCModify(event.getPlayer(), block) && !UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.blocks.ignoreLWCProtections")) {
                        event.getPlayer().sendMessage(ChatColor.RED + "This bridge is protected!");
                     } else {
                        this.handleReturnResult(event.getPlayer(), this.toggle(sign));
                     }
                  }
               }
            }
         }
      }
   }

   private void handleReturnResult(Player player, int result) {
      switch(result) {
      case -11:
         player.sendMessage(ChatColor.RED + "Bridgesigns must be signposts.");
         break;
      case -10:
         player.sendMessage(ChatColor.RED + "Internal error while toggling bridge.");
         break;
      case -9:
         player.sendMessage(ChatColor.RED + "Bridgesigns must be created at an angle divisionable by 90 degrees.");
         break;
      case -8:
         player.sendMessage(ChatColor.RED + "No bridge found.");
         break;
      case -7:
         player.sendMessage(ChatColor.RED + "The bridgewidth of both signs is different.");
      case -6:
      case -5:
      case -4:
      case -3:
      case -2:
      case -1:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      default:
         break;
      case 0:
         player.sendMessage(ChatColor.RED + "This blocktype is not allowed for building bridges.");
         break;
      case 1:
         player.sendMessage(ChatColor.RED + "Bridges must be made out of one material.");
         break;
      case 2:
         player.sendMessage(ChatColor.RED + "Bridgesigns must be more than 1 block away from eachother.");
         break;
      case 8:
         player.sendMessage(ChatColor.GOLD + "Bridge toggled.");
      }

   }

   private boolean isBlockProtected(List blockList) {
      for(int j = 0; j < blockList.size(); ++j) {
         try {
            for(int e = 0; e < this.BridgeAreas.size(); ++e) {
               if(((BridgeArea)this.BridgeAreas.get(e)).isBlockInArea((Block)blockList.get(j))) {
                  return true;
               }
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return false;
   }

   private boolean isBlockProtected(Block block) {
      try {
         for(int e = 0; e < this.BridgeAreas.size(); ++e) {
            if(((BridgeArea)this.BridgeAreas.get(e)).isBlockInArea(block)) {
               return true;
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return false;
   }

   private void saveBridges() {
      File folder = new File("plugins" + System.getProperty("file.separator") + "FalseBook");
      folder.mkdirs();

      try {
         BufferedWriter e = new BufferedWriter(new FileWriter("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "Bridges.db"));
         e.write("BridgesCount=" + this.BridgeAreas.size() + "\r\n");

         for(int i = 0; i < this.BridgeAreas.size(); ++i) {
            String str = "";
            str = ((BridgeArea)this.BridgeAreas.get(i)).getSign1().getBlock().getX() + "," + ((BridgeArea)this.BridgeAreas.get(i)).getSign1().getBlock().getY() + "," + ((BridgeArea)this.BridgeAreas.get(i)).getSign1().getBlock().getZ() + ";";
            str = str + ((BridgeArea)this.BridgeAreas.get(i)).getSign2().getBlock().getX() + "," + ((BridgeArea)this.BridgeAreas.get(i)).getSign2().getBlock().getY() + "," + ((BridgeArea)this.BridgeAreas.get(i)).getSign2().getBlock().getZ() + ";";
            str = str + ((BridgeArea)this.BridgeAreas.get(i)).getUp() + ";";
            str = str + ((BridgeArea)this.BridgeAreas.get(i)).getSign1().getBlock().getWorld().getName();
            e.write(str + "\r\n");
         }

         e.close();
      } catch (IOException var5) {
         FalseBookBlockCore.printInConsole("Error while saving file: plugins/FalseBook/Bridges.db");
         var5.printStackTrace();
      }

   }

   private boolean loadBridges() {
      File f = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "Bridges.db");
      if(f.exists()) {
         try {
            FileInputStream e = new FileInputStream("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "Bridges.db");
            DataInputStream in = new DataInputStream(e);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = br.readLine();
            if(strLine == null) {
               FalseBookBlockCore.printInConsole("No Bridges loaded");
               return false;
            } else {
               String[] split = strLine.split("=");
               int count = Integer.valueOf(split[1]).intValue();

               for(int i = 0; i < count; ++i) {
                  strLine = br.readLine();
                  if(strLine != null) {
                     String[] splitData = strLine.split(";");
                     if(splitData.length == 4) {
                        String[] sign1 = splitData[0].split(",");
                        String[] sign2 = splitData[1].split(",");
                        Boolean isUp = Boolean.valueOf(splitData[2]);
                        String worldName = splitData[3];
                        World thisWorld = Bukkit.getServer().getWorld(worldName);
                        if(thisWorld != null) {
                           Block thisBlock = thisWorld.getBlockAt(Integer.valueOf(sign1[0]).intValue(), Integer.valueOf(sign1[1]).intValue(), Integer.valueOf(sign1[2]).intValue());
                           Block thisBlock2 = thisWorld.getBlockAt(Integer.valueOf(sign2[0]).intValue(), Integer.valueOf(sign2[1]).intValue(), Integer.valueOf(sign2[2]).intValue());
                           if(thisBlock != null && thisBlock2 != null && thisBlock.getType().equals(Material.SIGN_POST) && thisBlock2.getType().equals(Material.SIGN_POST)) {
                              Sign signBlock1 = (Sign)thisBlock.getState();
                              Sign signBlock2 = (Sign)thisBlock2.getState();
                              boolean canInit = true;
                              Point corner1 = new Point(signBlock1.getX(), signBlock1.getZ());
                              Point corner2 = new Point(signBlock2.getX(), signBlock2.getZ());
                              int signDir = SignUtils.getDirection(signBlock1);
                              int bridgeLeft = Parser.getInteger(signBlock1.getLine(2), 1);
                              int bridgeRight = Parser.getInteger(signBlock1.getLine(3), 1);
                              switch(signDir) {
                              case 1:
                                 corner1.x -= bridgeLeft;
                                 corner2.x += bridgeRight;
                                 break;
                              case 2:
                                 corner1.y += bridgeLeft;
                                 corner2.y -= bridgeRight;
                                 break;
                              case 3:
                                 corner1.x += bridgeLeft;
                                 corner2.x -= bridgeRight;
                                 break;
                              case 4:
                                 corner1.y -= bridgeLeft;
                                 corner2.y += bridgeRight;
                                 break;
                              default:
                                 canInit = false;
                              }

                              if(canInit) {
                                 BridgeArea now = new BridgeArea(signBlock1, signBlock2, corner1, corner2);
                                 now.setUp(isUp);
                                 this.BridgeAreas.add(now);
                              }
                           }
                        }
                     }
                  }
               }

               FalseBookBlockCore.printInConsole(this.BridgeAreas.size() + " Bridges successfully loaded.");
               return true;
            }
         } catch (Exception var26) {
            var26.printStackTrace();
            FalseBookBlockCore.printInConsole("Error while reading file: plugins/FalseBook/Bridges.db");
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean isBlockTypeAllowed(int ID, String worldName) {
      return ConfigHandler.getAllowedBridgeBlocks(worldName).hasValue(ID);
   }

   public int toggle(Sign signBlock) {
      if(signBlock.getTypeId() != Material.SIGN_POST.getId()) {
         return -11;
      } else {
         int signDir = SignUtils.getDirection(signBlock);
         if(signDir == -1) {
            return -9;
         } else {
            int left = Parser.getInteger(signBlock.getLine(2), 1);
            int right = Parser.getInteger(signBlock.getLine(3), 1);
            Sign opSign = this.getOppositeSign(signBlock, signDir);
            if(opSign == null) {
               return -8;
            } else {
               int left2nd = Parser.getInteger(opSign.getLine(2), 1);
               int right2nd = Parser.getInteger(opSign.getLine(3), 1);
               if(left == right2nd && right == left2nd) {
                  boolean result = true;
                  int result1 = this.toggle(signBlock, opSign, signBlock.getBlock().getRelative(0, 1, 0), signDir, left, right, 1);
                  if(result1 != 8) {
                     result1 = this.toggle(signBlock, opSign, signBlock.getBlock().getRelative(0, -1, 0), signDir, left, right, -1);
                  }

                  return result1;
               } else {
                  return -7;
               }
            }
         }
      }
   }

   private int toggle(Sign signBlock, Sign opSign, Block originBlock, int signDir, int bridgeLeft, int bridgeRight, int yOffset) {
      if(!this.isBlockTypeAllowed(originBlock.getTypeId(), originBlock.getWorld().getName())) {
         originBlock = null;
         return 0;
      } else {
         Point corner1 = new Point(signBlock.getX(), signBlock.getZ());
         Point corner2 = new Point(opSign.getX(), opSign.getZ());
         boolean keepX = false;
         switch(signDir) {
         case 1:
            corner1.x -= bridgeLeft;
            corner2.x += bridgeRight;
            keepX = false;
            break;
         case 2:
            corner1.y += bridgeLeft;
            corner2.y -= bridgeRight;
            keepX = true;
            break;
         case 3:
            corner1.x += bridgeLeft;
            corner2.x -= bridgeRight;
            keepX = false;
            break;
         case 4:
            corner1.y -= bridgeLeft;
            corner2.y += bridgeRight;
            keepX = true;
            break;
         default:
            return -9;
         }

         ArrayList newArea = this.getArea(signBlock, opSign, signDir, bridgeLeft, bridgeRight, yOffset);
         if(newArea.size() == 0) {
            return 2;
         } else {
            boolean toggleOn = ((Block)newArea.get(0)).getTypeId() != originBlock.getTypeId();
            if(!this.canToggle(originBlock, opSign, corner1, corner2, toggleOn, keepX)) {
               return 1;
            } else {
               World world = originBlock.getWorld();

               int indexInList;
               for(int now = Math.min(corner1.x, corner2.x); now <= Math.max(corner1.x, corner2.x); ++now) {
                  for(indexInList = Math.min(corner1.y, corner2.y); indexInList <= Math.max(corner1.y, corner2.y); ++indexInList) {
                     if(toggleOn) {
                        if(keepX) {
                           if(now != originBlock.getX() && now != opSign.getX()) {
                              world.getBlockAt(now, originBlock.getY(), indexInList).setTypeIdAndData(originBlock.getTypeId(), originBlock.getData(), true);
                           }
                        } else if(indexInList != originBlock.getZ() && indexInList != opSign.getZ()) {
                           world.getBlockAt(now, originBlock.getY(), indexInList).setTypeIdAndData(originBlock.getTypeId(), originBlock.getData(), true);
                        }
                     } else if(keepX) {
                        if(now != originBlock.getX() && now != opSign.getX()) {
                           world.getBlockAt(now, originBlock.getY(), indexInList).setType(Material.AIR);
                        }
                     } else if(indexInList != originBlock.getZ() && indexInList != opSign.getZ()) {
                        world.getBlockAt(now, originBlock.getY(), indexInList).setType(Material.AIR);
                     }
                  }
               }

               BridgeArea var16 = new BridgeArea(signBlock, opSign, corner1, corner2);
               if(originBlock.getY() > signBlock.getY()) {
                  var16.setUp(Boolean.valueOf(true));
               } else {
                  var16.setUp(Boolean.valueOf(false));
               }

               if(!toggleOn) {
                  indexInList = this.isInBridgeList(var16);
                  if(indexInList != -1) {
                     this.BridgeAreas.remove(indexInList);
                  }
               } else if(this.isInBridgeList(var16) == -1) {
                  this.BridgeAreas.add(var16);
               }

               originBlock = null;
               newArea.clear();
               newArea = null;
               return 8;
            }
         }
      }
   }

   public boolean canToggle(Block originBlock, Sign opSign, Point corner1, Point corner2, boolean toggleOn, boolean keepX) {
      World world = originBlock.getWorld();
      boolean ID = false;

      for(int x = Math.min(corner1.x, corner2.x); x <= Math.max(corner1.x, corner2.x); ++x) {
         for(int z = Math.min(corner1.y, corner2.y); z <= Math.max(corner1.y, corner2.y); ++z) {
            int var11 = world.getBlockAt(x, originBlock.getY(), z).getTypeId();
            if(toggleOn) {
               if(keepX) {
                  if(x != originBlock.getX() && x != opSign.getX()) {
                     if(var11 != Material.AIR.getId() && var11 != Material.WATER.getId() && var11 != Material.STATIONARY_WATER.getId() && var11 != Material.LAVA.getId() && var11 != Material.STATIONARY_LAVA.getId()) {
                        return false;
                     }
                  } else if(originBlock.getTypeId() != var11 || originBlock.getData() != world.getBlockAt(x, originBlock.getY(), z).getData()) {
                     return false;
                  }
               } else if(z != originBlock.getZ() && z != opSign.getZ()) {
                  if(var11 != Material.AIR.getId() && var11 != Material.WATER.getId() && var11 != Material.STATIONARY_WATER.getId() && var11 != Material.LAVA.getId() && var11 != Material.STATIONARY_LAVA.getId()) {
                     return false;
                  }
               } else if(originBlock.getTypeId() != var11 || originBlock.getData() != world.getBlockAt(x, originBlock.getY(), z).getData()) {
                  return false;
               }
            } else if(originBlock.getTypeId() != var11 || originBlock.getData() != world.getBlockAt(x, originBlock.getY(), z).getData()) {
               return false;
            }
         }
      }

      return true;
   }

   private ArrayList getArea(Sign firstSign, Sign secondSign, int signDir, int widthLeft, int widthRight, int yDir) {
      ArrayList newArea = new ArrayList();
      Block originBlock = firstSign.getBlock();
      byte flag = 1;
      int bridgeLength;
      int i;
      int right;
      if(signDir != 1 && signDir != 3) {
         if(signDir == 2) {
            flag = -1;
         }

         bridgeLength = Math.abs(firstSign.getBlock().getX() - secondSign.getBlock().getX()) - 1;

         for(i = 1; i <= bridgeLength; ++i) {
            newArea.add(originBlock.getRelative(i * flag, yDir, 0));

            for(right = 1; right <= widthLeft; ++right) {
               newArea.add(originBlock.getRelative(i * flag, yDir, right * flag));
            }

            for(right = 1; right <= widthRight; ++right) {
               newArea.add(originBlock.getRelative(i * flag, yDir, right * flag * -1));
            }
         }
      } else {
         if(signDir == 1) {
            flag = -1;
         }

         bridgeLength = Math.abs(firstSign.getBlock().getZ() - secondSign.getBlock().getZ()) - 1;

         for(i = 1; i <= bridgeLength; ++i) {
            newArea.add(originBlock.getRelative(0, yDir, i * flag));

            for(right = 1; right <= widthLeft; ++right) {
               newArea.add(originBlock.getRelative(right * flag, yDir, i * flag));
            }

            for(right = 1; right <= widthRight; ++right) {
               newArea.add(originBlock.getRelative(right * flag * -1, yDir, i * flag));
            }
         }
      }

      return newArea;
   }

   private Sign getOppositeSign(Sign signBlock, int signDir) {
      Block block = signBlock.getBlock();
      boolean thisID = true;
      byte flag = 1;
      int i;
      Sign opSign;
      int var8;
      if(signDir != 1 && signDir != 3) {
         if(signDir == 2) {
            flag = -1;
         }

         for(i = 2; i <= ConfigHandler.getMaxBridgeLength(signBlock.getWorld().getName()); ++i) {
            var8 = block.getRelative(i * flag, 0, 0).getTypeId();
            if(var8 == Material.WALL_SIGN.getId() || var8 == Material.SIGN_POST.getId()) {
               opSign = (Sign)block.getRelative(i * flag, 0, 0).getState();
               if((signDir == 2 && SignUtils.getDirection(opSign) == 4 || signDir == 4 && SignUtils.getDirection(opSign) == 2) && (opSign.getLine(1).equalsIgnoreCase("[Bridge]") || opSign.getLine(1).equalsIgnoreCase("[Bridge End]"))) {
                  return opSign;
               }
            }
         }
      } else {
         if(signDir == 1) {
            flag = -1;
         }

         for(i = 2; i <= ConfigHandler.getMaxBridgeLength(signBlock.getWorld().getName()) + 1; ++i) {
            var8 = block.getRelative(0, 0, i * flag).getTypeId();
            if(var8 == Material.WALL_SIGN.getId() || var8 == Material.SIGN_POST.getId()) {
               opSign = (Sign)block.getRelative(0, 0, i * flag).getState();
               if((signDir == 1 && SignUtils.getDirection(opSign) == 3 || signDir == 3 && SignUtils.getDirection(opSign) == 1) && (opSign.getLine(1).equalsIgnoreCase("[Bridge]") || opSign.getLine(1).equalsIgnoreCase("[Bridge End]"))) {
                  return opSign;
               }
            }
         }
      }

      return null;
   }

   private int isInBridgeList(BridgeArea a) {
      for(int i = 0; i < this.BridgeAreas.size(); ++i) {
         BridgeArea a2 = (BridgeArea)this.BridgeAreas.get(i);
         if((BlockUtils.LocationEquals(a.getSign1().getBlock().getLocation(), a2.getSign1().getBlock().getLocation()) && BlockUtils.LocationEquals(a.getSign2().getBlock().getLocation(), a2.getSign2().getBlock().getLocation()) || BlockUtils.LocationEquals(a.getSign1().getBlock().getLocation(), a2.getSign2().getBlock().getLocation()) && BlockUtils.LocationEquals(a.getSign2().getBlock().getLocation(), a2.getSign1().getBlock().getLocation())) && a.getUp() == a2.getUp()) {
            return i;
         }
      }

      return -1;
   }
}
