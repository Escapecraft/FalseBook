package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Block.Mechanics.DoorArea;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.Parser;
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
import org.bukkit.util.Vector;

public class MechanicDoor extends MechanicListener {

   private ArrayList DoorAreas = new ArrayList();


   public MechanicDoor(FalseBookBlockCore plugin) {
      plugin.getMechanicHandler().registerEvent(BlockBreakEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonExtendEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonRetractEvent.class, this);
      plugin.getMechanicHandler().registerEvent(SignChangeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityChangeBlockEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityExplodeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(PlayerInteractEvent.class, this);
   }

   public boolean isActivatedByRedstone(Block block, BlockRedstoneEvent event) {
      return ConfigHandler.isRedstoneAllowedForDoors(block.getWorld().getName()) && ConfigHandler.isDoorEnabled(block.getWorld().getName());
   }

   public void onLoad() {
      this.loadDoors();
   }

   public void reloadMechanic() {
      this.saveDoors();
      this.DoorAreas = new ArrayList();
      this.loadDoors();
   }

   public void onUnload() {
      this.saveDoors();
   }

   private int isInDoorList(DoorArea a) {
      for(int i = 0; i < this.DoorAreas.size(); ++i) {
         DoorArea a2 = (DoorArea)this.DoorAreas.get(i);
         if(BlockUtils.LocationEquals(a.getSign1().getBlock().getLocation(), a2.getSign1().getBlock().getLocation()) && BlockUtils.LocationEquals(a.getSign2().getBlock().getLocation(), a2.getSign2().getBlock().getLocation()) || BlockUtils.LocationEquals(a.getSign1().getBlock().getLocation(), a2.getSign2().getBlock().getLocation()) && BlockUtils.LocationEquals(a.getSign2().getBlock().getLocation(), a2.getSign1().getBlock().getLocation())) {
            return i;
         }
      }

      return -1;
   }

   private boolean isBlockProtected(List blockList) {
      for(int j = 0; j < blockList.size(); ++j) {
         try {
            for(int e = 0; e < this.DoorAreas.size(); ++e) {
               if(((DoorArea)this.DoorAreas.get(e)).isBlockInArea((Block)blockList.get(j))) {
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
         for(int e = 0; e < this.DoorAreas.size(); ++e) {
            if(((DoorArea)this.DoorAreas.get(e)).isBlockInArea(block)) {
               return true;
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return false;
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

   public void onBlockBreak(BlockBreakEvent event) {
      if(!event.isCancelled()) {
         if(ConfigHandler.isDoorEnabled(event.getBlock().getWorld().getName())) {
            Block block = event.getBlock();
            if(!block.getType().equals(Material.SIGN_POST) && !block.getType().equals(Material.WALL_SIGN)) {
               boolean isOp1 = UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.destroy.blocks");
               if(this.isBlockProtected(block) && !isOp1) {
                  event.getPlayer().sendMessage(ChatColor.AQUA + "[ FalseBook ] " + ChatColor.RED + "You are not allowed to destroy doorblocks!");
                  event.setCancelled(true);
                  return;
               }
            } else {
               Player isOp = event.getPlayer();
               Sign sign = (Sign)event.getBlock().getState();
               if((sign.getLine(1).equalsIgnoreCase("[Door Up]") || sign.getLine(1).equalsIgnoreCase("[Door Down]")) && this.isBlockProtected(block) && !UtilPermissions.playerCanUseCommand(isOp, "falsebook.destroy.blocks")) {
                  isOp.sendMessage(ChatColor.RED + "You are not allowed to destroy doorsigns.");
                  event.setCancelled(true);
                  return;
               }
            }

         }
      }
   }

   public void onBlockPistonExtend(BlockPistonExtendEvent event) {
      if(!event.isCancelled()) {
         if(ConfigHandler.isDoorEnabled(event.getBlock().getWorld().getName())) {
            if(this.isBlockProtected(event.getBlocks())) {
               event.setCancelled(true);
            }

         }
      }
   }

   public void onBlockPistonRetract(BlockPistonRetractEvent event) {
      if(!event.isCancelled()) {
         if(event.isSticky()) {
            if(ConfigHandler.isDoorEnabled(event.getBlock().getWorld().getName())) {
               if(this.isBlockProtected(event.getRetractLocation().getBlock())) {
                  event.setCancelled(true);
               }

            }
         }
      }
   }

   public void onSignChange(SignChangeEvent event) {
      if(!event.isCancelled()) {
         if(ConfigHandler.isDoorEnabled(event.getBlock().getWorld().getName())) {
            if(event.getLine(1).equalsIgnoreCase("[Door Up]") || event.getLine(1).equalsIgnoreCase("[Door Down]")) {
               Player player = event.getPlayer();
               if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.door")) {
                  SignUtils.cancelSignCreation(event, "You are not allowed to build doors.");
               } else {
                  String worldName = event.getBlock().getWorld().getName();
                  int e;
                  if(event.getLine(1).equalsIgnoreCase("[Door Up]")) {
                     if(event.getBlock().getTypeId() != Material.SIGN_POST.getId()) {
                        SignUtils.cancelSignCreation(event, "Doorsigns must be signposts.");
                        return;
                     }

                     if(event.getBlock().getData() != 0 && event.getBlock().getData() != 4 && event.getBlock().getData() != 8 && event.getBlock().getData() != 12) {
                        SignUtils.cancelSignCreation(event, "Doorsigns may only be created at specific angles (90 degrees).");
                        return;
                     }

                     event.setLine(1, "[Door Up]");
                     if(event.getLine(2).length() > 0) {
                        try {
                           e = Integer.valueOf(event.getLine(2)).intValue();
                           if(e < 0) {
                              SignUtils.cancelSignCreation(event, "Line 3 must be >= 0, or leave it empty.");
                              return;
                           }

                           if(e > ConfigHandler.getMaxDoorSideWidth(worldName)) {
                              SignUtils.cancelSignCreation(event, "Line 3 must be <= " + ConfigHandler.getMaxDoorSideWidth(worldName));
                              return;
                           }

                           if(e == 1) {
                              event.setLine(2, "");
                           }
                        } catch (Exception var8) {
                           SignUtils.cancelSignCreation(event, "Line 3 must be a number. Or leave it empty.");
                           return;
                        }
                     }

                     if(event.getLine(3).length() > 0) {
                        try {
                           e = Integer.valueOf(event.getLine(3)).intValue();
                           if(e < 0) {
                              SignUtils.cancelSignCreation(event, "Line 4 must be >= 0, or leave it empty.");
                              return;
                           }

                           if(e > ConfigHandler.getMaxDoorSideWidth(worldName)) {
                              SignUtils.cancelSignCreation(event, "Line 4 must be <= " + ConfigHandler.getMaxDoorSideWidth(worldName));
                              return;
                           }

                           if(e == 1) {
                              event.setLine(3, "");
                           }
                        } catch (Exception var7) {
                           SignUtils.cancelSignCreation(event, "Line 4 must be a number. Or leave it empty.");
                           return;
                        }
                     }
                  } else if(event.getLine(1).equalsIgnoreCase("[Door Down]") && ConfigHandler.isDoorEnabled(worldName)) {
                     if(event.getBlock().getTypeId() != Material.SIGN_POST.getId()) {
                        SignUtils.cancelSignCreation(event, "Doorsigns must be signposts.");
                        return;
                     }

                     if(event.getBlock().getData() != 0 && event.getBlock().getData() != 4 && event.getBlock().getData() != 8 && event.getBlock().getData() != 12) {
                        SignUtils.cancelSignCreation(event, "Door signs may only be created at specific angles (90 degrees).");
                        return;
                     }

                     event.setLine(1, "[Door Down]");
                     if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.door")) {
                        SignUtils.cancelSignCreation(event, "You are not allowed to build doors.");
                        return;
                     }

                     if(event.getLine(2).length() > 0) {
                        try {
                           e = Integer.valueOf(event.getLine(2)).intValue();
                           if(e < 0) {
                              SignUtils.cancelSignCreation(event, "Line 3 must be >= 0, or leave it empty.");
                              return;
                           }

                           if(e > ConfigHandler.getMaxDoorSideWidth(worldName)) {
                              SignUtils.cancelSignCreation(event, "Line 3 must be <= " + ConfigHandler.getMaxDoorSideWidth(worldName));
                              return;
                           }

                           if(e == 1) {
                              event.setLine(2, "");
                           }
                        } catch (Exception var6) {
                           SignUtils.cancelSignCreation(event, "Line 3 must be a number. Or leave it empty.");
                        }
                     }

                     if(event.getLine(3).length() > 0) {
                        try {
                           e = Integer.valueOf(event.getLine(3)).intValue();
                           if(e < 0) {
                              SignUtils.cancelSignCreation(event, "Line 4 must be >= 0, or leave it empty.");
                              return;
                           }

                           if(e > ConfigHandler.getMaxDoorSideWidth(worldName)) {
                              SignUtils.cancelSignCreation(event, "Line 4 must be <= " + ConfigHandler.getMaxDoorSideWidth(worldName));
                              return;
                           }

                           if(e == 1) {
                              event.setLine(3, "");
                           }
                        } catch (Exception var5) {
                           SignUtils.cancelSignCreation(event, "Line 4 must be a number. Or leave it empty.");
                           return;
                        }
                     }
                  }

                  ChatUtils.printSuccess(player, "[FB-Block]", "Doorsign created.");
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
               if(sign.getLine(1).equalsIgnoreCase("[Door Up]") || sign.getLine(1).equalsIgnoreCase("[Door Down]")) {
                  if(ConfigHandler.isDoorEnabled(block.getWorld().getName())) {
                     event.setUseInteractedBlock(Result.DENY);
                     event.setUseItemInHand(Result.DENY);
                     event.setCancelled(true);
                     if(ConfigHandler.isRespectLWCProtections(event.getPlayer().getWorld().getName()) && !LWCProtection.canAccessWithCModify(event.getPlayer(), block) && !UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.blocks.ignoreLWCProtections")) {
                        event.getPlayer().sendMessage(ChatColor.RED + "This door is protected!");
                     } else {
                        this.handleReturnResult(event.getPlayer(), this.toggle(sign));
                     }
                  }
               }
            }
         }
      }
   }

   private boolean loadDoors() {
      File file = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "Doors.db");
      if(file.exists()) {
         try {
            FileInputStream e = new FileInputStream(file);
            DataInputStream in = new DataInputStream(e);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = br.readLine();
            if(strLine == null) {
               FalseBookBlockCore.printInConsole("No Doors loaded");
               return false;
            } else {
               String[] split = strLine.split("=");
               int count = Integer.valueOf(split[1]).intValue();

               for(int i = 0; i < count; ++i) {
                  strLine = br.readLine();
                  if(strLine != null) {
                     String[] splitData = strLine.split(";");
                     if(splitData.length == 3) {
                        String[] sign1 = splitData[0].split(",");
                        String[] sign2 = splitData[1].split(",");
                        String worldName = splitData[2];
                        World thisWorld = Bukkit.getServer().getWorld(worldName);
                        if(thisWorld != null) {
                           Block thisBlock1 = thisWorld.getBlockAt(Integer.valueOf(sign1[0]).intValue(), Integer.valueOf(sign1[1]).intValue(), Integer.valueOf(sign1[2]).intValue());
                           Block thisBlock2 = thisWorld.getBlockAt(Integer.valueOf(sign2[0]).intValue(), Integer.valueOf(sign2[1]).intValue(), Integer.valueOf(sign2[2]).intValue());
                           if(thisBlock1 != null && thisBlock2 != null && thisBlock1.getType().equals(Material.SIGN_POST) && thisBlock2.getType().equals(Material.SIGN_POST)) {
                              Sign signBlock1 = (Sign)thisBlock1.getState();
                              Sign signBlock2 = (Sign)thisBlock2.getState();
                              Vector corner1 = new Vector(signBlock1.getX(), Math.min(signBlock1.getY(), signBlock2.getY()) + 1, signBlock1.getZ());
                              Vector corner2 = new Vector(signBlock2.getX(), Math.max(signBlock1.getY(), signBlock2.getY()) - 1, signBlock2.getZ());
                              int signDir = SignUtils.getDirection(signBlock1);
                              int signDir2 = SignUtils.getDirection(signBlock2);
                              if((signDir != 1 && signDir != 3 || signDir2 == 1 || signDir2 == 3) && (signDir != 2 && signDir != 4 || signDir2 == 2 || signDir2 == 4)) {
                                 int doorLeft = Parser.getInteger(signBlock1.getLine(2), 1);
                                 int doorRight = Parser.getInteger(signBlock1.getLine(3), 1);
                                 switch(signDir) {
                                 case 1:
                                    corner1.setX(corner1.getBlockX() - doorLeft);
                                    corner2.setX(corner2.getBlockX() + doorRight);
                                    break;
                                 case 2:
                                    corner1.setZ(corner1.getBlockZ() + doorLeft);
                                    corner2.setZ(corner2.getBlockZ() - doorRight);
                                    break;
                                 case 3:
                                    corner1.setX(corner1.getBlockX() - doorLeft);
                                    corner2.setX(corner2.getBlockX() - doorRight);
                                    break;
                                 case 4:
                                    corner1.setZ(corner1.getBlockZ() - doorLeft);
                                    corner2.setZ(corner2.getBlockZ() + doorRight);
                                    break;
                                 default:
                                    continue;
                                 }

                                 DoorArea now = new DoorArea(signBlock1, signBlock2, corner1, corner2);
                                 this.DoorAreas.add(now);
                              }
                           }
                        }
                     }
                  }
               }

               FalseBookBlockCore.printInConsole(this.DoorAreas.size() + " Doors successfully loaded.");
               return true;
            }
         } catch (Exception var25) {
            var25.printStackTrace();
            FalseBookBlockCore.printInConsole("Error while reading file: plugins/FalseBook/Doors.db");
            return false;
         }
      } else {
         return false;
      }
   }

   private void saveDoors() {
      File folder = new File("plugins" + System.getProperty("file.separator") + "FalseBook");
      folder.mkdirs();

      try {
         BufferedWriter e = new BufferedWriter(new FileWriter("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "Doors.db"));
         e.write("DoorCount=" + this.DoorAreas.size() + "\r\n");

         for(int i = 0; i < this.DoorAreas.size(); ++i) {
            String str = "";
            str = ((DoorArea)this.DoorAreas.get(i)).getSign1().getBlock().getX() + "," + ((DoorArea)this.DoorAreas.get(i)).getSign1().getBlock().getY() + "," + ((DoorArea)this.DoorAreas.get(i)).getSign1().getBlock().getZ() + ";";
            str = str + ((DoorArea)this.DoorAreas.get(i)).getSign2().getBlock().getX() + "," + ((DoorArea)this.DoorAreas.get(i)).getSign2().getBlock().getY() + "," + ((DoorArea)this.DoorAreas.get(i)).getSign2().getBlock().getZ() + ";";
            str = str + ((DoorArea)this.DoorAreas.get(i)).getSign1().getBlock().getWorld().getName();
            e.write(str + "\r\n");
         }

         e.close();
      } catch (IOException var5) {
         FalseBookBlockCore.printInConsole("Error while saving file: plugins/FalseBook/Doors.db");
         var5.printStackTrace();
      }

   }

   private boolean isBlockTypeAllowed(int ID, String worldName) {
      return ConfigHandler.getAllowedDoorBlocks(worldName).hasValue(ID);
   }

   private void handleReturnResult(Player player, int result) {
      switch(result) {
      case -12:
         player.sendMessage(ChatColor.RED + "Doorsigns must be created in the same angle.");
         break;
      case -11:
         player.sendMessage(ChatColor.RED + "Doorsigns must be signposts.");
         break;
      case -10:
         player.sendMessage(ChatColor.RED + "Internal error while toggling door.");
         break;
      case -9:
         player.sendMessage(ChatColor.RED + "Doorsigns must be created at an angle divisionable by 90 degrees.");
         break;
      case -8:
         player.sendMessage(ChatColor.RED + "No door found.");
         break;
      case -7:
         player.sendMessage(ChatColor.RED + "The doorwidth of both signs is different.");
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
         player.sendMessage(ChatColor.RED + "This blocktype is not allowed for building doors.");
         break;
      case 1:
         player.sendMessage(ChatColor.RED + "Doors must be made out of one material.");
         break;
      case 2:
         player.sendMessage(ChatColor.RED + "Doorsigns must be more than 1 block away from eachother.");
         break;
      case 8:
         player.sendMessage(ChatColor.GOLD + "Door toggled.");
      }

   }

   public int toggle(Sign signBlock) {
      if(signBlock.getTypeId() != Material.WALL_SIGN.getId() && signBlock.getTypeId() != Material.SIGN_POST.getId()) {
         return -10;
      } else if(signBlock.getTypeId() != Material.SIGN_POST.getId()) {
         return -11;
      } else {
         int signDir = SignUtils.getDirection(signBlock);
         if(signDir == -1) {
            return -9;
         } else {
            int doorLeft = Parser.getInteger(signBlock.getLine(2), 1);
            int doorRight = Parser.getInteger(signBlock.getLine(3), 1);
            Sign opSign = this.getOppositeSign(signBlock, signDir);
            if(opSign == null) {
               return -8;
            } else {
               int door2ndLeft = Parser.getInteger(opSign.getLine(2), 1);
               int door2ndRight = Parser.getInteger(opSign.getLine(3), 1);
               if(doorLeft == door2ndLeft && doorRight == door2ndRight) {
                  int result = -10;
                  if(signBlock.getLine(1).equalsIgnoreCase("[Door Up]")) {
                     result = this.toggle(signBlock, opSign, signBlock.getBlock().getRelative(0, 1, 0), signDir, doorLeft, doorRight);
                  } else if(signBlock.getLine(1).equalsIgnoreCase("[Door Down]")) {
                     result = this.toggle(signBlock, opSign, signBlock.getBlock().getRelative(0, -1, 0), signDir, doorLeft, doorRight);
                  }

                  return result;
               } else {
                  return -7;
               }
            }
         }
      }
   }

   private int toggle(Sign signBlock, Sign opSign, Block originBlock, int signDir, int doorLeft, int doorRight) {
      if(!this.isBlockTypeAllowed(originBlock.getTypeId(), originBlock.getWorld().getName())) {
         originBlock = null;
         return 0;
      } else {
         Vector corner1 = new Vector(signBlock.getX(), Math.min(signBlock.getY(), opSign.getY()) + 1, signBlock.getZ());
         Vector corner2 = new Vector(opSign.getX(), Math.max(signBlock.getY(), opSign.getY()) - 1, opSign.getZ());
         switch(signDir) {
         case 1:
            corner1.setX(corner1.getBlockX() - doorLeft);
            corner2.setX(corner2.getBlockX() + doorRight);
            break;
         case 2:
            corner1.setZ(corner1.getBlockZ() + doorLeft);
            corner2.setZ(corner2.getBlockZ() - doorRight);
            break;
         case 3:
            corner1.setX(corner1.getBlockX() + doorLeft);
            corner2.setX(corner2.getBlockX() - doorRight);
            break;
         case 4:
            corner1.setZ(corner1.getBlockZ() - doorLeft);
            corner2.setZ(corner2.getBlockZ() + doorRight);
            break;
         default:
            return -9;
         }

         ArrayList newArea = this.getArea(signBlock, opSign, signDir, doorLeft, doorRight);
         if(newArea.size() == 0) {
            return 2;
         } else {
            boolean toggleOn = ((Block)newArea.get(0)).getTypeId() != originBlock.getTypeId();
            if(!this.canToggle(originBlock, opSign, corner1, corner2, toggleOn)) {
               return 1;
            } else {
               World world = originBlock.getWorld();

               int indexInList;
               for(int now = Math.min(corner1.getBlockX(), corner2.getBlockX()); now <= Math.max(corner1.getBlockX(), corner2.getBlockX()); ++now) {
                  for(indexInList = Math.min(corner1.getBlockZ(), corner2.getBlockZ()); indexInList <= Math.max(corner1.getBlockZ(), corner2.getBlockZ()); ++indexInList) {
                     for(int y = Math.min(corner1.getBlockY(), corner2.getBlockY()) + 1; y < Math.max(corner1.getBlockY(), corner2.getBlockY()); ++y) {
                        if(toggleOn) {
                           world.getBlockAt(now, y, indexInList).setTypeIdAndData(originBlock.getTypeId(), originBlock.getData(), true);
                        } else {
                           world.getBlockAt(now, y, indexInList).setType(Material.AIR);
                        }
                     }
                  }
               }

               DoorArea var15 = new DoorArea(signBlock, opSign, corner1, corner2);
               if(!toggleOn) {
                  indexInList = this.isInDoorList(var15);
                  if(indexInList != -1) {
                     this.DoorAreas.remove(indexInList);
                  }
               } else if(this.isInDoorList(var15) == -1) {
                  this.DoorAreas.add(var15);
               }

               originBlock = null;
               newArea.clear();
               newArea = null;
               return 8;
            }
         }
      }
   }

   public boolean canToggle(Block originBlock, Sign opSign, Vector corner1, Vector corner2, boolean toggleOn) {
      World world = originBlock.getWorld();
      boolean ID = false;

      for(int x = Math.min(corner1.getBlockX(), corner2.getBlockX()); x <= Math.max(corner1.getBlockX(), corner2.getBlockX()); ++x) {
         for(int z = Math.min(corner1.getBlockZ(), corner2.getBlockZ()); z <= Math.max(corner1.getBlockZ(), corner2.getBlockZ()); ++z) {
            for(int y = Math.min(corner1.getBlockY(), corner2.getBlockY()); y <= Math.max(corner1.getBlockY(), corner2.getBlockY()); ++y) {
               if(toggleOn) {
                  int var11 = world.getBlockAt(x, y, z).getTypeId();
                  if(y != corner1.getBlockY() && y != corner2.getBlockY()) {
                     if(var11 != Material.AIR.getId() && var11 != Material.WATER.getId() && var11 != Material.STATIONARY_WATER.getId() && var11 != Material.LAVA.getId() && var11 != Material.STATIONARY_LAVA.getId()) {
                        return false;
                     }
                  } else if(originBlock.getTypeId() != world.getBlockAt(x, y, z).getTypeId() || originBlock.getData() != world.getBlockAt(x, y, z).getData()) {
                     return false;
                  }
               } else if(originBlock.getTypeId() != world.getBlockAt(x, y, z).getTypeId() || originBlock.getData() != world.getBlockAt(x, y, z).getData()) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private ArrayList getArea(Sign firstSign, Sign secondSign, int signDir, int widthLeft, int widthRight) {
      ArrayList newArea = new ArrayList();
      Block originBlock = firstSign.getBlock();
      Block originBlock2nd = secondSign.getBlock();
      byte flagX = 0;
      byte flagZ = 0;
      byte noteFlag = 1;
      if(signDir == 1) {
         flagX = 1;
         noteFlag = -1;
      } else if(signDir == 3) {
         flagX = -1;
         noteFlag = -1;
      } else if(signDir == 2) {
         flagZ = -1;
         noteFlag = -1;
      } else if(signDir == 4) {
         flagZ = 1;
         noteFlag = -1;
      }

      int doorHeight = Math.abs(originBlock.getY() - originBlock2nd.getY()) - 2;
      Block downBlock = firstSign.getBlock();
      if(firstSign.getY() > secondSign.getY()) {
         downBlock = secondSign.getBlock();
      }

      for(int i = 2; i < doorHeight + 1; ++i) {
         newArea.add(downBlock.getRelative(0, i, 0));

         int right;
         for(right = 1; right <= widthLeft; ++right) {
            newArea.add(downBlock.getRelative(right * flagX * noteFlag, i, right * flagZ * noteFlag));
         }

         for(right = 1; right <= widthRight; ++right) {
            newArea.add(downBlock.getRelative(right * -flagX * noteFlag, i, right * -flagZ * noteFlag));
         }
      }

      return newArea;
   }

   private Sign getOppositeSign(Sign signBlock, int signDir) {
      Block block = signBlock.getBlock();
      boolean thisID = true;
      int length;
      int i;
      Sign opSign;
      int var8;
      if(signBlock.getLine(1).equalsIgnoreCase("[Door Up]")) {
         length = 1;

         for(i = 2; i <= block.getWorld().getMaxHeight() && block.getY() + i <= block.getWorld().getMaxHeight() && length <= ConfigHandler.getMaxDoorHeight(signBlock.getWorld().getName()); ++i) {
            ++length;
            var8 = block.getRelative(0, i, 0).getTypeId();
            if(var8 == Material.WALL_SIGN.getId() || var8 == Material.SIGN_POST.getId()) {
               opSign = (Sign)block.getRelative(0, i, 0).getState();
               if(signDir == SignUtils.getDirection(opSign) && opSign.getLine(1).equalsIgnoreCase("[Door Down]")) {
                  return opSign;
               }
            }
         }
      } else if(signBlock.getLine(1).equalsIgnoreCase("[Door Down]")) {
         length = 1;

         for(i = 2; i <= block.getWorld().getMaxHeight() && block.getY() - i >= 0 && length <= ConfigHandler.getMaxDoorHeight(signBlock.getWorld().getName()); ++i) {
            var8 = block.getRelative(0, -i, 0).getTypeId();
            ++length;
            if(var8 == Material.WALL_SIGN.getId() || var8 == Material.SIGN_POST.getId()) {
               opSign = (Sign)block.getRelative(0, -i, 0).getState();
               if(signDir == SignUtils.getDirection(opSign) && opSign.getLine(1).equalsIgnoreCase("[Door Up]")) {
                  return opSign;
               }
            }
         }
      }

      return null;
   }
}
