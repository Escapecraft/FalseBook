package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.SignUtils;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class MC1211 extends BaseIC {

   public MC1211() {
      this.ICName = "SET P-BRIDGE";
      this.ICNumber = "[MC1211]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("BlockIDOn[:SubID][-BlockIDOff[:SubID]] (Examples: \'wool:15-stone\' or \'grass\' or \'dirt-44:2\')", "xOffset,yOffset[,zOffset]:width,depth (0 being the IC block). Example: -1,2,2:3,5");
   }

   public void checkCreation(SignChangeEvent event) {
      ArrayList itemList = SignUtils.parseLineToItemListWithSize(event.getLine(2), "-", true, 1, 2);
      if(itemList == null) {
         SignUtils.cancelSignCreation(event, "Line 3 is not valid. Usage: BlockIDOn[:SubID][-BlockIDOff[:SubID]]");
      } else {
         Iterator bridgePosition = itemList.iterator();

         while(bridgePosition.hasNext()) {
            FBItemType e = (FBItemType)bridgePosition.next();
            if(!BlockUtils.isValidBlock(e.getItemID())) {
               SignUtils.cancelSignCreation(event, "\'" + Material.getMaterial(e.getItemID()).name() + "\' is not a block.");
               return;
            }
         }

         if(event.getLine(3).length() <= 0) {
            SignUtils.cancelSignCreation(event, "Bridge position and size required.");
         } else {
            try {
               String[] e1 = event.getLine(3).split(":");
               if(e1[0].charAt(0) == 108 || e1[0].charAt(0) == 76) {
                  e1[0] = e1[0].substring(1);
               }

               String[] bridgePosition1 = e1[0].split(",");
               Integer.parseInt(bridgePosition1[0]);
               if(bridgePosition1.length == 2) {
                  Integer.parseInt(bridgePosition1[1]);
               }

               if(bridgePosition1.length == 3) {
                  Integer.parseInt(bridgePosition1[1]);
                  Integer.parseInt(bridgePosition1[2]);
               }

               if(e1.length == 2) {
                  String[] doorSize = e1[1].split(",");
                  if(Integer.parseInt(doorSize[0]) < 1) {
                     SignUtils.cancelSignCreation(event, "Bridge width must be more then 0.");
                  } else if(doorSize.length == 2 && Integer.parseInt(doorSize[1]) < 1) {
                     SignUtils.cancelSignCreation(event, "Bridge length must be more then 0.");
                  }
               } else {
                  SignUtils.cancelSignCreation(event, "Bridge size required.");
               }
            } catch (Exception var6) {
               SignUtils.cancelSignCreation(event, "Line 4 is not valid.");
            }
         }
      }
   }

   private static Point FetchPoint(String data, int a) {
      String[] pointData = data.split(",");
      Point point = new Point(a, a);

      try {
         point.x = Integer.parseInt(pointData[0]);
         if(pointData.length == 2) {
            point.y = Integer.parseInt(pointData[1]);
         }

         return point;
      } catch (Exception var5) {
         return null;
      }
   }

   private static Vector FetchVector(String data, int a) {
      String[] pointData = data.split(",");
      Vector point = new Vector(a, a, a);

      try {
         point.setX(Integer.parseInt(pointData[0]));
         if(pointData.length == 2) {
            point.setY(Integer.parseInt(pointData[1]));
            point.setZ(Integer.parseInt("0"));
         }

         if(pointData.length == 3) {
            point.setY(Integer.parseInt(pointData[1]));
            point.setZ(Integer.parseInt(pointData[2]));
         }

         return point;
      } catch (Exception var5) {
         return null;
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      Vector bridgePosition = null;
      Point bridgeSize = null;
      Boolean light = Boolean.valueOf(false);
      if(signBlock.getLine(3) != null) {
         String[] itemList = signBlock.getLine(3).split(":");
         if(itemList[0].charAt(0) == 108 || itemList[0].charAt(0) == 76) {
            light = Boolean.valueOf(true);
            itemList[0] = itemList[0].substring(1);
         }

         bridgePosition = FetchVector(itemList[0], 0);
         if(itemList.length != 2) {
            return;
         }

         bridgeSize = FetchPoint(itemList[1], 1);
      }

      if(bridgePosition != null && bridgeSize != null) {
         if(light.booleanValue()) {
            bridgePosition.setX(bridgePosition.getBlockX() - 1);
            bridgeSize.x += 2;
         }

         ArrayList var20 = SignUtils.parseLineToItemListWithSize(signBlock.getLine(2), "-", true, 1, 2);
         if(var20 != null) {
            Iterator newBlockPositions = var20.iterator();

            while(newBlockPositions.hasNext()) {
               FBItemType basePosition = (FBItemType)newBlockPositions.next();
               if(!BlockUtils.isValidBlock(basePosition.getItemID())) {
                  return;
               }
            }

            if(var20.size() == 1) {
               var20.add(new FBItemType(0));
            }

            Location var21 = getICBlock(signBlock);
            Location[] var22 = new Location[bridgeSize.x * bridgeSize.y];
            int direction = SignUtils.getDirection(signBlock);
            byte hFix = 1;
            byte vFix = 1;
            boolean ruler = false;
            int pos;
            int fY;
            if(direction != 1 && direction != 3) {
               if(direction == 2 || direction == 4) {
                  ruler = true;
                  if(direction == 2) {
                     hFix = -1;
                     vFix = -1;
                  }

                  for(pos = 0; pos < bridgeSize.x; ++pos) {
                     for(fY = 0; fY < bridgeSize.y; ++fY) {
                        var22[pos * bridgeSize.y + fY] = new Location(var21.getWorld(), var21.getX() + (double)((fY + bridgePosition.getBlockZ()) * vFix), var21.getY() + (double)bridgePosition.getBlockY(), var21.getZ() + (double)((pos + bridgePosition.getBlockX()) * hFix));
                     }
                  }
               }
            } else {
               ruler = false;
               if(direction == 3) {
                  hFix = -1;
               } else {
                  vFix = -1;
               }

               for(pos = 0; pos < bridgeSize.x; ++pos) {
                  for(fY = 0; fY < bridgeSize.y; ++fY) {
                     var22[pos * bridgeSize.y + fY] = new Location(var21.getWorld(), var21.getX() + (double)((pos + bridgePosition.getBlockX()) * hFix), var21.getY() + (double)bridgePosition.getBlockY(), var21.getZ() + (double)((fY + bridgePosition.getBlockZ()) * vFix));
                  }
               }
            }

            Location[] var17;
            int var16;
            int z;
            Block b;
            Location var23;
            if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
               var17 = var22;
               var16 = var22.length;

               for(fY = 0; fY < var16; ++fY) {
                  var23 = var17[fY];
                  b = var23.getBlock();
                  if(light.booleanValue()) {
                     if(!ruler) {
                        z = b.getLocation().getBlockX();
                        if((double)z == var21.getX() + (double)(bridgePosition.getBlockX() * hFix) || (double)z == var21.getX() + (double)((bridgePosition.getBlockX() + bridgeSize.x) * hFix) - (double)hFix) {
                           b.setTypeIdAndData(89, (byte)0, true);
                           continue;
                        }
                     } else if(ruler) {
                        z = b.getLocation().getBlockZ();
                        if((double)z == var21.getZ() + (double)(bridgePosition.getBlockX() * hFix) || (double)z == var21.getZ() + (double)((bridgePosition.getBlockX() + bridgeSize.x) * hFix) - (double)hFix) {
                           b.setTypeIdAndData(89, (byte)0, true);
                           continue;
                        }
                     }
                  }

                  b.setTypeIdAndData(((FBItemType)var20.get(0)).getItemID(), ((FBItemType)var20.get(0)).getItemDataAsByte(), true);
               }

               this.switchLever(Lever.BACK, signBlock, true);
            } else if(currentInputs.isInputOneLow() && previousInputs.isInputOneHigh()) {
               var17 = var22;
               var16 = var22.length;

               for(fY = 0; fY < var16; ++fY) {
                  var23 = var17[fY];
                  b = var23.getBlock();
                  if(light.booleanValue()) {
                     if(!ruler) {
                        z = b.getLocation().getBlockX();
                        if((double)z == var21.getX() + (double)(bridgePosition.getBlockX() * hFix) - 1.0D || (double)z == var21.getX() + (double)(bridgePosition.getBlockX() * hFix) + (double)bridgeSize.x) {
                           b.setTypeIdAndData(0, (byte)0, true);
                           continue;
                        }
                     } else if(ruler) {
                        z = b.getLocation().getBlockZ();
                        if((double)z == var21.getZ() + (double)(bridgePosition.getBlockX() * hFix) - 1.0D || (double)z == var21.getZ() + (double)(bridgePosition.getBlockX() * hFix) + (double)bridgeSize.x) {
                           b.setTypeIdAndData(0, (byte)0, true);
                           continue;
                        }
                     }
                  }

                  b.setTypeIdAndData(((FBItemType)var20.get(1)).getItemID(), ((FBItemType)var20.get(1)).getItemDataAsByte(), true);
               }

               this.switchLever(Lever.BACK, signBlock, false);
            }

         }
      }
   }
}
