package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.block.SignChangeEvent;

public class MC0230 extends SelftriggeredBaseIC {

   private int val1;
   private int val2;
   private boolean result;


   public MC0230() {
      this.setTypeID(5);
      this.ICName = "IS IT DAY";
      this.ICNumber = "[MC0230]";
      this.setICGroup(ICGroup.SELFTRIGGERED);
      this.chipState = new BaseChip(false, false, false, "", "", "");
      this.chipState.setOutputs("Output: High if it is day", "", "");
      this.chipState.setLines("first value in worldticks", "second value in worldticks");
      this.ICDescription = "The MC0230 outputs high if it is day by default (Line 3 and Line 4 blank). Precisely it is high when the server time is less than 13000 ticks. Essentially this means that a low is outputted when it starts getting dark and it goes high after it has been bright for some time.<br />Otherwise it will check if the worldtime is between the specified values.";
   }

   public void checkCreation(SignChangeEvent event) {
      if(event.getLine(2).length() > 1) {
         try {
            Integer.valueOf(event.getLine(2));
         } catch (Exception var4) {
            SignUtils.cancelSignCreation(event, ChatColor.RED + "Line 3 must be a number.");
            return;
         }
      }

      if(event.getLine(3).length() > 1) {
         try {
            Integer.valueOf(event.getLine(3));
         } catch (Exception var3) {
            SignUtils.cancelSignCreation(event, ChatColor.RED + "Line 4 must be a number.");
            return;
         }
      }

   }

   public boolean onLoad(String[] lines) {
      try {
         this.val1 = Integer.valueOf(lines[2]).intValue();
         this.val2 = Integer.valueOf(lines[3]).intValue();
      } catch (Exception var3) {
         this.val1 = 0;
         this.val2 = 13000;
      }

      return true;
   }

   public void Execute() {
      if(this.val1 <= this.val2) {
         this.result = this.signBlock.getWorld().getTime() >= (long)this.val1 && this.signBlock.getWorld().getTime() <= (long)this.val2;
         if(this.result != this.oldStatus) {
            this.oldStatus = this.result;
            this.switchLever(Lever.BACK, this.signBlock, this.result);
         }
      } else if(this.val1 > this.val2) {
         this.result = this.signBlock.getWorld().getTime() >= (long)this.val1 || this.signBlock.getWorld().getTime() <= (long)this.val2;
         if(this.result != this.oldStatus) {
            this.oldStatus = this.result;
            this.switchLever(Lever.BACK, this.signBlock, this.result);
         }
      }

   }
}
