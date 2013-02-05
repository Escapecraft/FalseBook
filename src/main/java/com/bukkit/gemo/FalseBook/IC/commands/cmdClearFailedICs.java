package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.FalseBook.IC.ICs.NotLoadedIC;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdClearFailedICs extends Command {

   public cmdClearFailedICs(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Clear all failed ICs";
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
      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Clearing " + ChatColor.RED + factory.getFailedICsSize() + ChatColor.YELLOW + "  failed ICs...");
      Iterator var6 = factory.getFailedICs().iterator();

      while(var6.hasNext()) {
         NotLoadedIC thisIC = (NotLoadedIC)var6.next();
         core1.getPersistenceHandler().removeSelftriggeredIC(thisIC.getICLocation());
         factory.removeFailedIC(thisIC);
      }

      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Failed ICs cleared successfully!");
   }
}
