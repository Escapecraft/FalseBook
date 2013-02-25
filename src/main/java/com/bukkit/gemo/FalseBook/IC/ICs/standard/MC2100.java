package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.ICUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC2100 extends BaseIC {

   public HashMap TaskList = new HashMap();


   public MC2100() {
      this.ICName = "DELAYER";
      this.ICNumber = "[MC2100]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, false, "Clock", "Reset", "");
      this.chipState.setOutputs("Output", "", "");
      this.chipState.setLines("delay in seconds", "");
      this.ICDescription = "The MC2100 delays a high signal for [x] seconds (Line 3), if the signal is stable.";
   }

   public void checkCreation(SignChangeEvent event) {
      if(!Parser.isInteger(event.getLine(2))) {
         SignUtils.cancelSignCreation(event, "Enter the delay in seconds in line 3.");
      } else {
         event.setLine(2, String.valueOf(Math.abs(Integer.valueOf(event.getLine(2)).intValue())));
         event.setLine(3, "");
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      boolean input1High = currentInputs.isInputOneHigh();
      boolean input2High = currentInputs.isInputTwoHigh();
      MC2100.SchedulerClass sched;
      Iterator newSched;
      if(input1High && !input2High) {
         newSched = this.TaskList.values().iterator();

         while(newSched.hasNext()) {
            sched = (MC2100.SchedulerClass)newSched.next();
            if(sched.equalsLoc(signBlock.getBlock().getLocation())) {
               Bukkit.getServer().getScheduler().cancelTask(sched.TaskID);
               this.TaskList.remove(signBlock.getBlock().getLocation().toString());
               break;
            }
         }

         if(!Parser.isInteger(signBlock.getLine(2))) {
            return;
         }

         int sched1 = Math.abs(Parser.getInteger(signBlock.getLine(2), 1));
         MC2100.SchedulerClass newSched1 = new MC2100.SchedulerClass(this, signBlock.getBlock().getLocation());
         newSched1.TaskID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FalseBookICCore.getInstance(), newSched1, (long)(20 * sched1));
         this.TaskList.put(signBlock.getBlock().getLocation().toString(), newSched1);
      } else if(input2High || !input1High) {
         newSched = this.TaskList.values().iterator();

         while(newSched.hasNext()) {
            sched = (MC2100.SchedulerClass)newSched.next();
            if(sched.equalsLoc(signBlock.getBlock().getLocation())) {
               Bukkit.getServer().getScheduler().cancelTask(sched.TaskID);
               this.TaskList.remove(signBlock.getBlock().getLocation().toString());
               break;
            }
         }

         this.switchLever(Lever.BACK, signBlock, false);
      }

   }

   public class SchedulerClass implements Runnable {

      public int TaskID = -1;
      public MC2100 father;
      public Location signLoc;


      public SchedulerClass(MC2100 father, Location signLoc) {
         this.father = father;
         this.signLoc = signLoc;
      }

      public void run() {
         ICUtils.switchLever((Sign)this.signLoc.getBlock().getState(), true);
         this.father.TaskList.remove(this.signLoc.getBlock().getLocation().toString());
         Bukkit.getServer().getScheduler().cancelTask(this.TaskID);
      }

      public boolean equalsLoc(Location loc) {
         return this.signLoc.equals(loc);
      }
   }
}
