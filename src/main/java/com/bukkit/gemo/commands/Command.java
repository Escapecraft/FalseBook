package com.bukkit.gemo.commands;

import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command {

   public static final String NO_RIGHT = "You are not allowed to use this command!";
   protected String description;
   private String syntax;
   private String arguments;
   protected String permissionNode;
   protected String pluginName;
   private final int argumentCount;


   public Command(String syntax, String arguments, String node) {
      this.description = "";
      this.pluginName = "";
      this.syntax = syntax;
      this.arguments = arguments;
      this.permissionNode = node;
      this.argumentCount = this.countArguments();
   }

   public Command(String pluginName, String syntax, String arguments, String node) {
      this(syntax, arguments, node);
      if(pluginName != null) {
         this.pluginName = pluginName;
      }

   }

   public void run(String[] args, CommandSender sender) {
      if(!this.hasRights(sender)) {
         ChatUtils.printError(sender, this.pluginName, "You are not allowed to use this command!");
      } else if(!this.hasCorrectSyntax(args)) {
         ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, this.getHelpMessage());
      } else {
         this.execute(args, sender);
      }
   }

   public abstract void execute(String[] var1, CommandSender var2);

   protected boolean hasRights(CommandSender sender) {
      return sender instanceof Player?this.permissionNode.length() == 0 || UtilPermissions.playerCanUseCommand((Player)sender, this.getPermissionNode()):true;
   }

   protected boolean hasCorrectSyntax(String[] args) {
      return args.length == this.argumentCount;
   }

   public String getDescription() {
      return this.description;
   }

   public String getHelpMessage(String parentCommand) {
      return ChatColor.DARK_AQUA + (parentCommand.length() > 0?parentCommand + " ":"") + this.getSyntax() + " " + this.getArguments() + ChatColor.GRAY + " : " + this.getDescription();
   }

   public String getHelpMessage() {
      return this.getHelpMessage("");
   }

   public String getSyntax() {
      return this.syntax;
   }

   public String getArguments() {
      return this.arguments;
   }

   public String getPermissionNode() {
      return this.permissionNode;
   }

   public int getArgumentCount() {
      return this.argumentCount;
   }

   private int countArguments() {
      if(this.arguments.isEmpty()) {
         return 0;
      } else {
         int counter = 0;

         for(int i = 0; i < this.arguments.length(); ++i) {
            if(this.arguments.charAt(i) == 60) {
               ++counter;
            }
         }

         return counter;
      }
   }
}
