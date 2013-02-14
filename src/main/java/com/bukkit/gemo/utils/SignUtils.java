package com.bukkit.gemo.utils;

import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.FBBlockType;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.Parser;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class SignUtils {

   public static void addAdjacentWallSigns(ArrayList list, Block baseBlock) {
      if(baseBlock.getRelative(1, 0, 0).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(1, 0, 0).getData() == 5) {
         list.add((Sign)baseBlock.getRelative(1, 0, 0).getState());
      }

      if(baseBlock.getRelative(-1, 0, 0).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(-1, 0, 0).getData() == 4) {
         list.add((Sign)baseBlock.getRelative(-1, 0, 0).getState());
      }

      if(baseBlock.getRelative(0, 0, 1).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(0, 0, 1).getData() == 3) {
         list.add((Sign)baseBlock.getRelative(0, 0, 1).getState());
      }

      if(baseBlock.getRelative(0, 0, -1).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(0, 0, -1).getData() == 2) {
         list.add((Sign)baseBlock.getRelative(0, 0, -1).getState());
      }

   }

   public static ArrayList getAdjacentWallSigns(Block baseBlock) {
      ArrayList list = new ArrayList();
      if(baseBlock.getRelative(1, 0, 0).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(1, 0, 0).getData() == 5) {
         list.add((Sign)baseBlock.getRelative(1, 0, 0).getState());
      }

      if(baseBlock.getRelative(-1, 0, 0).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(-1, 0, 0).getData() == 4) {
         list.add((Sign)baseBlock.getRelative(-1, 0, 0).getState());
      }

      if(baseBlock.getRelative(0, 0, 1).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(0, 0, 1).getData() == 3) {
         list.add((Sign)baseBlock.getRelative(0, 0, 1).getState());
      }

      if(baseBlock.getRelative(0, 0, -1).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(0, 0, -1).getData() == 2) {
         list.add((Sign)baseBlock.getRelative(0, 0, -1).getState());
      }

      return list;
   }

   public static boolean isSignAnchor(Block baseBlock) {
      return baseBlock.getRelative(1, 0, 0).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(1, 0, 0).getData() == 5?true:(baseBlock.getRelative(-1, 0, 0).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(-1, 0, 0).getData() == 4?true:(baseBlock.getRelative(0, 0, 1).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(0, 0, 1).getData() == 3?true:baseBlock.getRelative(0, 0, -1).getTypeId() == Material.WALL_SIGN.getId() && baseBlock.getRelative(0, 0, -1).getData() == 2));
   }

   public static Location getSignAnchor(Sign signBlock) {
      Location leverPos = signBlock.getBlock().getLocation().clone();
      if(signBlock.getTypeId() == Material.WALL_SIGN.getId()) {
         switch(signBlock.getRawData()) {
         case 2:
            leverPos.setZ(leverPos.getZ() + 1.0D);
            break;
         case 3:
            leverPos.setZ(leverPos.getZ() - 1.0D);
            break;
         case 4:
            leverPos.setX(leverPos.getX() + 1.0D);
            break;
         case 5:
            leverPos.setX(leverPos.getX() - 1.0D);
         }
      } else if(signBlock.getTypeId() == Material.SIGN_POST.getId()) {
         switch(signBlock.getRawData()) {
         case 0:
            leverPos.setZ(leverPos.getZ() - 1.0D);
            break;
         case 4:
            leverPos.setX(leverPos.getX() + 1.0D);
            break;
         case 8:
            leverPos.setZ(leverPos.getZ() + 1.0D);
            break;
         case 12:
            leverPos.setX(leverPos.getX() - 1.0D);
         }
      }

      return leverPos;
   }

   public static int getDirection(Sign signBlock) {
      return signBlock.getTypeId() == Material.WALL_SIGN.getId()?(signBlock.getRawData() == 2?3:(signBlock.getRawData() == 3?1:(signBlock.getRawData() == 4?4:(signBlock.getRawData() == 5?2:-1)))):(signBlock.getTypeId() == Material.SIGN_POST.getId()?(signBlock.getRawData() == 8?3:(signBlock.getRawData() == 0?1:(signBlock.getRawData() == 4?4:(signBlock.getRawData() == 12?2:-1)))):-1);
   }

   public static void cancelSignCreation(SignChangeEvent event, String reason) {
      event.setCancelled(true);
      event.getBlock().setType(Material.AIR);
      ItemStack item = new ItemStack(Material.SIGN, 1);
      event.getPlayer().getInventory().addItem(new ItemStack[]{item});
      ChatUtils.printError(event.getPlayer(), "", reason);
   }

   public static void cancelSignCreation(SignChangeEvent event) {
      event.setCancelled(true);
      event.getBlock().setType(Material.AIR);
      ItemStack item = new ItemStack(Material.SIGN, 1);
      event.getPlayer().getInventory().addItem(new ItemStack[]{item});
   }

   public static ArrayList parseItems(String[] lines, String delimiter, boolean allowAir) {
      ArrayList list = new ArrayList();

      for(int l = 0; l < lines.length; ++l) {
         lines[l] = lines[l].trim();
         int i;
         if(lines[l].equalsIgnoreCase("all")) {
            for(int var14 = allowAir?0:1; var14 < Material.values().length; ++var14) {
               if(Material.values()[var14].getId() != Material.LOG.getId() && Material.values()[var14].getId() != Material.LEAVES.getId()) {
                  if(Material.values()[var14].getId() == Material.WOOL.getId() || Material.values()[var14].getId() == Material.INK_SACK.getId()) {
                     for(i = 0; i < 16; ++i) {
                        list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                     }
                  } else if(Material.values()[var14].getId() != Material.SAPLING.getId()) {
                     if(Material.values()[var14].getId() != Material.STEP.getId() && Material.values()[var14].getId() != Material.DOUBLE_STEP.getId()) {
                        if(Material.values()[var14].getId() == Material.COAL.getId()) {
                           for(i = 0; i < 2; ++i) {
                              list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                           }
                        } else if(Material.values()[var14].getId() != Material.WOOD_HOE.getId() && Material.values()[var14].getId() != Material.WOOD_SPADE.getId() && Material.values()[var14].getId() != Material.WOOD_PICKAXE.getId() && Material.values()[var14].getId() != Material.WOOD_AXE.getId() && Material.values()[var14].getId() != Material.WOOD_SWORD.getId()) {
                           if(Material.values()[var14].getId() != Material.STONE_HOE.getId() && Material.values()[var14].getId() != Material.STONE_SPADE.getId() && Material.values()[var14].getId() != Material.STONE_PICKAXE.getId() && Material.values()[var14].getId() != Material.STONE_AXE.getId() && Material.values()[var14].getId() != Material.STONE_SWORD.getId()) {
                              if(Material.values()[var14].getId() != Material.IRON_HOE.getId() && Material.values()[var14].getId() != Material.IRON_SPADE.getId() && Material.values()[var14].getId() != Material.IRON_PICKAXE.getId() && Material.values()[var14].getId() != Material.IRON_AXE.getId() && Material.values()[var14].getId() != Material.IRON_SWORD.getId()) {
                                 if(Material.values()[var14].getId() != Material.GOLD_HOE.getId() && Material.values()[var14].getId() != Material.GOLD_SPADE.getId() && Material.values()[var14].getId() != Material.GOLD_PICKAXE.getId() && Material.values()[var14].getId() != Material.GOLD_AXE.getId() && Material.values()[var14].getId() != Material.GOLD_SWORD.getId()) {
                                    if(Material.values()[var14].getId() != Material.DIAMOND_HOE.getId() && Material.values()[var14].getId() != Material.DIAMOND_SPADE.getId() && Material.values()[var14].getId() != Material.DIAMOND_PICKAXE.getId() && Material.values()[var14].getId() != Material.DIAMOND_AXE.getId() && Material.values()[var14].getId() != Material.DIAMOND_SWORD.getId()) {
                                       list.add(new FBBlockType(Material.values()[var14].getId(), (short)0));
                                    } else {
                                       for(i = 0; i < 1562; ++i) {
                                          list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                                       }
                                    }
                                 } else {
                                    for(i = 0; i < 33; ++i) {
                                       list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                                    }
                                 }
                              } else {
                                 for(i = 0; i < 251; ++i) {
                                    list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                                 }
                              }
                           } else {
                              for(i = 0; i < 132; ++i) {
                                 list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                              }
                           }
                        } else {
                           for(i = 0; i < 60; ++i) {
                              list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                           }
                        }
                     } else {
                        for(i = 0; i < 6; ++i) {
                           list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                        }
                     }
                  } else {
                     for(i = 0; i < 4; ++i) {
                        list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                     }
                  }
               } else {
                  for(i = 0; i < 3; ++i) {
                     list.add(new FBBlockType(Material.values()[var14].getId(), (byte)i));
                  }
               }
            }

            return list;
         }

         String[] split = lines[l].split(delimiter);

         for(i = 0; i < split.length; ++i) {
            String[] itemSplit = split[i].split(":");
            int j;
            if(itemSplit.length == 1) {
               try {
                  list.add(new FBBlockType(Integer.valueOf(itemSplit[0]).intValue(), (short)0));
               } catch (Exception var13) {
                  for(j = allowAir?0:1; j < Material.values().length; ++j) {
                     if(Material.values()[j].name().equalsIgnoreCase(itemSplit[0])) {
                        list.add(new FBBlockType(Material.values()[j].getId(), (short)0));
                     }
                  }
               }
            } else if(itemSplit.length == 2) {
               try {
                  list.add(new FBBlockType(Integer.valueOf(itemSplit[0]).intValue(), Byte.valueOf(itemSplit[1]).byteValue()));
               } catch (Exception var12) {
                  for(j = allowAir?0:1; j < Material.values().length; ++j) {
                     if(Material.values()[j].name().equalsIgnoreCase(itemSplit[0])) {
                        try {
                           list.add(new FBBlockType(Material.values()[j].getId(), Byte.valueOf(itemSplit[1]).byteValue()));
                        } catch (Exception var11) {
                           return list;
                        }
                     }
                  }
               }
            }
         }
      }

      return list;
   }

   public static ArrayList parseLineToItemListWithSize(String line, String delimiter, boolean allowAir, int minSize, int maxSize) {
      ArrayList list = parseLineToItemList(line, delimiter, allowAir);
      return list != null && list.size() >= minSize && list.size() <= maxSize?list:null;
   }

   public static ArrayList parseLineToItemList(String line, String delimiter, boolean allowAir) {
      ArrayList list = new ArrayList();
      line = line.trim().toLowerCase();
      if(line.equalsIgnoreCase("empty")) {
         return null;
      } else {
         if(line.equalsIgnoreCase("all")) {
            for(int split = allowAir?0:1; split < Material.values().length; ++split) {
               list.add(new FBItemType(Material.values()[split].getId()));
            }
         } else {
            String[] var18 = line.split(delimiter);
            String[] itemSplit = (String[])null;
            String[] var9 = var18;
            int var8 = var18.length;

            for(int var7 = 0; var7 < var8; ++var7) {
               String item = var9[var7];
               String[] splitAmount = item.split("\\*");
               int amount = -1;
               if(splitAmount.length > 1) {
                  if(!Parser.isInteger(splitAmount[0])) {
                     String rangeSplit = splitAmount[0].substring(0, splitAmount[0].length() - 1);
                     if(!splitAmount[0].endsWith("s") || !Parser.isInteger(rangeSplit)) {
                        continue;
                     }

                     splitAmount[0] = "" + 64 * Parser.getInteger(rangeSplit, 1);
                  }

                  amount = Parser.getInteger(splitAmount[0], -1);
                  item = splitAmount[1];
               }

               if(amount < -1) {
                  amount = -1;
               }

               String[] var19 = item.split("\\?");
               if(var19.length == 1) {
                  itemSplit = var19[0].split(":");
                  FBItemType firstID;
                  if(itemSplit.length == 1) {
                     if(Parser.isInteger(itemSplit[0])) {
                        firstID = new FBItemType(Parser.getInteger(itemSplit[0], 0));
                        firstID.setAmount(amount);
                        list.add(firstID);
                     } else {
                        firstID = new FBItemType(itemSplit[0]);
                        firstID.setAmount(amount);
                        if(firstID.getItemID() != 0) {
                           list.add(firstID);
                        }
                     }
                  } else if(itemSplit.length == 2) {
                     if(Parser.isInteger(itemSplit[0]) && Parser.isInteger(itemSplit[1])) {
                        firstID = new FBItemType(Parser.getInteger(itemSplit[0], 0), Short.valueOf(itemSplit[1]).shortValue());
                        firstID.setAmount(amount);
                        list.add(firstID);
                     } else if(Parser.isInteger(itemSplit[1])) {
                        firstID = new FBItemType(itemSplit[0], Short.valueOf(itemSplit[1]).shortValue());
                        firstID.setAmount(amount);
                        if(firstID.getItemID() != 0) {
                           list.add(firstID);
                        }
                     }
                  }
               } else if(var19.length == 2) {
                  int var20 = -1;
                  int secondID = -1;
                  itemSplit = var19[0].split(":");
                  Material mat;
                  if(Parser.isInteger(itemSplit[0])) {
                     var20 = Parser.getInteger(itemSplit[0], -1);
                  } else {
                     mat = Material.getMaterial(itemSplit[0]);
                     if(mat != null) {
                        var20 = mat.getId();
                     }
                  }

                  itemSplit = var19[1].split(":");
                  if(Parser.isInteger(itemSplit[0])) {
                     secondID = Parser.getInteger(itemSplit[0], -1);
                  } else {
                     mat = Material.getMaterial(itemSplit[0]);
                     if(mat != null) {
                        secondID = mat.getId();
                     }
                  }

                  if(var20 != -1 && secondID != -1 && var20 < secondID) {
                     mat = null;

                     for(int i = var20; i <= secondID; ++i) {
                        mat = Material.getMaterial(i);
                        if(mat != null) {
                           FBItemType thisItem = new FBItemType(i);
                           thisItem.setAmount(amount);
                           if(thisItem.getItemID() != 0) {
                              list.add(thisItem);
                           }
                        }
                     }
                  }
               }
            }
         }

         return list;
      }
   }

   public static ArrayList parseLinesToItemList(String[] lines, String delimiter, boolean allowAir) {
      ArrayList list = new ArrayList();
      ArrayList thisList = null;
      String[] var8 = lines;
      int var7 = lines.length;

      for(int var6 = 0; var6 < var7; ++var6) {
         String line = var8[var6];
         thisList = parseLineToItemList(line, delimiter, allowAir);
         if(thisList != null) {
            list.addAll(thisList);
         }
      }

      return list;
   }

   public static ArrayList parseItems(String line, String delimiter, boolean allowAir) {
      ArrayList list = new ArrayList();
      if(line.equalsIgnoreCase("empty")) {
         return null;
      } else {
         line = line.trim();
         int i;
         if(line.equalsIgnoreCase("all")) {
            for(int var13 = allowAir?0:1; var13 < Material.values().length; ++var13) {
               if(Material.values()[var13].getId() != Material.COAL.getId() && Material.values()[var13].getId() != Material.SAPLING.getId() && Material.values()[var13].getId() != Material.LEAVES.getId() && Material.values()[var13].getId() != Material.LOG.getId() && Material.values()[var13].getId() != Material.WOOL.getId() && Material.values()[var13].getId() != Material.STEP.getId()) {
                  list.add(new FBBlockType(Material.values()[var13].getId(), (short)0));
               } else if(Material.values()[var13].getId() != Material.LOG.getId() && Material.values()[var13].getId() != Material.LEAVES.getId()) {
                  if(Material.values()[var13].getId() != Material.WOOL.getId() && Material.values()[var13].getId() != Material.INK_SACK.getId()) {
                     if(Material.values()[var13].getId() != Material.STEP.getId() && Material.values()[var13].getId() != Material.SAPLING.getId()) {
                        if(Material.values()[var13].getId() == Material.COAL.getId()) {
                           for(i = 0; i < 2; ++i) {
                              list.add(new FBBlockType(Material.values()[var13].getId(), (byte)i));
                           }
                        }
                     } else {
                        for(i = 0; i < 4; ++i) {
                           list.add(new FBBlockType(Material.values()[var13].getId(), (byte)i));
                        }
                     }
                  } else {
                     for(i = 0; i < 16; ++i) {
                        list.add(new FBBlockType(Material.values()[var13].getId(), (byte)i));
                     }
                  }
               } else {
                  for(i = 0; i < 3; ++i) {
                     list.add(new FBBlockType(Material.values()[var13].getId(), (byte)i));
                  }
               }
            }

            return list;
         } else {
            String[] split = line.split(delimiter);

            for(i = 0; i < split.length; ++i) {
               String[] itemSplit = split[i].split(":");
               int j;
               if(itemSplit.length == 1) {
                  try {
                     list.add(new FBBlockType(Integer.valueOf(itemSplit[0]).intValue(), (short)0));
                  } catch (Exception var11) {
                     for(j = allowAir?0:1; j < Material.values().length; ++j) {
                        if(Material.values()[j].name().equalsIgnoreCase(itemSplit[0])) {
                           list.add(new FBBlockType(Material.values()[j].getId(), (short)0));
                        }
                     }
                  }
               } else if(itemSplit.length == 2) {
                  try {
                     list.add(new FBBlockType(Integer.valueOf(itemSplit[0]).intValue(), Byte.valueOf(itemSplit[1]).byteValue()));
                  } catch (Exception var12) {
                     for(j = allowAir?0:1; j < Material.values().length; ++j) {
                        if(Material.values()[j].name().equalsIgnoreCase(itemSplit[0])) {
                           try {
                              list.add(new FBBlockType(Material.values()[j].getId(), Byte.valueOf(itemSplit[1]).byteValue()));
                           } catch (Exception var10) {
                              return list;
                           }
                        }
                     }
                  }
               }
            }

            return list;
         }
      }
   }
}
