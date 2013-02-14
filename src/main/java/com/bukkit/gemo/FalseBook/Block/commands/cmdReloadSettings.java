package com.bukkit.gemo.FalseBook.Block.commands;

import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdReloadSettings extends Command {

   public cmdReloadSettings(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Reload worldsettings";
   }

   public void execute(String[] args, CommandSender sender) {
      if(sender instanceof Player) {
         Player player = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.reload.blocks.settings")) {
            ChatUtils.printError(player, this.pluginName, "You are not allowed to use this command.");
            return;
         }
      }

      ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, "Reloading Block-Settings...");
      ConfigHandler.clearAllSettings();
      ConfigHandler.loadWorldSettings();
      ChatUtils.printSuccess(sender, this.pluginName, "Block-Settings reloaded!");
   }
}
