package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdClearICs extends Command {

   public cmdClearICs(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Clear ALL ICs";
   }

   public void execute(String[] args, CommandSender sender) {
      if(sender instanceof Player) {
         Player core = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(core, "falsebook.admin.ic")) {
            ChatUtils.printError(core, this.pluginName, "You are not allowed to use this command.");
            return;
         }
      }

      FalseBookICCore core1 = FalseBookICCore.getInstance();
      ICFactory factory = core1.getFactory();
      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Clearing ICs...");
      if(core1.getMainTaskID() != -1) {
         Bukkit.getServer().getScheduler().cancelTask(core1.getMainTaskID());
      }

      factory.clearSensorList();
      core1.getPersistenceHandler().clearAllSelftriggeredICs();
      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Selftriggered ICs cleared successfully!");
   }
}
