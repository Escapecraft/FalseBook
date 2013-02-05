package com.bukkit.gemo.FalseBook.IC.ICs;

import org.bukkit.Location;

public class NotLoadedIC {

   private int ID;
   private String ICNumber;
   private String ICName;
   private Location ICLocation;
   private int SensorID;


   public NotLoadedIC(int ID, Location location) {
      this.ID = -1;
      this.ICNumber = "";
      this.ICName = "";
      this.ICLocation = null;
      this.ID = ID;
      this.ICLocation = location;
   }

   public NotLoadedIC(int ID, Location location, int SensorID) {
      this.ID = -1;
      this.ICNumber = "";
      this.ICName = "";
      this.ICLocation = null;
      this.ID = ID;
      this.ICLocation = location;
      this.SensorID = SensorID;
   }

   public NotLoadedIC(int ID, String ICNumber, Location location) {
      this(ID, location);
      this.ICNumber = ICNumber;
   }

   public NotLoadedIC(int ID, String ICNumber, String ICName, Location location) {
      this(ID, ICNumber, location);
      this.ICName = ICName;
   }

   public int getID() {
      return this.ID;
   }

   public String getICNumber() {
      return this.ICNumber;
   }

   public String getName() {
      return this.ICName;
   }

   public Location getICLocation() {
      return this.ICLocation;
   }

   public int getSensorID() {
      return this.SensorID;
   }
}
