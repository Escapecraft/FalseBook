package com.bukkit.gemo.FalseBook.Block.World;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.QueuedExecutionEvent;
import com.bukkit.gemo.FalseBook.Block.World.EnumSettings;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import com.bukkit.gemo.FalseBook.Values.ValueIntegerList;
import com.bukkit.gemo.FalseBook.World.FBWorld;
import com.bukkit.gemo.utils.FlatFile;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class FBWorldBlock extends FBWorld implements Runnable {

   private List queuedEvents = Collections.synchronizedList(new ArrayList());
   private Map queuedEventsPos = Collections.synchronizedMap(new HashMap());
   private int mainTaskID = -1;
   private static String FileName = "FalseBookBlock.properties";


   public FBWorldBlock(String worldName) {
      super(worldName);
   }

   public void run() {
      QueuedExecutionEvent event = null;

      for(int i = 0; i < this.queuedEvents.size(); ++i) {
         event = (QueuedExecutionEvent)this.queuedEvents.get(i);
         event.Execute();
      }

      this.queuedEventsPos.clear();
      this.queuedEvents.clear();
      this.mainTaskID = -1;
   }

   public boolean loadSettings() {
      File baseFolder = new File("plugins" + System.getProperty("file.separator") + "FalseBook");
      baseFolder.mkdirs();
      File worldFolder = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + this.getWorldName());
      worldFolder.mkdirs();
      File oldFile = new File(baseFolder, FileName);
      if(oldFile.exists()) {
         oldFile.delete();
      }

      try {
         this.initSettings();
         FlatFile e = new FlatFile("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + this.getWorldName() + System.getProperty("file.separator") + FileName, false);
         if(e.readFile()) {
            this.getSettings().addBoolean(EnumSettings.BRIDGE_ENABLED.getName(), e.getBoolean(EnumSettings.BRIDGE_ENABLED.getName(), true));
            this.getSettings().addBoolean(EnumSettings.BRIDGE_ALLOW_REDSTONE.getName(), e.getBoolean(EnumSettings.BRIDGE_ALLOW_REDSTONE.getName(), true));
            this.getSettings().addInteger(EnumSettings.BRIDGE_MAX_LENGTH.getName(), e.getInt(EnumSettings.BRIDGE_MAX_LENGTH.getName(), 20));
            this.getSettings().addInteger(EnumSettings.BRIDGE_MAX_SIDEWIDTH.getName(), e.getInt(EnumSettings.BRIDGE_MAX_SIDEWIDTH.getName(), 5));
            this.getSettings().addIntegerList(EnumSettings.BRIDGE_ALLOWED_BLOCKS.getName(), e.getIntArrayList(EnumSettings.BRIDGE_ALLOWED_BLOCKS.getName(), ValueIntegerList.delimiter));
            this.getSettings().addBoolean(EnumSettings.DOOR_ENABLED.getName(), e.getBoolean(EnumSettings.DOOR_ENABLED.getName(), true));
            this.getSettings().addBoolean(EnumSettings.DOOR_ALLOW_REDSTONE.getName(), e.getBoolean(EnumSettings.DOOR_ALLOW_REDSTONE.getName(), true));
            this.getSettings().addInteger(EnumSettings.DOOR_MAX_HEIGHT.getName(), e.getInt(EnumSettings.DOOR_MAX_HEIGHT.getName(), 20));
            this.getSettings().addInteger(EnumSettings.DOOR_MAX_SIDEWIDTH.getName(), e.getInt(EnumSettings.DOOR_MAX_SIDEWIDTH.getName(), 5));
            this.getSettings().addIntegerList(EnumSettings.DOOR_ALLOWED_BLOCKS.getName(), e.getIntArrayList(EnumSettings.DOOR_ALLOWED_BLOCKS.getName(), ValueIntegerList.delimiter));
            this.getSettings().addBoolean(EnumSettings.GATE_ENABLED.getName(), e.getBoolean(EnumSettings.GATE_ENABLED.getName(), true));
            this.getSettings().addBoolean(EnumSettings.GATE_ALLOW_REDSTONE.getName(), e.getBoolean(EnumSettings.GATE_ALLOW_REDSTONE.getName(), true));
            this.getSettings().addInteger(EnumSettings.GATE_MAX_HEIGHT.getName(), e.getInt(EnumSettings.GATE_MAX_HEIGHT.getName(), 20));
            this.getSettings().addInteger(EnumSettings.GATE_MAX_WIDTH.getName(), e.getInt(EnumSettings.GATE_MAX_WIDTH.getName(), 20));
            this.getSettings().addIntegerList(EnumSettings.GATE_ALLOWED_BLOCKS.getName(), e.getIntArrayList(EnumSettings.GATE_ALLOWED_BLOCKS.getName(), ValueIntegerList.delimiter));
            this.getSettings().addBoolean(EnumSettings.AREA_ALLOW_REDSTONE.getName(), e.getBoolean(EnumSettings.AREA_ALLOW_REDSTONE.getName(), true));
            this.getSettings().addInteger(EnumSettings.AREA_TOOL.getName(), e.getInt(EnumSettings.AREA_TOOL.getName(), Material.WOOD_HOE.getId()));
            this.getSettings().addBoolean(EnumSettings.CAULDRON_NATIVE.getName(), e.getBoolean(EnumSettings.CAULDRON_NATIVE.getName(), true));
            this.getSettings().addInteger(EnumSettings.CAULDRON_COOLDOWN.getName(), e.getInt(EnumSettings.CAULDRON_COOLDOWN.getName(), 1));
            this.getSettings().addBoolean(EnumSettings.LIGHTSWITCH_ENABLED.getName(), e.getBoolean(EnumSettings.LIGHTSWITCH_ENABLED.getName(), true));
            this.getSettings().addInteger(EnumSettings.LIGHTSWITCH_MAX_TOGGLE.getName(), e.getInt(EnumSettings.LIGHTSWITCH_MAX_TOGGLE.getName(), 20));
            this.getSettings().addBoolean(EnumSettings.LWC_RESPECT.getName(), e.getBoolean(EnumSettings.LWC_RESPECT.getName(), false));
            this.getSettings().addBoolean(EnumSettings.BOOKSHELFS_ENABLED.getName(), e.getBoolean(EnumSettings.BOOKSHELFS_ENABLED.getName(), true));
            this.getSettings().addDouble(EnumSettings.APPLE_DROP_CHANCE.getName(), (double)e.getFloat(EnumSettings.APPLE_DROP_CHANCE.getName(), 10.0F));
            this.saveSettings(new File("plugins/FalseBook" + System.getProperty("file.separator") + this.getWorldName()), FileName);
         } else {
            this.saveSettings(new File("plugins/FalseBook" + System.getProperty("file.separator") + this.getWorldName()), FileName);
         }

         return true;
      } catch (Exception var7) {
         FalseBookBlockCore.printInConsole("Error while reading file: plugins/FalseBook/" + this.getWorldName() + "/" + FileName);

         try {
            FlatFile e1 = new FlatFile("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + this.getWorldName() + System.getProperty("file.separator") + FileName, false);
            e1.regenerateFile("FalseBook" + System.getProperty("file.separator") + this.getWorldName() + System.getProperty("file.separator") + FileName);
            this.saveSettings(new File("FalseBook" + System.getProperty("file.separator") + this.getWorldName()), this.getWorldName());
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         FalseBookBlockCore.printInConsole("regenerated file: plugins/FalseBook/" + this.getWorldName() + "/" + FileName);
         return true;
      }
   }

   private void initSettings() {
      this.getSettings().addBoolean(EnumSettings.BRIDGE_ENABLED.getName(), true);
      this.getSettings().addBoolean(EnumSettings.BRIDGE_ALLOW_REDSTONE.getName(), true);
      this.getSettings().addInteger(EnumSettings.BRIDGE_MAX_LENGTH.getName(), 20);
      this.getSettings().addInteger(EnumSettings.BRIDGE_MAX_SIDEWIDTH.getName(), 5);
      this.getSettings().addIntegerList(EnumSettings.BRIDGE_ALLOWED_BLOCKS.getName(), EnumSettings.BRIDGE_ALLOWED_BLOCKS.getName() + "=1,2,3,4,5,17,20,24,35,43,44,48,49,98");
      this.getSettings().addBoolean(EnumSettings.DOOR_ENABLED.getName(), true);
      this.getSettings().addBoolean(EnumSettings.DOOR_ALLOW_REDSTONE.getName(), true);
      this.getSettings().addInteger(EnumSettings.DOOR_MAX_HEIGHT.getName(), 20);
      this.getSettings().addInteger(EnumSettings.DOOR_MAX_SIDEWIDTH.getName(), 5);
      this.getSettings().addIntegerList(EnumSettings.DOOR_ALLOWED_BLOCKS.getName(), EnumSettings.DOOR_ALLOWED_BLOCKS.getName() + "=1,2,3,4,5,17,20,24,35,43,44,48,49,98");
      this.getSettings().addBoolean(EnumSettings.GATE_ENABLED.getName(), true);
      this.getSettings().addBoolean(EnumSettings.GATE_ALLOW_REDSTONE.getName(), true);
      this.getSettings().addInteger(EnumSettings.GATE_MAX_HEIGHT.getName(), 20);
      this.getSettings().addInteger(EnumSettings.GATE_MAX_WIDTH.getName(), 20);
      this.getSettings().addIntegerList(EnumSettings.GATE_ALLOWED_BLOCKS.getName(), EnumSettings.GATE_ALLOWED_BLOCKS.getName() + "=85,101,102");
      this.getSettings().addBoolean(EnumSettings.AREA_ALLOW_REDSTONE.getName(), true);
      this.getSettings().addInteger(EnumSettings.AREA_TOOL.getName(), Material.WOOD_HOE.getId());
      this.getSettings().addBoolean(EnumSettings.CAULDRON_NATIVE.getName(), true);
      this.getSettings().addInteger(EnumSettings.CAULDRON_COOLDOWN.getName(), 1);
      this.getSettings().addBoolean(EnumSettings.LIGHTSWITCH_ENABLED.getName(), true);
      this.getSettings().addInteger(EnumSettings.LIGHTSWITCH_MAX_TOGGLE.getName(), 20);
      this.getSettings().addBoolean(EnumSettings.LWC_RESPECT.getName(), false);
      this.getSettings().addBoolean(EnumSettings.BOOKSHELFS_ENABLED.getName(), true);
      this.getSettings().addFloat(EnumSettings.APPLE_DROP_CHANCE.getName(), 10.0F);
   }

   public void onBlockBreak(BlockBreakEvent event) {
      FalseBookBlockCore.getInstance().getMechanicHandler().onBlockBreak(event);
   }

   public void onBlockPlace(BlockPlaceEvent event) {
      FalseBookBlockCore.getInstance().getMechanicHandler().onBlockPlace(event);
   }

   public void onPistonExtend(BlockPistonExtendEvent event) {
      FalseBookBlockCore.getInstance().getMechanicHandler().onBlockPistonExtend(event);
   }

   public void onPistonRetract(BlockPistonRetractEvent event) {
      FalseBookBlockCore.getInstance().getMechanicHandler().onBlockPistonRetract(event);
   }

   public void onRedstoneChange(BlockRedstoneEvent event) {
      Block block = event.getBlock();
      this.doRedstoneEvent(block.getRelative(1, 0, 0), event);
      this.doRedstoneEvent(block.getRelative(-1, 0, 0), event);
      this.doRedstoneEvent(block.getRelative(0, 0, 1), event);
      this.doRedstoneEvent(block.getRelative(0, 0, -1), event);
      this.doRedstoneEvent(block.getRelative(1, 1, 0), event);
      this.doRedstoneEvent(block.getRelative(-1, 1, 0), event);
      this.doRedstoneEvent(block.getRelative(1, -1, 0), event);
      this.doRedstoneEvent(block.getRelative(-1, -1, 0), event);
      this.doRedstoneEvent(block.getRelative(0, -1, 1), event);
      this.doRedstoneEvent(block.getRelative(0, -1, -1), event);
      this.doRedstoneEvent(block.getRelative(0, 1, 1), event);
      this.doRedstoneEvent(block.getRelative(0, 1, -1), event);
   }

   private void doRedstoneEvent(Block block, BlockRedstoneEvent event) {
      if(block.getTypeId() == Material.WALL_SIGN.getId() || block.getTypeId() == Material.SIGN_POST.getId()) {
         Sign sign = (Sign)block.getState();
         String line = sign.getLine(1);
         if(line != null) {
            MechanicListener listener = FalseBookBlockCore.getInstance().getMechanicHandler().getMechanicByLine(line);
            if(listener != null && listener.isActivatedByRedstone(block, event)) {
               if(this.queuedEventsPos.containsKey(block.getLocation().toString())) {
                  return;
               }

               this.queuedEventsPos.put(block.getLocation().toString(), Integer.valueOf(0));
               this.queuedEvents.add(new QueuedExecutionEvent(FalseBookBlockCore.getInstance(), block, event.getBlock().getLocation()));
            }

            if(this.mainTaskID == -1) {
               this.mainTaskID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FalseBookBlockCore.getInstance(), this, 1L);
            }

         }
      }
   }

   public void onSignChange(SignChangeEvent event) {
      if(event.getBlock().getTypeId() == Material.WALL_SIGN.getId() || event.getBlock().getTypeId() == Material.SIGN_POST.getId()) {
         MechanicListener listener = FalseBookBlockCore.getInstance().getMechanicHandler().getMechanicByLine(event.getLine(1));
         if(listener != null) {
            listener.onSignChange(event);
         }

      }
   }

   public void onPlayerInteract(PlayerInteractEvent event) {
      if(event.hasBlock()) {
         boolean isWallSign = event.getClickedBlock().getTypeId() == Material.WALL_SIGN.getId();
         boolean isSignPost = event.getClickedBlock().getTypeId() == Material.SIGN_POST.getId();
         FalseBookBlockCore.getInstance().getMechanicHandler().onPlayerInteract(event, isWallSign, isSignPost);
      }
   }

   public void onEntityChangeBlock(EntityChangeBlockEvent event) {
      FalseBookBlockCore.getInstance().getMechanicHandler().onEntityChangeBlock(event);
   }

   public void onEntityExplode(EntityExplodeEvent event) {
      FalseBookBlockCore.getInstance().getMechanicHandler().onEntityExplode(event);
   }
}
