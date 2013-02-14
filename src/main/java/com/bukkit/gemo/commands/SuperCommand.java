package com.bukkit.gemo.commands;

import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class SuperCommand extends Command {

   private Command[] subCommands;
   private boolean hasFunction;
   private String pluginName;


   public SuperCommand(String syntax, String arguments, String node, boolean hasFunction, Command ... subCommands) {
      super(syntax, arguments, node);
      this.pluginName = "";
      this.hasFunction = hasFunction;
      this.subCommands = subCommands;
   }

   public SuperCommand(String pluginName, String syntax, String arguments, String node, boolean hasFunction, Command ... subCommands) {
      this(syntax, arguments, node, hasFunction, subCommands);
      if(pluginName != null) {
         this.pluginName = pluginName;
      }

   }

   public SuperCommand(String syntax, String arguments, String node, Command ... subCommands) {
      this(syntax, arguments, node, false, subCommands);
   }

   public abstract void execute(String[] var1, CommandSender var2);

   public void run(String[] args, CommandSender sender) {
      if(args.length == 0) {
         if(this.hasFunction) {
            ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, this.getHelpMessage());
         } else {
            this.printSubcommands(sender);
         }

      } else {
         if(!this.runSubCommand(args, sender)) {
            super.run(args, sender);
         }

      }
   }

   private void printSubcommands(CommandSender sender) {
      ChatUtils.printInfo(sender, this.pluginName, ChatColor.GOLD, "Possible commands:");
      Command[] var5;
      int var4 = (var5 = this.getSubCommands()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Command command = var5[var3];
         ChatUtils.printLine(sender, ChatColor.GRAY, command.getHelpMessage(this.getSyntax()));
      }

   }

   protected boolean runSubCommand(String[] args, CommandSender sender) {
      if(args != null && args.length == 0) {
         return false;
      } else {
         Command[] var6 = this.subCommands;
         int var5 = this.subCommands.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            Command com = var6[var4];
            if(com.getSyntax().equalsIgnoreCase(args[0])) {
               com.run((String[])Arrays.copyOfRange(args, 1, args.length), sender);
               return true;
            }
         }

         return false;
      }
   }

   protected Command[] getSubCommands() {
      return this.subCommands;
   }
}
