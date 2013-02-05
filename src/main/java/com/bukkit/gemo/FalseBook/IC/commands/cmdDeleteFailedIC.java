package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.FalseBook.IC.ICs.NotLoadedIC;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdDeleteFailedIC extends Command {

   public cmdDeleteFailedIC(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Delete a failed IC";
   }

   public void execute(String[] args, CommandSender sender) {
      if(sender instanceof Player) {
         Player core = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(core, "falsebook.admin.ic")) {
            ChatUtils.printError(core, this.pluginName, "You are not allowed to use this command.");
            return;
         }
      }

      if(!Parser.isInteger(args[0])) {
         ChatUtils.printError(sender, this.pluginName, "The argument must be an integer.");
      } else {
         FalseBookICCore core1 = FalseBookICCore.getInstance();
         ICFactory factory = core1.getFactory();
         int failedID = Parser.getInteger(args[0], 0);
         boolean found = false;
         Iterator var8 = factory.getFailedICs().iterator();

         while(var8.hasNext()) {
            NotLoadedIC thisIC = (NotLoadedIC)var8.next();
            if(thisIC.getID() == failedID) {
               ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Deleting failed IC " + thisIC.getICNumber() + " with ID: " + thisIC.getID());
               core1.getPersistenceHandler().removeSelftriggeredIC(thisIC.getICLocation());
               factory.removeFailedIC(thisIC);
               found = true;
               break;
            }
         }

         if(!found) {
            ChatUtils.printError(sender, "[FB-IC]", "The given ID was not found!");
         } else {
            ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "Done!");
         }

      }
   }
}
