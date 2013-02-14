package com.bukkit.gemo.FalseBook.Mechanics;

import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MechanicHandler {

   private Map registeredEvents;
   private Map registeredListeners;


   public MechanicHandler() {
      this.registerEvents();
   }

   private void registerEvents() {
      this.registeredListeners = new HashMap();
      this.registeredEvents = new HashMap();
      this.registeredEvents.put(BlockBreakEvent.class, new ArrayList());
      this.registeredEvents.put(BlockPlaceEvent.class, new ArrayList());
      this.registeredEvents.put(BlockPistonExtendEvent.class, new ArrayList());
      this.registeredEvents.put(BlockPistonRetractEvent.class, new ArrayList());
      this.registeredEvents.put(BlockRedstoneEvent.class, new ArrayList());
      this.registeredEvents.put(SignChangeEvent.class, new ArrayList());
      this.registeredEvents.put(EntityChangeBlockEvent.class, new ArrayList());
      this.registeredEvents.put(EntityExplodeEvent.class, new ArrayList());
      this.registeredEvents.put(PlayerInteractEvent.class, new ArrayList());
      this.registeredEvents.put(PlayerLoginEvent.class, new ArrayList());
      this.registeredEvents.put(PlayerPreLoginEvent.class, new ArrayList());
      this.registeredEvents.put(PlayerQuitEvent.class, new ArrayList());
      this.registeredEvents.put(PlayerRespawnEvent.class, new ArrayList());
      this.registeredEvents.put(PlayerTeleportEvent.class, new ArrayList());
   }

   public boolean registerEvent(Class eventClass, MechanicListener listener) {
      return ((List)this.registeredEvents.get(eventClass)).add(listener);
   }

   public boolean unregisterEvent(Class eventClass, MechanicListener listener) {
      return ((List)this.registeredEvents.get(eventClass)).remove(listener);
   }

   public boolean registerMechanic(String name, MechanicListener listener) {
      return this.registeredListeners.put(name, listener) != null;
   }

   public boolean unregisterMechanic(String name) {
      return this.registeredListeners.remove(name) != null;
   }

   public MechanicListener getMechanic(String name) {
      return (MechanicListener)this.registeredListeners.get(name);
   }

   public Map getAllMechanics() {
      return this.registeredListeners;
   }

   public boolean hasMechanic(String name) {
      return this.registeredListeners.containsKey(name);
   }

   public void onLoad() {
      Iterator var2 = this.registeredListeners.values().iterator();

      while(var2.hasNext()) {
         MechanicListener listener = (MechanicListener)var2.next();
         listener.onLoad();
      }

   }

   public void onUnload() {
      Iterator var2 = this.registeredListeners.values().iterator();

      while(var2.hasNext()) {
         MechanicListener listener = (MechanicListener)var2.next();
         listener.onUnload();
      }

   }

   public void onCommand(CommandSender sender, Command command, String label, String[] args) {
      Iterator var6 = this.registeredListeners.values().iterator();

      while(var6.hasNext()) {
         MechanicListener listener = (MechanicListener)var6.next();
         listener.onCommand(sender, command, label, args);
      }

   }

   public void onBlockBreak(BlockBreakEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onBlockBreak(event);
         }

         mechanicList = null;
      }
   }

   public void onBlockPlace(BlockPlaceEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onBlockPlace(event);
         }

         mechanicList = null;
      }
   }

   public void onBlockRedstoneChange(BlockRedstoneEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onBlockRedstoneChange(event);
         }

         mechanicList = null;
      }
   }

   public void onBlockPistonExtend(BlockPistonExtendEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onBlockPistonExtend(event);
         }

         mechanicList = null;
      }
   }

   public void onBlockPistonRetract(BlockPistonRetractEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onBlockPistonRetract(event);
         }

         mechanicList = null;
      }
   }

   public void onSignChange(SignChangeEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onSignChange(event);
         }

         mechanicList = null;
      }
   }

   public void onPlayerInteract(PlayerInteractEvent event, boolean isWallSign, boolean isSignPost) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var6 = mechanicList.iterator();

         while(var6.hasNext()) {
            MechanicListener listener = (MechanicListener)var6.next();
            listener.onPlayerInteract(event, isWallSign, isSignPost);
         }

         mechanicList = null;
      }
   }

   public void onPlayerPreLogin(PlayerPreLoginEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onPlayerPreLogin(event);
         }

         mechanicList = null;
      }
   }

   public void onPlayerLogin(PlayerLoginEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onPlayerLogin(event);
         }

         mechanicList = null;
      }
   }

   public void onPlayerQuit(PlayerQuitEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onPlayerQuit(event);
         }

         mechanicList = null;
      }
   }

   public void onPlayerTeleport(PlayerTeleportEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onPlayerTeleport(event);
         }

         mechanicList = null;
      }
   }

   public void onPlayerRespawn(PlayerRespawnEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onPlayerRespawn(event);
         }

         mechanicList = null;
      }
   }

   public void onEntityExplode(EntityExplodeEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onEntityExplode(event);
         }

         mechanicList = null;
      }
   }

   public void onEntityChangeBlock(EntityChangeBlockEvent event) {
      List mechanicList = (List)this.registeredEvents.get(event.getClass());
      if(mechanicList != null) {
         Iterator var4 = mechanicList.iterator();

         while(var4.hasNext()) {
            MechanicListener listener = (MechanicListener)var4.next();
            listener.onEntityChangeBlock(event);
         }

         mechanicList = null;
      }
   }
}
