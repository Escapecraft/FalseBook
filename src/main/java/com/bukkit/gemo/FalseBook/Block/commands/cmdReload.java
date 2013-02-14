package com.bukkit.gemo.FalseBook.Block.commands;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdReload extends Command {

   public cmdReload(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Reload FalseBook-Block";
   }

   public void execute(String[] args, CommandSender sender) {
      if(sender instanceof Player) {
         Player player = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.reload.blocks.all")) {
            ChatUtils.printError(player, this.pluginName, "You are not allowed to use this command.");
            return;
         }
      }

      ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, "Reloading...");
      ConfigHandler.clearAllSettings();
      ConfigHandler.loadWorldSettings();
      FalseBookBlockCore.getInstance().getMechanicHandler().getMechanic("AREA").reloadMechanic();
      FalseBookBlockCore.getInstance().getMechanicHandler().getMechanic("BRIDGE").reloadMechanic();
      FalseBookBlockCore.getInstance().getMechanicHandler().getMechanic("DOOR").reloadMechanic();
      FalseBookBlockCore.getInstance().getMechanicHandler().getMechanic("CAULDRON").reloadMechanic();
      FalseBookBlockCore.getInstance().getMechanicHandler().getMechanic("BOOKSHELF").reloadMechanic();
      ChatUtils.printSuccess(sender, this.pluginName, "FalseBook-Block reloaded!");
   }
}
