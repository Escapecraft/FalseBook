package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.commands.SuperCommand;
import com.bukkit.gemo.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class cmdSetCommand extends SuperCommand {

   public cmdSetCommand(String pluginName, String syntax, String arguments, String node, boolean hasFunction, Command[] subCommands) {
      super(pluginName, syntax, arguments, node, hasFunction, subCommands);
      this.description = "Change some settings";
   }

   public void execute(String[] args, CommandSender sender) {
      ChatUtils.printLine(sender, ChatColor.AQUA, "-------------- [ FalseBookIC Help ] --------------");
      Command[] commands = this.getSubCommands();
      Command[] var7 = commands;
      int var6 = commands.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         Command command = var7[var5];
         ChatUtils.printLine(sender, ChatColor.GRAY, command.getHelpMessage());
      }

   }

   public void run(String[] args, CommandSender sender) {
      if(!this.runSubCommand(args, sender)) {
         super.run(args, sender);
      }

   }
}
