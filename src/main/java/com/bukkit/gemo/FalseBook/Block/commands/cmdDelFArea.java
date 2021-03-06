package com.bukkit.gemo.FalseBook.Block.commands;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicArea;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdDelFArea extends Command {

   public cmdDelFArea(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Delete area";
   }

   public void execute(String[] args, CommandSender sender) {
      if(sender instanceof Player) {
         Player mechanic = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(mechanic, "falsebook.blocks.area")) {
            ChatUtils.printError(mechanic, this.pluginName, "You are not allowed to use this command.");
            return;
         }
      }

      MechanicArea mechanic1 = (MechanicArea)FalseBookBlockCore.getInstance().getMechanicHandler().getMechanic("AREA");
      String aName = args[0];
      mechanic1.deleteArea(sender, aName);
   }
}
