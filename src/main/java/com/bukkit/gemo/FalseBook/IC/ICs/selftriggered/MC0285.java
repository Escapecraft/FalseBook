package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.block.SignChangeEvent;

public class MC0285 extends SelftriggeredBaseIC {

   private World myWorld;
   private int mode = -1;
   private boolean isRain = false;
   private boolean isThunder = false;


   public MC0285() {
      this.setTypeID(17);
      this.ICName = "SET WEATHER";
      this.ICNumber = "[MC0285]";
      this.setICGroup(ICGroup.SELFTRIGGERED);
      this.chipState = new BaseChip(false, false, false, "", "", "");
      this.chipState.setOutputs("", "", "");
      this.chipState.setLines("SUN or RAIN or STORM", "");
      this.ICDescription = "The MC0285 sets the weather to the specified weather, whenever the weather changes.";
   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(3, "");
      String line = event.getLine(2);
      if(!line.equalsIgnoreCase("sun") && !line.equalsIgnoreCase("rain") && !line.equalsIgnoreCase("storm")) {
         SignUtils.cancelSignCreation(event, ChatColor.RED + "Line 3 must be sun, rain or storm");
      } else {
         event.setLine(2, line.toUpperCase());
      }
   }

   public boolean onLoad(String[] lines) {
      this.myWorld = this.signBlock.getWorld();
      String line = lines[2];
      if(!line.equalsIgnoreCase("sun") && !line.equalsIgnoreCase("rain") && !line.equalsIgnoreCase("storm")) {
         this.mode = -1;
         return false;
      } else {
         if(line.equalsIgnoreCase("sun")) {
            this.mode = 0;
         } else if(line.equalsIgnoreCase("rain")) {
            this.mode = 1;
         } else if(line.equalsIgnoreCase("storm")) {
            this.mode = 2;
         }

         return true;
      }
   }

   public void Execute() {
      this.isRain = this.myWorld.hasStorm();
      this.isThunder = this.myWorld.isThundering();
      if(this.mode == 0) {
         if(this.isRain || this.isThunder) {
            this.signBlock.getWorld().setStorm(false);
            this.signBlock.getWorld().setThundering(false);
         }
      } else if(this.mode == 1) {
         if(!this.isRain || this.isThunder) {
            this.signBlock.getWorld().setStorm(true);
            this.signBlock.getWorld().setThundering(false);
         }
      } else if(this.mode == 2 && (!this.isRain || !this.isThunder)) {
         this.signBlock.getWorld().setStorm(true);
         this.signBlock.getWorld().setThundering(true);
      }

   }
}
