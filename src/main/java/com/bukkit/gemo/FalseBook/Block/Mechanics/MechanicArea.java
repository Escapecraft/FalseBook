package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Areas.Area;
import com.bukkit.gemo.FalseBook.Block.Areas.AreaBlockType;
import com.bukkit.gemo.FalseBook.Block.Areas.AreaSelection;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MechanicArea extends MechanicListener {

   private ArrayList Areas = new ArrayList();
   private ArrayList Selections = new ArrayList();


   public MechanicArea(FalseBookBlockCore plugin) {
      plugin.getMechanicHandler().registerEvent(BlockBreakEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPlaceEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonExtendEvent.class, this);
      plugin.getMechanicHandler().registerEvent(BlockPistonRetractEvent.class, this);
      plugin.getMechanicHandler().registerEvent(SignChangeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityChangeBlockEvent.class, this);
      plugin.getMechanicHandler().registerEvent(EntityExplodeEvent.class, this);
      plugin.getMechanicHandler().registerEvent(PlayerInteractEvent.class, this);
   }

   public void onLoad() {
      this.loadAreas();
   }

   public void reloadMechanic() {
      HashSet areaNames = new HashSet();
      Iterator var3 = this.Areas.iterator();

      while(var3.hasNext()) {
         Area areaName = (Area)var3.next();
         areaNames.add(areaName.getAreaName());
      }

      var3 = areaNames.iterator();

      while(var3.hasNext()) {
         String areaName1 = (String)var3.next();
         this.saveAreas(areaName1, false);
         this.saveAreas(areaName1, true);
      }

      this.Areas.clear();
      this.loadAreas();
   }

   public boolean isActivatedByRedstone(Block block, BlockRedstoneEvent event) {
      return ConfigHandler.isRedstoneAllowedForAreas(block.getWorld().getName());
   }

   public void onCommand(CommandSender sender, Command command, String label, String[] args) {
      if(sender instanceof Player) {
         Player player = (Player)sender;
         if(label.equalsIgnoreCase("fareaListAllow") && UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.area")) {
            if(args.length >= 1) {
               String areaName = "";

               for(int f = 0; f < args.length - 1; ++f) {
                  if(f > 0) {
                     areaName = areaName + " ";
                  }

                  areaName = areaName + args[f];
               }

               boolean var11 = false;

               for(int i = this.Areas.size() - 1; i >= 0; --i) {
                  if(((Area)this.Areas.get(i)).getAreaName().equalsIgnoreCase(areaName)) {
                     String txt = "";
                     if(((Area)this.Areas.get(i)).getAllowedBlocks().size() == 0) {
                        ChatUtils.printLine(player, ChatColor.GOLD, "All blocktypes are allowed in \'" + areaName + "\'");
                     } else {
                        for(int j = 0; j < ((Area)this.Areas.get(i)).getAllowedBlocks().size(); ++j) {
                           txt = txt + ((AreaBlockType)((Area)this.Areas.get(i)).getAllowedBlocks().get(j)).getTypeID() + ":" + ((AreaBlockType)((Area)this.Areas.get(i)).getAllowedBlocks().get(j)).getData();
                           if(j < ((Area)this.Areas.get(i)).getAllowedBlocks().size() - 1) {
                              txt = txt + ", ";
                           }
                        }

                        ChatUtils.printLine(player, ChatColor.GOLD, "Allowed blocktypes in \'" + areaName + "\':");
                        ChatUtils.printLine(player, ChatColor.GRAY, txt);
                     }

                     var11 = true;
                  }
               }

               if(!var11) {
                  ChatUtils.printError(player, "[FB-Block]", "Area \'" + areaName + "\' not found!");
               }
            } else {
               ChatUtils.printError(player, "[FB-Block]", "Wrong syntax! Use \'/fareaListAllow <areaname>\'");
            }
         }

      }
   }

   public void onPlayerInteract(PlayerInteractEvent event, boolean isWallSign, boolean isSignPost) {
      if(!event.isCancelled()) {
         if(event.hasBlock()) {
            int i;
            boolean var10;
            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.blocks.area")) {
               Player isOp = event.getPlayer();
               Block blockID = event.getClickedBlock();
               if(isOp.getItemInHand().getTypeId() == ConfigHandler.getAreaSelectionTool(blockID.getWorld().getName()) && UtilPermissions.playerCanUseCommand(isOp, "falsebook.blocks.area")) {
                  event.setUseInteractedBlock(Result.DENY);
                  event.setUseItemInHand(Result.DENY);
                  event.setCancelled(true);
                  var10 = false;

                  for(i = 0; i < this.Selections.size(); ++i) {
                     if(((AreaSelection)this.Selections.get(i)).player.getName().equalsIgnoreCase(isOp.getName())) {
                        if(((AreaSelection)this.Selections.get(i)).selFirst) {
                           ((AreaSelection)this.Selections.get(i)).selP1 = blockID.getLocation();
                           ((AreaSelection)this.Selections.get(i)).selP2 = null;
                           ((AreaSelection)this.Selections.get(i)).selFirst = false;
                           ChatUtils.printLine(isOp, ChatColor.LIGHT_PURPLE, "[FB-Area] Position 1 selected");
                        } else {
                           ((AreaSelection)this.Selections.get(i)).selP2 = blockID.getLocation();
                           ((AreaSelection)this.Selections.get(i)).selFirst = true;
                           ChatUtils.printLine(isOp, ChatColor.LIGHT_PURPLE, "[FB-Area] Position 2 selected");
                        }

                        var10 = true;
                        break;
                     }
                  }

                  if(!var10) {
                     AreaSelection var11 = new AreaSelection();
                     var11.player = isOp;
                     var11.selP1 = blockID.getLocation();
                     var11.selP2 = null;
                     var11.selFirst = false;
                     this.Selections.add(var11);
                     ChatUtils.printLine(isOp, ChatColor.LIGHT_PURPLE, "[FB-Area] Position 1 selected");
                  }
               } else if(blockID.getType().equals(Material.SIGN_POST) || blockID.getType().equals(Material.WALL_SIGN)) {
                  Sign isInteractable = (Sign)blockID.getState();
                  if(isInteractable.getLine(1).length() > 0 && (isInteractable.getLine(1).equalsIgnoreCase("[Toggle]") || isInteractable.getLine(1).equalsIgnoreCase("[Area]"))) {
                     event.setUseInteractedBlock(Result.DENY);
                     event.setUseItemInHand(Result.DENY);
                     event.setCancelled(true);
                     this.check(FalseBookBlockCore.getInstance(), isInteractable, isOp, blockID);
                  }
               }
            }

            boolean var8 = UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.interact.blocks");
            int var9 = event.getClickedBlock().getTypeId();
            var10 = var9 == Material.WOODEN_DOOR.getId() || var9 == Material.DEAD_BUSH.getId() || var9 == Material.CAKE_BLOCK.getId() || var9 == Material.LEVER.getId() || var9 == Material.STONE_BUTTON.getId() || var9 == Material.CHEST.getId() || var9 == Material.WOOD_DOOR.getId() || var9 == Material.IRON_DOOR.getId() || var9 == Material.LONG_GRASS.getId() || var9 == Material.DISPENSER.getId() || var9 == Material.FURNACE.getId() || var9 == Material.BURNING_FURNACE.getId() || var9 == Material.JUKEBOX.getId() || var9 == Material.NOTE_BLOCK.getId() || var9 == Material.SEEDS.getId() || var9 == Material.SUGAR_CANE_BLOCK.getId();
            if(var10 && !var8) {
               for(i = 0; i < this.Areas.size(); ++i) {
                  if(((Area)this.Areas.get(i)).isInteractBlocked() && ((Area)this.Areas.get(i)).isBlockInArea(event.getClickedBlock())) {
                     event.setCancelled(true);
                     event.setUseInteractedBlock(Result.DENY);
                     event.setUseItemInHand(Result.DENY);
                     ChatUtils.printError(event.getPlayer(), "[FB-Block]", "This area is interact-protected!");
                     return;
                  }
               }
            }

         }
      }
   }

   public void onSignChange(SignChangeEvent event) {
      if(!event.isCancelled()) {
         if(event.getLine(1).equalsIgnoreCase("[Toggle]")) {
            event.setLine(1, "[Toggle]");
         } else {
            event.setLine(1, "[Area]");
         }

         if(!UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.blocks.area")) {
            SignUtils.cancelSignCreation(event, "You are not allowed to build areasigns.");
         } else {
            ChatUtils.printSuccess(event.getPlayer(), "[FB-Block]", "Areasign created.");
         }
      }
   }

   public void onBlockPlace(BlockPlaceEvent event) {
      if(!event.isCancelled()) {
         Player player = event.getPlayer();
         Block block = event.getBlockPlaced();
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.destroy.blocks")) {
            for(int i = 0; i < this.Areas.size(); ++i) {
               if(((Area)this.Areas.get(i)).isProtect() && ((Area)this.Areas.get(i)).isBlockInArea(block)) {
                  ChatUtils.printError(player, "[FB-Block]", "This area is protected!");
                  event.setBuild(false);
                  event.setCancelled(true);
                  return;
               }
            }
         }

      }
   }

   public void onBlockBreak(BlockBreakEvent event) {
      if(!event.isCancelled()) {
         Player player = event.getPlayer();
         Block block = event.getBlock();
         if(!UtilPermissions.playerCanUseCommand(player, "falsebook.destroy.blocks")) {
            for(int sign = 0; sign < this.Areas.size(); ++sign) {
               if(((Area)this.Areas.get(sign)).isProtect() && ((Area)this.Areas.get(sign)).isBlockInArea(block)) {
                  ChatUtils.printError(event.getPlayer(), "[FB-Block]", "This area is protected!");
                  event.setCancelled(true);
                  return;
               }
            }
         }

         if(event.getBlock().getTypeId() == Material.WALL_SIGN.getId()) {
            Sign var5 = (Sign)event.getBlock().getState();
            if((var5.getLine(1).equalsIgnoreCase("[Area]") || var5.getLine(1).equalsIgnoreCase("[Toggle]")) && !UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.area")) {
               ChatUtils.printError(player, "[FB-Block]", "You are not allowed to destroy areasigns.");
               event.setCancelled(true);
               return;
            }
         }

      }
   }

   public void onBlockPistonExtend(BlockPistonExtendEvent event) {
      if(!event.isCancelled()) {
         if(this.isBlockProtected(event.getBlocks())) {
            event.setCancelled(true);
         }

      }
   }

   public void onBlockPistonRetract(BlockPistonRetractEvent event) {
      if(!event.isCancelled()) {
         if(event.isSticky()) {
            if(this.isBlockProtected(event.getRetractLocation().getBlock())) {
               event.setCancelled(true);
            }

         }
      }
   }

   public void onEntityExplode(EntityExplodeEvent event) {
      if(!event.isCancelled()) {
         if(this.isBlockProtected(event.blockList())) {
            event.setYield(0.0F);
            event.setCancelled(true);
         }

      }
   }

   public void onEntityChangeBlock(EntityChangeBlockEvent event) {
      if(!event.isCancelled()) {
         if(this.isBlockProtected(event.getBlock())) {
            event.setCancelled(true);
         }

      }
   }

   private boolean isBlockProtected(List blockList) {
      try {
         for(int e = 0; e < this.Areas.size(); ++e) {
            if(((Area)this.Areas.get(e)).isProtect()) {
               for(int j = 0; j < blockList.size(); ++j) {
                  if(((Area)this.Areas.get(e)).isBlockInArea((Block)blockList.get(j))) {
                     return true;
                  }
               }
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return false;
   }

   private boolean isBlockProtected(Block block) {
      try {
         for(int e = 0; e < this.Areas.size(); ++e) {
            if(((Area)this.Areas.get(e)).isProtect() && ((Area)this.Areas.get(e)).isBlockInArea(block)) {
               return true;
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return false;
   }

   private void check(FalseBookBlockCore plugin, Sign sign, Player player, Block block) {
      int[] res = this.toggle(plugin, sign, block, true, true);
      if(res[0] >= 0 && (res[1] == -2 || res[1] >= 0)) {
         ChatUtils.printLine(player, ChatColor.GOLD, "Area toggled.");
      } else {
         if(res[0] == -1) {
            ChatUtils.printError(player, "[FB-Block]", "Area \'" + sign.getLine(0) + "\' not found.");
         }

         if(res[1] == -1) {
            ChatUtils.printError(player, "[FB-Block]", "Area \'" + sign.getLine(2) + "\' not found.");
         }
      }

   }

   public int[] toggle(FalseBookBlockCore plugin, Sign sign, Block block, boolean State, boolean playerActivated) {
      int[] f = new int[]{-1, -2};
      if(sign.getLine(2).length() > 0) {
         f[1] = -1;
      }

      ArrayList one = new ArrayList();
      ArrayList two = new ArrayList();
      if(sign.getLine(1).equalsIgnoreCase("[Toggle]") || sign.getLine(1).equalsIgnoreCase("[Area]")) {
         int j;
         for(j = 0; j < this.Areas.size(); ++j) {
            if(((Area)this.Areas.get(j)).getAreaName().equalsIgnoreCase(sign.getLine(0))) {
               one.add((Area)this.Areas.get(j));
               f[0] = j;
            }

            if(((Area)this.Areas.get(j)).getAreaName().equalsIgnoreCase(sign.getLine(2))) {
               two.add((Area)this.Areas.get(j));
               f[1] = j;
            }
         }

         if(playerActivated) {
            for(j = 0; j < one.size(); ++j) {
               int j1;
               if(!((Area)one.get(j)).isShow()) {
                  for(j1 = 0; j1 < two.size(); ++j1) {
                     ((Area)two.get(j1)).toggle(false, plugin);
                  }

                  ((Area)one.get(j)).toggle(true, plugin);
               } else {
                  ((Area)one.get(j)).toggle(false, plugin);

                  for(j1 = 0; j1 < two.size(); ++j1) {
                     ((Area)two.get(j1)).toggle(true, plugin);
                  }
               }
            }
         } else if(State) {
            for(j = 0; j < two.size(); ++j) {
               ((Area)two.get(j)).toggle(false, plugin);
            }

            for(j = 0; j < one.size(); ++j) {
               ((Area)one.get(j)).toggle(true, plugin);
            }
         } else {
            for(j = 0; j < one.size(); ++j) {
               ((Area)one.get(j)).toggle(false, plugin);
            }

            for(j = 0; j < two.size(); ++j) {
               ((Area)two.get(j)).toggle(true, plugin);
            }
         }
      }

      if(one.size() > 0) {
         this.saveAreas(((Area)one.get(0)).getAreaName(), true);
      }

      if(two.size() > 0) {
         this.saveAreas(((Area)two.get(0)).getAreaName(), true);
      }

      return f;
   }

   private boolean loadArea(File FileName) {
      String areaPath = "plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "areas" + System.getProperty("file.separator");

      try {
         ObjectInputStream e = new ObjectInputStream(new BufferedInputStream(new FileInputStream(areaPath + FileName.getName())));
         int count = ((Integer)e.readObject()).intValue();

         for(int i = 0; i < count; ++i) {
            try {
               Area e1 = (Area)e.readObject();
               e1.initArea();
               this.Areas.add(e1);
            } catch (Exception var7) {
               FalseBookBlockCore.printInConsole("An error occured while loading an Area...");
            }
         }

         e.close();
         return true;
      } catch (Exception var8) {
         var8.printStackTrace();
         FalseBookBlockCore.printInConsole("Error while reading " + areaPath + FileName.getName());
         return false;
      }
   }

   private boolean loadAreas() {
      String areaPath = "plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "areas" + System.getProperty("file.separator");
      File f = new File(areaPath);
      f.mkdirs();
      if(f.listFiles() == null) {
         return true;
      } else {
         File[] var6;
         int var5 = (var6 = f.listFiles()).length;

         for(int var4 = 0; var4 < var5; ++var4) {
            File file = var6[var4];
            if(file.isFile()) {
               this.loadArea(file);
            }
         }

         FalseBookBlockCore.printInConsole(this.Areas.size() + " Areas loaded.");
         return true;
      }
   }

   public void saveAreas(String FileName, boolean delete) {
      String areaPath = "plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "areas" + System.getProperty("file.separator");
      File folder = new File(areaPath);
      folder.mkdirs();
      if(!delete) {
         File e = new File(areaPath + FileName + ".db");
         e.delete();
      } else {
         try {
            ObjectOutputStream var10 = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(areaPath + FileName + ".db")));
            int i = 0;
            Iterator var8 = this.Areas.iterator();

            Area area;
            while(var8.hasNext()) {
               area = (Area)var8.next();
               if(area.getAreaName().equals(FileName)) {
                  ++i;
               }
            }

            var10.writeObject(Integer.valueOf(i));
            var8 = this.Areas.iterator();

            while(var8.hasNext()) {
               area = (Area)var8.next();
               if(area.getAreaName().equals(FileName)) {
                  var10.writeObject(area);
               }
            }

            var10.close();
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }

   }

   public void deleteArea(CommandSender sender, String name) {
      boolean f = false;

      for(int i = this.Areas.size() - 1; i >= 0; --i) {
         if(((Area)this.Areas.get(i)).getAreaName().equalsIgnoreCase(name)) {
            this.Areas.remove(i);
            this.saveAreas(name, false);
            ChatUtils.printSuccess(sender, "[FB-Block]", "Area \'" + name + "\' deleted!");
            f = true;
         }
      }

      if(!f) {
         ChatUtils.printError(sender, "[FB-Block]", "Area \'" + name + "\' not found!");
      }

   }

   public void listAreas(CommandSender sender) {
      String str = ChatColor.GOLD + "List of Areas: " + ChatColor.WHITE;

      for(int i = 0; i < this.Areas.size() - 1; ++i) {
         str = str + ((Area)this.Areas.get(i)).getAreaName() + ", ";
      }

      if(this.Areas.size() > 0) {
         str = str + ((Area)this.Areas.get(this.Areas.size() - 1)).getAreaName();
      }

      ChatUtils.printLine(sender, ChatColor.GRAY, str);
   }

   public ArrayList getAreas() {
      return this.Areas;
   }

   public ArrayList getSelections() {
      return this.Selections;
   }
}
