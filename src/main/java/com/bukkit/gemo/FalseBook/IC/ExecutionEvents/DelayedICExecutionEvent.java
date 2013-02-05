package com.bukkit.gemo.FalseBook.IC.ExecutionEvents;

import com.bukkit.gemo.FalseBook.IC.ICs.IC;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class DelayedICExecutionEvent implements Runnable {

   private IC thisIC = null;
   private Sign signBlock = null;
   private InputState oldInputs;


   public DelayedICExecutionEvent(IC thisIC, Sign signBlock, InputState oldInputs) {
      this.thisIC = thisIC;
      this.signBlock = signBlock;
      this.oldInputs = oldInputs;
   }

   public Block getSignBlock() {
      return this.signBlock.getBlock();
   }

   public void run() {
      try {
         this.thisIC.Execute(this.signBlock, new InputState(this.signBlock), this.oldInputs);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
