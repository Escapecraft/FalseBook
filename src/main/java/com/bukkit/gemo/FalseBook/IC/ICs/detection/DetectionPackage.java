package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1230;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1260;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1261;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1262;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1263;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1264;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1270;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1271;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1272;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1280;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1281;
import com.bukkit.gemo.FalseBook.IC.ICs.detection.MC1282;

public class DetectionPackage extends ExternalICPackage {

   public DetectionPackage() {
      this.setAPI_VERSION("1.1");
      this.setShowImportMessages(false);
      this.addIC(MC1230.class);
      this.addIC(MC1260.class);
      this.addIC(MC1261.class);
      this.addIC(MC1262.class);
      this.addIC(MC1263.class);
      this.addIC(MC1264.class);
      this.addIC(MC1270.class);
      this.addIC(MC1271.class);
      this.addIC(MC1272.class);
      this.addIC(MC1280.class);
      this.addIC(MC1281.class);
      this.addIC(MC1282.class);
   }
}
