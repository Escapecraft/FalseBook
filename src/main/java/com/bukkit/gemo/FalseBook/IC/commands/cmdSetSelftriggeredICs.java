package com.bukkit.gemo.FalseBook.IC.commands;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdSetSelftriggeredICs extends Command {

   public cmdSetSelftriggeredICs(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Set the ticks between the execution of selftriggered ICs";
   }

   public void execute(String[] args, CommandSender sender) {
      if(sender instanceof Player) {
         Player core = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(core, "falsebook.admin.ic")) {
            ChatUtils.printError(core, this.pluginName, "You are not allowed to use this command.");
            return;
         }
      }

      if(!Parser.isBoolean(args[0])) {
         ChatUtils.printError(sender, this.pluginName, "The argument must be true OR false.");
      } else {
         FalseBookICCore core1 = FalseBookICCore.getInstance();
         boolean enableSTICs = Parser.getBoolean(args[0], true);
         core1.setEnableSTICs(enableSTICs);
         core1.saveSettings("FalseBook" + System.getProperty("file.separator") + "FalseBookIC.properties");
         ChatUtils.printInfo(sender, "[FB-IC]", ChatColor.YELLOW, "SelftriggeredICs set to: " + enableSTICs);
         ChatUtils.printLine(sender, ChatColor.GRAY, "Type /reload to apply the changes.");
      }
   }
}
