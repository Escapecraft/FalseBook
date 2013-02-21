package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1200;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1201;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1203;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1205;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1206;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1207;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1210;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1211;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1231;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1232;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1240;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1241;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1265;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1285;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1510;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1511;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC1700;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.MC3231;

public class WorldEditPackage extends ExternalICPackage {

   public WorldEditPackage() {
      this.setAPI_VERSION("1.1");
      this.setShowImportMessages(false);
      this.addIC(MC1200.class);
      this.addIC(MC1201.class);
      this.addIC(MC1203.class);
      this.addIC(MC1205.class);
      this.addIC(MC1206.class);
      this.addIC(MC1207.class);
      this.addIC(MC1210.class);
      this.addIC(MC1211.class);
      this.addIC(MC1231.class);
      this.addIC(MC1232.class);
      this.addIC(MC1240.class);
      this.addIC(MC1241.class);
      this.addIC(MC1265.class);
      this.addIC(MC1285.class);
      this.addIC(MC1510.class);
      this.addIC(MC1511.class);
      this.addIC(MC1700.class);
      this.addIC(MC3231.class);
   }
}
