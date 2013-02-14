package com.bukkit.gemo.FalseBook.Block.commands;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Areas.Area;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicArea;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdFAreaToggle extends Command {

   public cmdFAreaToggle(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Toggle options";
   }

   public void execute(String[] args, CommandSender sender) {
      if(sender instanceof Player) {
         Player mechanic = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(mechanic, "falsebook.blocks.area")) {
            ChatUtils.printError(mechanic, this.pluginName, "You are not allowed to use this command.");
            return;
         }
      }

      MechanicArea var7 = (MechanicArea)FalseBookBlockCore.getInstance().getMechanicHandler().getMechanic("AREA");
      String areaName = args[1];
      boolean f;
      int i;
      if(args[0].equalsIgnoreCase("AutoSave")) {
         f = false;

         for(i = var7.getAreas().size() - 1; i >= 0; --i) {
            if(((Area)var7.getAreas().get(i)).getAreaName().equalsIgnoreCase(areaName)) {
               ((Area)var7.getAreas().get(i)).setAutoSave(!((Area)var7.getAreas().get(i)).isAutoSave());
               var7.saveAreas(areaName, true);
               ChatUtils.printSuccess(sender, this.pluginName, "Changed Autosave from Area \'" + areaName + "\' to: " + ((Area)var7.getAreas().get(i)).isAutoSave());
               f = true;
            }
         }

         if(!f) {
            ChatUtils.printError(sender, this.pluginName, "Area \'" + areaName + "\' not found!");
         }
      } else if(args[0].equalsIgnoreCase("protect")) {
         f = false;

         for(i = var7.getAreas().size() - 1; i >= 0; --i) {
            if(((Area)var7.getAreas().get(i)).getAreaName().equalsIgnoreCase(areaName)) {
               ((Area)var7.getAreas().get(i)).setProtect(!((Area)var7.getAreas().get(i)).isProtect());
               var7.saveAreas(areaName, true);
               ChatUtils.printSuccess(sender, this.pluginName, "Changed Protection from Area \'" + areaName + "\' to: " + ((Area)var7.getAreas().get(i)).isProtect());
               f = true;
            }
         }

         if(!f) {
            ChatUtils.printError(sender, this.pluginName, "Area \'" + areaName + "\' not found!");
         }
      } else if(args[0].equalsIgnoreCase("interact")) {
         f = false;

         for(i = var7.getAreas().size() - 1; i >= 0; --i) {
            if(((Area)var7.getAreas().get(i)).getAreaName().equalsIgnoreCase(areaName)) {
               ((Area)var7.getAreas().get(i)).setInteractBlocked(!((Area)var7.getAreas().get(i)).isInteractBlocked());
               var7.saveAreas(areaName, true);
               ChatUtils.printSuccess(sender, this.pluginName, "Changed Interact-Protection from Area \'" + areaName + "\' to: " + ((Area)var7.getAreas().get(i)).isInteractBlocked());
               f = true;
            }
         }

         if(!f) {
            ChatUtils.printError(sender, this.pluginName, "Area \'" + areaName + "\' not found!");
         }
      } else {
         ChatUtils.printError(sender, this.pluginName, "Wrong syntax!");
         ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, this.getHelpMessage());
      }

   }
}
