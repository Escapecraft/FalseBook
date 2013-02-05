package com.bukkit.gemo.FalseBook.IC.ICs;

import java.util.ArrayList;
import java.util.UUID;

public abstract class ExternalICPackage {

   private String API_VERSION = "UNKNOWN";
   private boolean showImportMessages = true;
   private ArrayList ICList = new ArrayList();


   public ArrayList getICList() {
      return this.ICList;
   }

   public String getAPI_VERSION() {
      return this.API_VERSION;
   }

   public void setAPI_VERSION(String API_VERSION) {
      this.API_VERSION = API_VERSION;
   }

   public static int getUniqueID(String string) {
      return UUID.nameUUIDFromBytes(string.getBytes()).hashCode();
   }

   protected void addIC(Class clazz) {
      this.ICList.add(clazz);
   }

   public boolean isShowImportMessages() {
      return this.showImportMessages;
   }

   protected boolean setShowImportMessages(boolean var) {
      this.showImportMessages = var;
      return this.showImportMessages;
   }
}
