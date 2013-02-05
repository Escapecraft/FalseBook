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

public class cmdJumpToFailed extends Command {

   public cmdJumpToFailed(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Jump to a failed IC";
   }

   public void execute(String[] args, CommandSender sender) {
      if(!(sender instanceof Player)) {
         ChatUtils.printError(sender, this.pluginName, "This is only an ingame command.");
      } else {
         Player player = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.admin.ic")) {
            ChatUtils.printError(player, this.pluginName, "You are not allowed to use this command.");
         } else if(!Parser.isInteger(args[0])) {
            ChatUtils.printError(player, this.pluginName, "The argument must be an integer.");
         } else {
            FalseBookICCore core = FalseBookICCore.getInstance();
            ICFactory factory = core.getFactory();
            int failedID = Parser.getInteger(args[0], 0);
            Iterator var8 = factory.getFailedICs().iterator();

            while(var8.hasNext()) {
               NotLoadedIC thisIC = (NotLoadedIC)var8.next();
               if(thisIC.getID() == failedID) {
                  ChatUtils.printInfo(player, "[FB-IC]", ChatColor.YELLOW, "Teleporting to IC with ID: " + thisIC.getID());
                  player.teleport(thisIC.getICLocation());
                  return;
               }
            }

            ChatUtils.printError(player, "[FB-IC]", "The given ID was not found!");
         }
      }
   }
}
