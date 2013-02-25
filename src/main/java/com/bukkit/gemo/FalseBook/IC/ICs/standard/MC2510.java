package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.utils.ICUtils;
import com.bukkit.gemo.utils.SignUtils;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC2510 extends BaseIC {

   public HashMap TaskList = new HashMap();


   public MC2510() {
      this.ICName = "LOW PULSER";
      this.ICNumber = "[MC2510]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Pulsing output", "", "");
      this.chipState.setLines("[pulselength[:startdelay]]", "[[pulsecount][:pauselength in serverticks]]");
      this.ICDescription = "The MC2510 fires a (choosable) pulse of high-signals with a choosable length of the signal and the pause between the pulses when the input goes from high to low.<br /><br />Standard is one pulse with a length of 5 ticks.";
   }

   public void checkCreation(SignChangeEvent event) {
      String[] split;
      int e;
      int val2;
      try {
         split = event.getLine(2).split(":");
         if(split.length == 1) {
            e = Integer.valueOf(split[0]).intValue();
            if(e < 0) {
               e = -e;
               if(e < 1) {
                  e = 1;
               }
            }

            event.setLine(2, "" + e);
         } else if(split.length > 1) {
            e = Integer.valueOf(split[0]).intValue();
            if(e < 0) {
               e = -e;
               if(e < 1) {
                  e = 1;
               }
            }

            val2 = Integer.valueOf(split[1]).intValue();
            if(val2 < 0) {
               val2 = -val2;
               if(val2 < 1) {
                  val2 = 1;
               }
            }

            event.setLine(2, e + ":" + val2);
         }
      } catch (Exception var7) {
         event.setLine(2, "5");
         return;
      }

      if(event.getLine(3).length() > 1) {
         split = event.getLine(3).split(":");
         if(split.length < 2) {
            try {
               e = Integer.valueOf(split[0]).intValue();
               if(e < 0) {
                  e = -e;
                  if(e < 1) {
                     e = 1;
                  }
               }

               event.setLine(3, "" + e);
            } catch (Exception var6) {
               SignUtils.cancelSignCreation(event, "Wrong syntax in Line 4. Use: Pulsecount[:Pauselength] (Integer only)");
               return;
            }
         } else {
            try {
               e = Integer.valueOf(split[0]).intValue();
               if(e < 0) {
                  e = -e;
                  if(e < 1) {
                     e = 1;
                  }
               }

               val2 = Integer.valueOf(split[1]).intValue();
               if(val2 < 0) {
                  val2 = -val2;
               }

               event.setLine(3, e + ":" + val2);
            } catch (Exception var5) {
               SignUtils.cancelSignCreation(event, "Wrong syntax in Line 4. Use: Pulsecount[:Pauselength] (Integer only)");
               return;
            }
         }
      }

   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneLow() && previousInputs.isInputOneHigh()) {
         Iterator pulseCount = this.TaskList.values().iterator();

         while(pulseCount.hasNext()) {
            MC2510.SchedulerClass pulseLength = (MC2510.SchedulerClass)pulseCount.next();
            if(pulseLength.equalsLoc(signBlock.getBlock().getLocation())) {
               return;
            }
         }

         int pulseLength1 = 5;
         int pulseCount1 = 1;
         int pauseLength = 5;
         boolean pulseDelay = true;

         int pulseDelay1;
         try {
            String[] newSched = signBlock.getLine(2).split(":");
            pulseDelay1 = 1;
            if(newSched.length == 1) {
               pulseLength1 = Integer.valueOf(newSched[0]).intValue();
               if(pulseLength1 < 0) {
                  pulseLength1 = -pulseLength1;
                  if(pulseLength1 < 1) {
                     pulseLength1 = 5;
                  }
               }
            } else if(newSched.length > 1) {
               pulseLength1 = Integer.valueOf(newSched[0]).intValue();
               if(pulseLength1 < 0) {
                  pulseLength1 = -pulseLength1;
                  if(pulseLength1 < 1) {
                     pulseLength1 = 5;
                  }
               }

               pulseDelay1 = Integer.valueOf(newSched[1]).intValue();
               if(pulseDelay1 < 0) {
                  pulseDelay1 = -pulseDelay1;
               }
            }

            if(signBlock.getLine(3).length() > 0) {
               String[] split = signBlock.getLine(3).split(":");
               if(split.length < 2) {
                  pulseCount1 = Integer.valueOf(split[0]).intValue();
                  pauseLength = pulseLength1;
               } else {
                  pulseCount1 = Integer.valueOf(split[0]).intValue();
                  pauseLength = Integer.valueOf(split[1]).intValue();
               }
            }
         } catch (Exception var10) {
            pulseLength1 = 5;
            pulseCount1 = 1;
            pauseLength = 5;
            pulseDelay1 = 1;
         }

         MC2510.SchedulerClass newSched1 = new MC2510.SchedulerClass(this, signBlock.getBlock().getLocation(), pulseLength1, pulseCount1, pauseLength);
         newSched1.TaskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(FalseBookICCore.getInstance(), newSched1, (long)pulseDelay1, 1L);
         this.TaskList.put(signBlock.getBlock().getLocation().toString(), newSched1);
      }

   }

   public class SchedulerClass implements Runnable {

      public int TaskID = -1;
      public MC2510 father;
      public Location signLoc;
      public int countedTicks = 0;
      public int pulseLength = 5;
      public int pulseCount = 1;
      public int pauseLength = 5;
      public int curPulse = 0;
      public boolean curState = true;
      public boolean first = true;
      public Sign signBlock = null;


      public SchedulerClass(MC2510 father, Location signLoc, int pulseLength, int pulseCount, int pauseLength) {
         this.father = father;
         this.signLoc = signLoc;
         this.pulseLength = pulseLength;
         this.pulseCount = pulseCount;
         this.pauseLength = pauseLength;
         this.signBlock = (Sign)signLoc.getBlock().getState();
      }

      public void run() {
         if(this.first) {
            ICUtils.switchLever(this.signBlock, true);
            this.first = false;
         }

         ++this.countedTicks;
         if(this.curState && this.pulseLength == this.countedTicks || !this.curState && this.pauseLength == this.countedTicks) {
            this.countedTicks = 0;
            if(this.countedTicks == 0 && this.curState) {
               --this.pulseCount;
            }

            this.curState = !this.curState;

            try {
               ICUtils.switchLever((Sign)this.signLoc.getBlock().getState(), this.curState);
            } catch (Exception var2) {
               this.father.TaskList.remove(this.signLoc.getBlock().getLocation().toString());
               Bukkit.getServer().getScheduler().cancelTask(this.TaskID);
            }
         }

         if(this.pulseCount < 1) {
            this.father.TaskList.remove(this.signLoc.getBlock().getLocation().toString());
            Bukkit.getServer().getScheduler().cancelTask(this.TaskID);
         }

      }

      public boolean equalsLoc(Location loc) {
         return this.signLoc.equals(loc);
      }
   }
}
