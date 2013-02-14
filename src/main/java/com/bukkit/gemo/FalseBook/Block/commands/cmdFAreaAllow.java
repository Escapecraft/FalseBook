package com.bukkit.gemo.FalseBook.Block.commands;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Areas.Area;
import com.bukkit.gemo.FalseBook.Block.Areas.AreaBlockType;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicArea;
import com.bukkit.gemo.commands.ExtendedCommand;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdFAreaAllow extends ExtendedCommand {

   public cmdFAreaAllow(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Allow specific blocktypes in areas";
   }

   public void execute(String[] args, CommandSender sender) {
      if(sender instanceof Player) {
         Player mechanic = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(mechanic, "falsebook.blocks.area")) {
            ChatUtils.printError(mechanic, this.pluginName, "You are not allowed to use this command.");
            return;
         }
      }

      MechanicArea var13 = (MechanicArea)FalseBookBlockCore.getInstance().getMechanicHandler().getMechanic("AREA");
      if(args.length > 0) {
         String areaName = "";

         for(int split = 0; split < args.length - 1; ++split) {
            if(split > 0) {
               areaName = areaName + " ";
            }

            areaName = areaName + args[split];
         }

         String[] var14 = args[args.length - 1].split(":");
         boolean typeID = false;
         byte typeData = 0;
         int var15;
         if(var14.length == 1) {
            try {
               var15 = Integer.valueOf(var14[0]).intValue();
            } catch (Exception var12) {
               ChatUtils.printError(sender, this.pluginName, "Wrong syntax! Use \'/fareaallow <areaname> <ID>[:<SUB>]\'");
               return;
            }
         } else {
            try {
               var15 = Integer.valueOf(var14[0]).intValue();
               typeData = Byte.valueOf(var14[1]).byteValue();
            } catch (Exception var11) {
               ChatUtils.printError(sender, this.pluginName, "Wrong syntax! Use \'/fareaallow <areaname> <ID>[:<SUB>]\'");
               return;
            }
         }

         AreaBlockType thisBlock = new AreaBlockType(var15, typeData);
         boolean f = false;

         for(int i = var13.getAreas().size() - 1; i >= 0; --i) {
            if(((Area)var13.getAreas().get(i)).getAreaName().equalsIgnoreCase(areaName)) {
               if(!((Area)var13.getAreas().get(i)).isInAllowed(thisBlock)) {
                  ((Area)var13.getAreas().get(i)).getAllowedBlocks().add(thisBlock);
                  ChatUtils.printSuccess(sender, this.pluginName, "Added " + var15 + ":" + typeData + " to toggleable Blocks!");
               } else {
                  ((Area)var13.getAreas().get(i)).getAllowedBlocks().remove(((Area)var13.getAreas().get(i)).getInAllowed(thisBlock.getTypeID(), thisBlock.getData()));
                  ChatUtils.printSuccess(sender, this.pluginName, "Removed " + var15 + ":" + typeData + " from toggleable Blocks!");
               }

               var13.saveAreas(((Area)var13.getAreas().get(i)).getAreaName(), true);
               f = true;
            }
         }

         if(!f) {
            ChatUtils.printError(sender, this.pluginName, "Area \'" + areaName + "\' not found!");
         }
      }

   }
}
