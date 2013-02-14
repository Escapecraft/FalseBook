package com.bukkit.gemo.commands;

import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.commands.ExtendedCommand;
import com.bukkit.gemo.commands.SuperCommand;
import com.bukkit.gemo.utils.ChatUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandList {

   private HashMap commandList;
   private String pluginName;


   public CommandList(Command[] commands) {
      this.pluginName = "";
      this.initCommandList(commands);
   }

   public CommandList(String pluginName, Command[] commands) {
      this(commands);
      if(pluginName != null) {
         this.pluginName = pluginName;
      }

   }

   public void handleCommand(CommandSender sender, String label, String[] args) {
      if(!label.startsWith("/")) {
         label = "/" + label;
      }

      Command cmd = (Command)this.commandList.get(label + "_" + args.length);
      if(cmd != null) {
         cmd.run(args, sender);
      } else {
         cmd = (Command)this.commandList.get(label);
         if(cmd != null) {
            cmd.run(args, sender);
         } else {
            ChatUtils.printError(sender, this.pluginName, "Command \'" + label + "\' not found.");
            LinkedList cmdList = new LinkedList();
            Iterator var7 = this.commandList.entrySet().iterator();

            while(var7.hasNext()) {
               Entry command = (Entry)var7.next();
               if(((String)command.getKey()).startsWith(label)) {
                  cmdList.add((Command)command.getValue());
               }
            }

            var7 = cmdList.iterator();

            while(var7.hasNext()) {
               Command command1 = (Command)var7.next();
               ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, command1.getSyntax() + " " + command1.getArguments());
            }
         }
      }

   }

   private void initCommandList(Command[] cmds) {
      this.commandList = new HashMap();
      Command[] var5 = cmds;
      int var4 = cmds.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Command cmd = var5[var3];
         String key = "";
         if(!(cmd instanceof ExtendedCommand) && !(cmd instanceof SuperCommand)) {
            key = cmd.getSyntax() + "_" + cmd.getArgumentCount();
         } else {
            key = cmd.getSyntax();
         }

         this.commandList.put(key, cmd);
      }

   }
}
