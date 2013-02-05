package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.FalseBook.IC.ICs.NotLoadedIC;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.text.DecimalFormat;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdICStatus extends Command {

   public cmdICStatus(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Show status of ICs";
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
      ChatUtils.printLine(sender, ChatColor.AQUA, "[FalseBook IC Status]:");
      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Selftriggered ICs loaded: " + ChatColor.GREEN + factory.getSensorListSize());
      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Selftriggered ICs NOT loaded: " + ChatColor.RED + factory.getFailedICsSize());
      if(factory.getFailedICsSize() > 0) {
         ChatUtils.printLine(sender, ChatColor.AQUA, "List of failed ICs: ");
         Iterator var6 = factory.getFailedICs().iterator();

         while(var6.hasNext()) {
            NotLoadedIC formater = (NotLoadedIC)var6.next();
            ChatUtils.printError(sender, "[FB-IC]", "ID: " + formater.getID() + ", " + formater.getICNumber() + " @ World: " + formater.getICLocation().getWorld().getName() + " , X: " + formater.getICLocation().getBlockX() + " , Y: " + formater.getICLocation().getBlockY() + " , Z: " + formater.getICLocation().getBlockZ());
         }
      }

      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Selftriggered ICs are enabled: " + core1.isEnableSTICs());
      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Load unloaded chunks: " + core1.isLoadUnloadedChunks());
      ChatUtils.printInfo(sender, "", ChatColor.WHITE, " ");
      ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Execution of selftriggered ICs took:");
      DecimalFormat formater1 = new DecimalFormat("#.###");
      ChatUtils.printInfo(sender, core1.getPluginName(), ChatColor.AQUA, "executioncount - min. time in ms / max. time in ms / average time ms");
      ChatUtils.printInfo(sender, core1.getPluginName(), ChatColor.GRAY, "-----------------------------------------------");
      ChatUtils.printInfo(sender, core1.getPluginName(), ChatColor.GRAY, factory.statistic.eventCount + " - " + formater1.format((double)factory.statistic.minTime / 1000000.0D) + "ms / " + formater1.format((double)factory.statistic.maxTime / 1000000.0D) + "ms / " + formater1.format((double)(factory.statistic.allTime / factory.statistic.eventCount) / 1000000.0D) + "ms");
   }
}
