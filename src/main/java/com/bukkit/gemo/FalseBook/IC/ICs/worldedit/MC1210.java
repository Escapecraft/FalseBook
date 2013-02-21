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

public class MC1210 extends BaseIC {

   public MC1210() {
      this.ICName = "SET P-DOOR";
      this.ICNumber = "[MC1210]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("BlockIDOn[:SubID][-BlockIDOff[:SubID]] (Examples: \'wool:15-stone\' or \'grass\' or \'dirt-44:2\')", "xOffset,yOffset[,zOffset]:width,height (0 being the IC block). Example: -1,2,2:3,5");
   }

   public void checkCreation(SignChangeEvent event) {
      ArrayList itemList = SignUtils.parseLineToItemListWithSize(event.getLine(2), "-", true, 1, 2);
      if(itemList == null) {
         SignUtils.cancelSignCreation(event, "Line 3 is not valid. Usage: BlockIDOn[:SubID][-BlockIDOff[:SubID]]");
      } else {
         Iterator doorPosition = itemList.iterator();

         while(doorPosition.hasNext()) {
            FBItemType e = (FBItemType)doorPosition.next();
            if(!BlockUtils.isValidBlock(e.getItemID())) {
               SignUtils.cancelSignCreation(event, "\'" + Material.getMaterial(e.getItemID()).name() + "\' is not a block.");
               return;
            }
         }

         if(event.getLine(3).length() > 0) {
            try {
               String[] e1 = event.getLine(3).split(":");
               String[] doorPosition1 = e1[0].split(",");
               Integer.parseInt(doorPosition1[0]);
               if(doorPosition1.length == 2) {
                  Integer.parseInt(doorPosition1[1]);
               }

               if(doorPosition1.length == 3) {
                  Integer.parseInt(doorPosition1[1]);
                  Integer.parseInt(doorPosition1[2]);
               }

               if(e1.length == 2) {
                  String[] doorSize = e1[1].split(",");
                  if(Integer.parseInt(doorSize[0]) < 1) {
                     SignUtils.cancelSignCreation(event, "Door width must be more then 0.");
                  } else if(doorSize.length == 2 && Integer.parseInt(doorSize[1]) < 1) {
                     SignUtils.cancelSignCreation(event, "Door height must be more then 0.");
                  }
               } else {
                  SignUtils.cancelSignCreation(event, "Door size required.");
               }
            } catch (Exception var6) {
               SignUtils.cancelSignCreation(event, "Line 4 is not valid.");
            }
         } else {
            SignUtils.cancelSignCreation(event, "Door position and size required.");
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
      Vector doorPosition = null;
      Point doorSize = null;
      if(signBlock.getLine(3) != null) {
         String[] itemList = signBlock.getLine(3).split(":");
         doorPosition = FetchVector(itemList[0], 0);
         if(itemList.length == 2) {
            doorSize = FetchPoint(itemList[1], 1);
         }
      }

      if(doorPosition != null && doorSize != null) {
         ArrayList var17 = SignUtils.parseLineToItemListWithSize(signBlock.getLine(2), "-", true, 1, 2);
         if(var17 != null) {
            Iterator newBlockPositions = var17.iterator();

            while(newBlockPositions.hasNext()) {
               FBItemType basePosition = (FBItemType)newBlockPositions.next();
               if(!BlockUtils.isValidBlock(basePosition.getItemID())) {
                  return;
               }
            }

            if(var17.size() == 1) {
               var17.add(new FBItemType(0));
            }

            Location var18 = getICBlock(signBlock);
            Location[] var19 = new Location[doorSize.x * doorSize.y];
            int direction = SignUtils.getDirection(signBlock);
            byte hFix = 1;
            byte zFix = -1;
            int pos;
            int y;
            if(direction != 1 && direction != 3) {
               if(direction == 2 || direction == 4) {
                  zFix = 1;
                  if(direction == 2) {
                     hFix = -1;
                     zFix = -1;
                  }

                  for(pos = 0; pos < doorSize.x; ++pos) {
                     for(y = 0; y < doorSize.y; ++y) {
                        var19[pos * doorSize.y + y] = new Location(var18.getWorld(), var18.getX() + (double)(doorPosition.getBlockZ() * zFix), var18.getY() + (double)y + (double)doorPosition.getBlockY(), var18.getZ() + (double)((pos + doorPosition.getBlockX()) * hFix));
                     }
                  }
               }
            } else {
               if(direction == 3) {
                  hFix = -1;
                  zFix = 1;
               }

               for(pos = 0; pos < doorSize.x; ++pos) {
                  for(y = 0; y < doorSize.y; ++y) {
                     var19[pos * doorSize.y + y] = new Location(var18.getWorld(), var18.getX() + (double)((pos + doorPosition.getBlockX()) * hFix), var18.getY() + (double)y + (double)doorPosition.getBlockY(), var18.getZ() + (double)(doorPosition.getBlockZ() * zFix));
                  }
               }
            }

            int var14;
            Location[] var15;
            Block b;
            Location var20;
            if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
               var15 = var19;
               var14 = var19.length;

               for(y = 0; y < var14; ++y) {
                  var20 = var15[y];
                  b = var20.getBlock();
                  if(!BlockUtils.LocationEquals(b.getLocation(), signBlock.getBlock().getLocation())) {
                     b.setTypeIdAndData(((FBItemType)var17.get(0)).getItemID(), ((FBItemType)var17.get(0)).getItemDataAsByte(), true);
                  }
               }

               this.switchLever(Lever.BACK, signBlock, true);
            } else if(currentInputs.isInputOneLow() && previousInputs.isInputOneHigh()) {
               var15 = var19;
               var14 = var19.length;

               for(y = 0; y < var14; ++y) {
                  var20 = var15[y];
                  b = var20.getBlock();
                  if(!BlockUtils.LocationEquals(b.getLocation(), signBlock.getBlock().getLocation())) {
                     b.setTypeIdAndData(((FBItemType)var17.get(1)).getItemID(), ((FBItemType)var17.get(1)).getItemDataAsByte(), true);
                  }
               }

               this.switchLever(Lever.BACK, signBlock, false);
            }

         }
      }
   }
}
