package com.bukkit.gemo.FalseBook.Block.Config;

import com.bukkit.gemo.FalseBook.Block.Handler.WorldHandlerBlock;
import com.bukkit.gemo.FalseBook.Block.World.EnumSettings;
import com.bukkit.gemo.FalseBook.Settings.WorldSettings;
import com.bukkit.gemo.FalseBook.Values.ValueIntegerList;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ConfigHandler {

   private static WorldHandlerBlock worldHandler;


   public ConfigHandler() {
      worldHandler = new WorldHandlerBlock();
   }

   public static void loadWorldSettings() {
      for(int i = 0; i < Bukkit.getWorlds().size(); ++i) {
         getOrCreateSettings(((World)Bukkit.getWorlds().get(i)).getName());
      }

   }

   public static WorldSettings getOrCreateSettings(String worldName) {
      return worldHandler.getWorld(worldName).getSettings();
   }

   public static void clearAllSettings() {
      worldHandler = new WorldHandlerBlock();
   }

   public static int getAreaSelectionTool(String worldName) {
      return getOrCreateSettings(worldName).getInteger(EnumSettings.AREA_TOOL.getName());
   }

   public static boolean isRedstoneAllowedForAreas(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.AREA_ALLOW_REDSTONE.getName());
   }

   public static float getAppleDropChance(String worldName) {
      return getOrCreateSettings(worldName).getFloat(EnumSettings.APPLE_DROP_CHANCE.getName());
   }

   public static boolean isReadBooks(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.BOOKSHELFS_ENABLED.getName());
   }

   public static boolean isLightswitchEnabled(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.LIGHTSWITCH_ENABLED.getName());
   }

   public static int getLightswitchMaxToggle(String worldName) {
      return getOrCreateSettings(worldName).getInteger(EnumSettings.LIGHTSWITCH_MAX_TOGGLE.getName());
   }

   public static boolean isBridgeEnabled(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.BRIDGE_ENABLED.getName());
   }

   public static boolean isRedstoneAllowedForBridges(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.BRIDGE_ALLOW_REDSTONE.getName());
   }

   public static int getMaxBridgeLength(String worldName) {
      return getOrCreateSettings(worldName).getInteger(EnumSettings.BRIDGE_MAX_LENGTH.getName());
   }

   public static int getMaxBridgeSideWidth(String worldName) {
      return getOrCreateSettings(worldName).getInteger(EnumSettings.BRIDGE_MAX_SIDEWIDTH.getName());
   }

   public static ValueIntegerList getAllowedBridgeBlocks(String worldName) {
      return getOrCreateSettings(worldName).getIntegerMap(EnumSettings.BRIDGE_ALLOWED_BLOCKS.getName());
   }

   public static boolean isDoorEnabled(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.DOOR_ENABLED.getName());
   }

   public static boolean isRedstoneAllowedForDoors(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.DOOR_ALLOW_REDSTONE.getName());
   }

   public static int getMaxDoorSideWidth(String worldName) {
      return getOrCreateSettings(worldName).getInteger(EnumSettings.DOOR_MAX_SIDEWIDTH.getName());
   }

   public static int getMaxDoorHeight(String worldName) {
      return getOrCreateSettings(worldName).getInteger(EnumSettings.DOOR_MAX_HEIGHT.getName());
   }

   public static ValueIntegerList getAllowedDoorBlocks(String worldName) {
      return getOrCreateSettings(worldName).getIntegerMap(EnumSettings.DOOR_ALLOWED_BLOCKS.getName());
   }

   public static boolean isGateEnabled(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.GATE_ENABLED.getName());
   }

   public static boolean isRedstoneAllowedForGates(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.GATE_ALLOW_REDSTONE.getName());
   }

   public static int getMaxGateHeight(String worldName) {
      return getOrCreateSettings(worldName).getInteger(EnumSettings.GATE_MAX_HEIGHT.getName());
   }

   public static int getMaxGateWidth(String worldName) {
      return getOrCreateSettings(worldName).getInteger(EnumSettings.GATE_MAX_WIDTH.getName());
   }

   public static ValueIntegerList getAllowedGateBlocks(String worldName) {
      return getOrCreateSettings(worldName).getIntegerMap(EnumSettings.GATE_ALLOWED_BLOCKS.getName());
   }

   public static boolean isAllowMinecraftCauldron(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.CAULDRON_NATIVE.getName());
   }

   public static int getCauldronCoolDownTime(String worldName) {
      return getOrCreateSettings(worldName).getInteger(EnumSettings.CAULDRON_COOLDOWN.getName());
   }

   public static boolean isRespectLWCProtections(String worldName) {
      return getOrCreateSettings(worldName).getBoolean(EnumSettings.LWC_RESPECT.getName());
   }
}
