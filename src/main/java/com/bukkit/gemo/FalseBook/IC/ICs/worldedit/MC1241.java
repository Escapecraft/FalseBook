package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.utils.ICUtils;
import com.bukkit.gemo.utils.SignUtils;
import net.minecraft.server.v1_6_R1.EntityArrow;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_6_R1.CraftWorld;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class MC1241 extends BaseIC {

   public MC1241() {
      this.ICName = "ARROW BARRAGE";
      this.ICNumber = "[MC1241]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("", "", "");
      this.chipState.setLines("Either speed or both speed and spread. Speed is a value between 0.2 and 2 and the default value is 0.6. Spread is a value between 0 and 50 and the default value is 12. Leave blank to use default. <br /><br />Speed[:Spread]", "Vertical velocity between -1 and 1, with 0 being the default. Leave blank to use default of 0.");
      this.ICDescription = "The MC1241 shoots five arrows when the input (the \"clock\") goes from low to high.";
   }

   public void checkCreation(SignChangeEvent event) {
      String speedSpreadLine = event.getLine(2);
      String vertVelLine = event.getLine(3);

      try {
         if(speedSpreadLine.length() > 0) {
            String[] e = speedSpreadLine.split(":");
            float speed = Float.parseFloat(e[0]);
            if((double)speed < 0.3D || speed > 2.0F) {
               SignUtils.cancelSignCreation(event, "Speed must be >= 0.3 and <= 2.");
               return;
            }

            if(e.length > 1) {
               float spread = Float.parseFloat(e[1]);
               if(spread < 0.0F || spread > 50.0F) {
                  SignUtils.cancelSignCreation(event, "Spread must be >= 0 and <= 50.");
                  return;
               }
            }
         }

         if(vertVelLine.length() > 0) {
            float e1 = Float.parseFloat(vertVelLine);
            if(e1 < -1.0F || e1 > 1.0F) {
               SignUtils.cancelSignCreation(event, "Vertical velocity must be between or equal to -1 and 1.");
               return;
            }
         }

      } catch (NumberFormatException var7) {
         SignUtils.cancelSignCreation(event, "Speed is the third line and spread is the fourth line.");
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         int dir = SignUtils.getDirection(signBlock);
         float speed = 0.5F;
         float spread = 12.0F;
         float vertVel = 0.0F;
         Location location = ICUtils.getLeverPos(signBlock).clone();
         location.setX(location.getX() + 0.5D);
         location.setY(location.getY() + 0.5D);
         location.setZ(location.getZ() + 0.5D);
         String speedSpreadLine = signBlock.getLine(2);
         String vertVelLine = signBlock.getLine(3);

         try {
            if(speedSpreadLine.length() > 0) {
               String[] velocity = speedSpreadLine.split(":");
               speed = Float.parseFloat(velocity[0]);
               if(velocity.length > 1) {
                  spread = Float.parseFloat(velocity[1]);
               }
            }

            if(vertVelLine.length() > 0) {
               vertVel = Float.parseFloat(vertVelLine);
            }
         } catch (NumberFormatException var15) {
            return;
         }

         Vector var16 = new Vector(0.0F, 1.0F, 0.0F);
         if(dir == 1) {
            var16 = new Vector(0.0F, vertVel, -1.0F);
         } else if(dir == 2) {
            var16 = new Vector(-1.0F, vertVel, 0.0F);
         } else if(dir == 3) {
            var16 = new Vector(0.0F, vertVel, 1.0F);
         } else if(dir == 4) {
            var16 = new Vector(1.0F, vertVel, 0.0F);
         }

         CraftWorld w = (CraftWorld)signBlock.getWorld();

         for(int i = 0; i <= 4; ++i) {
            EntityArrow arrow = new EntityArrow(w.getHandle());
            arrow.setPositionRotation(location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F);
            arrow.shoot(var16.getX(), var16.getY(), var16.getZ(), speed, spread);
            w.getHandle().addEntity(arrow);
         }
      }

   }
}
