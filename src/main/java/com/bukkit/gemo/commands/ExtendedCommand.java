package com.bukkit.gemo.commands;

import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class ExtendedCommand extends Command {

   public ExtendedCommand(String syntax, String arguments, String node) {
      super(syntax, arguments, node);
   }

   public ExtendedCommand(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
   }

   public abstract void execute(String[] var1, CommandSender var2);

   public void run(String[] args, CommandSender sender) {
      if(!super.hasRights(sender)) {
         ChatUtils.printError(sender, this.pluginName, "You are not allowed to use this command!");
      } else if(!this.hasCorrectSyntax(args)) {
         ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, this.getHelpMessage());
      } else {
         this.execute(args, sender);
      }
   }

   protected boolean hasCorrectSyntax(String[] args) {
      return args.length >= super.getArgumentCount();
   }
}
