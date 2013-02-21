package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0020;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0230;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0232;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0260;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0261;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0262;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0263;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0264;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0265;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0270;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0271;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0272;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0280;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0281;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0282;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0285;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0420;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC9999;

public class SelftriggeredPackage extends ExternalICPackage {

   public SelftriggeredPackage() {
      this.setAPI_VERSION("1.1");
      this.setShowImportMessages(false);
      this.addIC(MC0020.class);
      this.addIC(MC0230.class);
      this.addIC(MC0232.class);
      this.addIC(MC0260.class);
      this.addIC(MC0261.class);
      this.addIC(MC0262.class);
      this.addIC(MC0263.class);
      this.addIC(MC0264.class);
      this.addIC(MC0265.class);
      this.addIC(MC0270.class);
      this.addIC(MC0271.class);
      this.addIC(MC0272.class);
      this.addIC(MC0280.class);
      this.addIC(MC0281.class);
      this.addIC(MC0282.class);
      this.addIC(MC0285.class);
      this.addIC(MC0420.class);
      this.addIC(MC9999.class);
   }
}
