package com.bukkit.gemo.FalseBook.Cart;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartBlockListener;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartPlayerListener;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartVehicleListener;
import com.bukkit.gemo.FalseBook.Cart.commands.cmdFBCart;
import com.bukkit.gemo.FalseBook.Cart.commands.cmdLockCart;
import com.bukkit.gemo.FalseBook.Cart.commands.cmdReloadSettings;
import com.bukkit.gemo.FalseBook.Cart.commands.cmdUnlockCart;
import com.bukkit.gemo.FalseBook.Core.FalseBookCore;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.commands.CommandList;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FalseBookCartCore extends JavaPlugin {

   private FalseBookCore core;
   private static String pluginName;
   private static Server server;
   private FalseBookCartVehicleListener cartListener;
   private FalseBookCartBlockListener blockListener;
   private FalseBookCartPlayerListener playerListener;
   private static FalseBookCartCore instance;
   private CommandList commandList;
   private static HashMap playerStationSelection;
   private static HashMap Settings;


   public static void printInConsole(String str) {
      System.out.println("[ FalseBook Cart ] " + str);
   }

   public void onDisable() {
      if(this.searchCore()) {
         CartHandler.saveLockedCarts();
         printInConsole(pluginName + " disabled");
      }

   }

   public void onEnable() {
      if(!this.searchCore()) {
         printInConsole(">>>>> FalseBookCore.jar not found! FalseBookCart.jar is being disabled!");
      } else {
         instance = this;
         this.loadVersion();
         server = this.getServer();
         this.loadWorldSettings();
         this.initCommands();
         this.cartListener = new FalseBookCartVehicleListener();
         this.blockListener = new FalseBookCartBlockListener();
         this.playerListener = new FalseBookCartPlayerListener();
         playerStationSelection = new HashMap();
         this.getServer().getPluginManager().registerEvents(this.blockListener, this);
         this.getServer().getPluginManager().registerEvents(this.cartListener, this);
         this.getServer().getPluginManager().registerEvents(this.playerListener, this);
         Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, this.blockListener, 1L, 1L);
         CartHandler.loadLockedCarts();
         printInConsole(pluginName + " enabled");
      }
   }

   private void initCommands() {
      Command[] commands = new Command[]{new cmdFBCart("[FB-Cart]", "/fbcart", "", "", false, new Command[]{new cmdLockCart("[FB-Cart]", "lock", "", ""), new cmdUnlockCart("[FB-Cart]", "unlock", "", ""), new cmdReloadSettings("[FB-Cart]", "reloadsettings", "", "")})};
      this.commandList = new CommandList("[FB-Chat]", commands);
   }

   public FalseBookCartVehicleListener getCartListener() {
      return this.cartListener;
   }

   public FalseBookCartBlockListener getBlockListener() {
      return this.blockListener;
   }

   public void loadVersion() {
      PluginDescriptionFile pdfFile = this.getDescription();
      pluginName = pdfFile.getName() + " v" + pdfFile.getVersion() + " by GeMo";
   }

   private boolean searchCore() {
      PluginManager pm = this.getServer().getPluginManager();
      if(pm.getPlugin("FalseBookCore") != null) {
         this.core = (FalseBookCore)pm.getPlugin("FalseBookCore");
         PluginDescriptionFile thisPDFFile = this.getDescription();
         PluginDescriptionFile corePDFFile = this.core.getDescription();
         if(!thisPDFFile.getVersion().equalsIgnoreCase(corePDFFile.getVersion())) {
            printInConsole("WRONG CORE-VERSION!");
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public FalseBookCore getCore() {
      return this.core;
   }

   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
      if(!this.searchCore()) {
         return true;
      } else {
         if(sender instanceof Player) {
            Player player = (Player)sender;
            if(label.equalsIgnoreCase("fbcart") && player.isOp()) {
               this.commandList.handleCommand(sender, label, args);
            } else if(label.equalsIgnoreCase("fbst") && UtilPermissions.playerCanUseCommand(player, "falsebook.cart.command.fbst") && args != null) {
               if(args.length == 1) {
                  setStation(player, args[0]);
                  ChatUtils.printSuccess(player, "[FB-Cart]", "Setting Current Station to: " + ChatColor.WHITE + getStation(player));
               } else {
                  ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GRAY, "Current Station: " + getStation(player));
               }
            }
         } else {
            this.commandList.handleCommand(sender, label, args);
         }

         return true;
      }
   }

   public void loadWorldSettings() {
      if(Settings != null) {
         Settings.clear();
      } else {
         Settings = new HashMap();
      }

      for(int i = 0; i < server.getWorlds().size(); ++i) {
         getOrCreateSettings(((World)server.getWorlds().get(i)).getName());
      }

   }

   public static CartWorldSettings getOrCreateSettings(String worldName) {
      CartWorldSettings result = (CartWorldSettings)Settings.get(worldName);
      if(result != null) {
         return result;
      } else {
         CartWorldSettings newSettings = new CartWorldSettings("FalseBookCart.properties", worldName);
         Settings.put(worldName, newSettings);
         return newSettings;
      }
   }

   public static void setStation(Player player, String station) {
      if(playerStationSelection.containsKey(player.getName())) {
         playerStationSelection.remove(player.getName());
      }

      playerStationSelection.put(player.getName(), station);
   }

   public static void clearStation(Player player) {
      if(playerStationSelection.containsKey(player.getName())) {
         playerStationSelection.remove(player.getName());
      }

   }

   public static String getStation(Player player) {
      return playerStationSelection.containsKey(player.getName())?(String)playerStationSelection.get(player.getName()):null;
   }

   public static Server getMCServer() {
      return server;
   }

   public static FalseBookCartCore getInstance() {
      return instance;
   }

   public static HashMap getSettings() {
      return Settings;
   }
}
