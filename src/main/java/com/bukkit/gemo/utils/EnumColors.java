package com.bukkit.gemo.utils;

import org.bukkit.ChatColor;

public enum EnumColors {

   AQUA("AQUA", 0, "aqua", ChatColor.AQUA),
   BLACK("BLACK", 1, "black", ChatColor.BLACK),
   BLUE("BLUE", 2, "blue", ChatColor.BLUE),
   DARK_AQUA("DARK_AQUA", 3, "darkaqua", ChatColor.DARK_AQUA),
   DARK_BLUE("DARK_BLUE", 4, "darkblue", ChatColor.DARK_BLUE),
   DARK_GRAY("DARK_GRAY", 5, "darkgray", ChatColor.DARK_GRAY),
   DARK_GREEN("DARK_GREEN", 6, "darkgreen", ChatColor.DARK_GREEN),
   DARK_PURPLE("DARK_PURPLE", 7, "darkpurple", ChatColor.DARK_PURPLE),
   DARK_RED("DARK_RED", 8, "darkred", ChatColor.DARK_RED),
   GOLD("GOLD", 9, "gold", ChatColor.GOLD),
   GRAY("GRAY", 10, "gray", ChatColor.GRAY),
   GREEN("GREEN", 11, "green", ChatColor.GREEN),
   LIGHT_PURPLE("LIGHT_PURPLE", 12, "lightpurple", ChatColor.LIGHT_PURPLE),
   RED("RED", 13, "red", ChatColor.RED),
   WHITE("WHITE", 14, "white", ChatColor.WHITE),
   YELLOW("YELLOW", 15, "yellow", ChatColor.YELLOW);
   private final ChatColor color;
   private final String name;
   // $FF: synthetic field
   private static final EnumColors[] ENUM$VALUES = new EnumColors[]{AQUA, BLACK, BLUE, DARK_AQUA, DARK_BLUE, DARK_GRAY, DARK_GREEN, DARK_PURPLE, DARK_RED, GOLD, GRAY, GREEN, LIGHT_PURPLE, RED, WHITE, YELLOW};


   private EnumColors(String var1, int var2, String name, ChatColor color) {
      this.name = name;
      this.color = color;
   }

   public String getName() {
      return this.name;
   }

   public ChatColor getColor() {
      return this.color;
   }

   public static ChatColor getColor(String name) {
      name = name.replace("_", "");
      EnumColors[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         EnumColors enums = var4[var2];
         if(enums.getName().equalsIgnoreCase(name)) {
            return enums.getColor();
         }
      }

      return null;
   }
}
