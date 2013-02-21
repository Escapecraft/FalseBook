package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1511 extends BaseIC {

   public MC1511() {
      this.ICName = "COMMANDSENDER";
      this.ICNumber = "[MC1511]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("first part of the command", "second part of the command");
      this.ICDescription = "The MC1511 executes a configurable serverside command when the input (the \"clock\") goes from low to high.<br /><br />The command is always Line 3 + Line 4.";
   }

   public void checkCreation(SignChangeEvent event) {
      String str = event.getLine(2) + event.getLine(3);
      if(str.length() < 1) {
         SignUtils.cancelSignCreation(event, "Enter a command in line 3 AND/OR 4!");
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         String message = signBlock.getLine(2) + signBlock.getLine(3);
         if(message.length() < 1) {
            return;
         }

         boolean res = Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), message);
         if(!res) {
            Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "[MC1511] " + ChatColor.RED + "Failed to execute command!");
         }

         this.switchLever(Lever.BACK, signBlock, true);
      } else {
         this.switchLever(Lever.BACK, signBlock, false);
      }

   }
}
