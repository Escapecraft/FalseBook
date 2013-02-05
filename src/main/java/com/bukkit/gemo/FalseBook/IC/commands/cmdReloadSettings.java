package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdReloadSettings extends Command {

   public cmdReloadSettings(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Reload the IC-Settings";
   }

   public void execute(String[] args, CommandSender sender) {
      if(sender instanceof Player) {
         Player player = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.admin.ic")) {
            ChatUtils.printError(player, this.pluginName, "You are not allowed to use this command.");
            return;
         }
      }

      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Reloading IC-Settings...");
      FalseBookICCore.getInstance().loadSettings("FalseBook" + System.getProperty("file.separator") + "FalseBookIC.properties");
      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "IC-Settings reloaded successfully!");
      ChatUtils.printInfo(sender, "", ChatColor.GRAY, "Type /reload to apply the changes.");
   }
}
