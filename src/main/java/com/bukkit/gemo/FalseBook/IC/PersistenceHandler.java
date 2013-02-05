package com.bukkit.gemo.FalseBook.IC;

import com.bukkit.gemo.FalseBook.IC.DatabaseHandler;
import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.FalseBook.IC.ICs.NotLoadedIC;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;

public class PersistenceHandler {

   private FalseBookICCore plugin = null;
   private ICFactory factory = null;
   private DatabaseHandler dbHandler = null;


   public PersistenceHandler(FalseBookICCore plugin) {
      this.plugin = plugin;
   }

   public void init(ICFactory factory) {
      this.factory = factory;
   }

   public void initSQLite() {
      this.dbHandler = new DatabaseHandler("plugins" + System.getProperty("file.separator") + "FalseBook", "SelftriggeredICs");
   }

   public void initMySQL() {
      this.createMYSQLConfig();

      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "MySQL.yml");
         YamlConfiguration config = new YamlConfiguration();
         config.load(e);
         this.dbHandler = new DatabaseHandler(config.getString("mysql.host"), config.getInt("mysql.port"), config.getString("mysql.database"), config.getString("mysql.username"), config.getString("mysql.password"));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void createMYSQLConfig() {
      File file = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "MySQL.yml");
      if(!file.exists()) {
         try {
            YamlConfiguration e = new YamlConfiguration();
            e.set("mysql.host", "localhost");
            e.set("mysql.port", Integer.valueOf(3306));
            e.set("mysql.database", "databaseName");
            e.set("mysql.username", "username");
            e.set("mysql.password", "password");
            e.save(file);
         } catch (Exception var3) {
            var3.printStackTrace();
         }

      }
   }

   public void closeSQL() {
      this.dbHandler.closeConnection();
   }

   public void loadSelftriggeredICs() {
      ResultSet result = this.dbHandler.getAllICs();
      ArrayList loadingList = new ArrayList();

      try {
         Sign signBlock;
         while(result != null && result.next()) {
            int e = result.getInt("Id");
            String thisIC = result.getString("WorldName");
            int sensorID = result.getInt("SensorId");
            int nIC = result.getInt("SignX");
            int e1 = result.getInt("SignY");
            int z = result.getInt("SignZ");
            Location location = new Location(Bukkit.getServer().getWorld(thisIC), (double)nIC, (double)e1, (double)z, 0.0F, 0.0F);

            try {
               if(location.getWorld() != null) {
                  location.getWorld().loadChunk(location.getBlock().getChunk().getX(), location.getBlock().getChunk().getZ(), true);
                  Block e2 = location.getBlock();
                  if(location.getBlock().getTypeId() == Material.WALL_SIGN.getId()) {
                     signBlock = (Sign)e2.getState();
                     loadingList.add(new NotLoadedIC(e, location, sensorID));
                  } else {
                     this.factory.addFailedIC(new NotLoadedIC(e, "UNKNOWN", "NO SIGN FOUND", location));
                  }
               }
            } catch (Exception var12) {
               loadingList.add(new NotLoadedIC(e, location, sensorID));
               this.factory.addFailedIC(new NotLoadedIC(e, "UNKNOWN", "BUKKIT ERROR - try /fbic reloadics", location));
            }
         }

         Iterator sensorID1 = loadingList.iterator();

         while(sensorID1.hasNext()) {
            NotLoadedIC thisIC1 = (NotLoadedIC)sensorID1.next();
            signBlock = (Sign)thisIC1.getICLocation().getBlock().getState();
            SelftriggeredBaseIC nIC1 = null;
            Iterator z1 = this.factory.getRegisteredSTICsEntrys().iterator();

            while(z1.hasNext()) {
               Entry e5 = (Entry)z1.next();
               if(signBlock.getLine(1) != null && ((SelftriggeredBaseIC)e5.getValue()).getTypeID() == thisIC1.getSensorID() && ((SelftriggeredBaseIC)e5.getValue()).getICNumber().equalsIgnoreCase(signBlock.getLine(1))) {
                  nIC1 = (SelftriggeredBaseIC)e5.getValue();
                  break;
               }
            }

            if(nIC1 != null) {
               try {
                  boolean e4 = false;
                  SelftriggeredBaseIC e3 = (SelftriggeredBaseIC)nIC1.getClass().newInstance();
                  e4 = e3.initIC(this.plugin, thisIC1.getICLocation());
                  if(e3.getSignBlock() != null && e4) {
                     e4 = e3.onLoad(e3.getSignBlock().getLines());
                     if(e4) {
                        e3.initCore();
                        e3.initIC(FalseBookICCore.getInstance(), thisIC1.getICLocation());
                        this.factory.addSelftriggeredIC(thisIC1.getICLocation(), e3);
                     } else {
                        this.factory.addFailedIC(new NotLoadedIC(thisIC1.getID(), e3.getSignBlock().getLine(1), e3.getSignBlock().getLine(0), thisIC1.getICLocation()));
                     }
                  } else {
                     this.factory.addFailedIC(new NotLoadedIC(thisIC1.getID(), "UNKNOWN", "NO SIGN FOUND", thisIC1.getICLocation()));
                  }
               } catch (Exception var13) {
                  var13.printStackTrace();
               }
            } else {
               this.factory.addFailedIC(new NotLoadedIC(thisIC1.getID(), "UNKNOWN", "NO IC FOUND ON THE SIGN", thisIC1.getICLocation()));
            }
         }

         FalseBookICCore.printInConsole("Loaded selftriggered ICs: " + this.factory.getSensorListSize() + " done");
         FalseBookICCore.printInConsole("Failed selftriggered ICs: " + this.factory.getFailedICsSize() + " failed");
      } catch (SQLException var14) {
         FalseBookICCore.printInConsole("Error while loading selftriggered ICs: ");
         FalseBookICCore.printInConsole("");
         var14.printStackTrace();
      }

   }

   public boolean addSelftriggeredICToDB(int SensorID, Location location) {
      return this.STICExistsInDB(location)?false:this.dbHandler.addIC(SensorID, location);
   }

   public int getNextID() {
      return this.dbHandler.getNextID();
   }

   public void removeSelftriggeredIC(Location location) {
      this.dbHandler.removeSelftriggeredIC(location);
   }

   public void clearAllSelftriggeredICs() {
      this.dbHandler.deleteAllICs();
   }

   public boolean STICExistsInDB(Location location) {
      return this.dbHandler.ICExists(location);
   }
}
