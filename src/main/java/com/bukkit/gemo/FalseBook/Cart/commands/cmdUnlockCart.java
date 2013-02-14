package com.bukkit.gemo.FalseBook.Cart.commands;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdUnlockCart extends Command {

   public cmdUnlockCart(String pluginName, String syntax, String arguments, String node) {
      super(pluginName, syntax, arguments, node);
      this.description = "Lock a storage-cart";
   }

   public void execute(String[] args, CommandSender sender) {
      if(!(sender instanceof Player)) {
         ChatUtils.printError(sender, this.pluginName, "This is only an ingame command.");
      } else {
         Player player = (Player)sender;
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.lockStorageCart")) {
            ChatUtils.printError(player, this.pluginName, "You are not allowed to use this command.");
         } else {
            CartHandler.addToUnLockMode(player);
            ChatUtils.printInfo(player, "[FB-IC]", ChatColor.GRAY, "Rightclick on a storagecart to unlock a cart.");
         }
      }
   }
}
