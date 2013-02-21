package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import java.util.Random;
import org.bukkit.event.block.SignChangeEvent;

public class MC0020 extends SelftriggeredBaseIC {

   private Random rGen = new Random();


   public MC0020() {
      this.setTypeID(0);
      this.setICName("RANDOM BIT");
      this.setICNumber("[MC0020]");
      this.setICGroup(ICGroup.SELFTRIGGERED);
      this.chipState = new BaseChip(false, false, false, "", "", "");
      this.chipState.setOutputs("Random bit", "", "");
      this.ICDescription = "The MC0020 generates a random state every X serverticks.";
   }

   public void Execute() {
      boolean newStatus = this.rGen.nextBoolean();
      if(newStatus != this.oldStatus) {
         this.oldStatus = newStatus;
         this.switchLever(Lever.BACK, this.signBlock, newStatus);
      }

   }

   public void checkCreation(SignChangeEvent event) {
      event.setLine(2, "");
      event.setLine(3, "");
   }
}
