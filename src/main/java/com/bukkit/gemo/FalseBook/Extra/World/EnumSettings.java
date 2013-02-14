package com.bukkit.gemo.FalseBook.Extra.World;


public enum EnumSettings {

   TOGGLE_PUMPKINS("TOGGLE_PUMPKINS", 0, "TogglePumpkins"),
   TOGGLE_NETHERRACK("TOGGLE_NETHERRACK", 1, "ToggleNetherrack"),
   TOGGLE_GLOWSTONE("TOGGLE_GLOWSTONE", 2, "ToggleGlowstone"),
   GLOWSTONE_OFF_ID("GLOWSTONE_OFF_ID", 3, "GlowstoneOffTypeID"),
   GLOWSTONE_OFF_DATA("GLOWSTONE_OFF_DATA", 4, "GlowstoneOffDataValue"),
   PROTECT_BLOCKS("PROTECT_BLOCKS", 5, "ProtectToggledBlocks");
   private final String thisName;
   // $FF: synthetic field
   private static final EnumSettings[] ENUM$VALUES = new EnumSettings[]{TOGGLE_PUMPKINS, TOGGLE_NETHERRACK, TOGGLE_GLOWSTONE, GLOWSTONE_OFF_ID, GLOWSTONE_OFF_DATA, PROTECT_BLOCKS};


   private EnumSettings(String var1, int var2, String name) {
      this.thisName = name;
   }

   public String getName() {
      return this.thisName;
   }
}
