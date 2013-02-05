package com.bukkit.gemo.FalseBook.IC.ICs;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.WikiPage;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.IC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.ICUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class BaseIC implements IC {

   protected FalseBookICCore core;
   protected Sign signBlock;
   protected String ICName = null;
   protected String ICNumber = null;
   private ICGroup Group = null;
   protected String ICDescription = "";
   protected BaseChip chipState = null;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$bukkit$gemo$FalseBook$IC$ICs$Lever;


   protected BaseIC(FalseBookICCore core) {
      this.core = core;
   }

   public BaseIC() {}

   public boolean hasPermission(Player player) {
      return UtilPermissions.playerCanUseCommand(player, "falsebook.ic." + this.Group.name().toLowerCase()) || UtilPermissions.playerCanUseCommand(player, "falsebook.ic." + this.ICNumber.toLowerCase().substring(1, this.ICNumber.length() - 1)) || UtilPermissions.playerCanUseCommand(player, "falsebook.anyic");
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {}

   public void Execute() {
      if(this.signBlock != null && this.core.isLoadUnloadedChunks() && !this.signBlock.getChunk().isLoaded()) {
         this.signBlock.getWorld().loadChunk(this.signBlock.getChunk().getX(), this.signBlock.getChunk().getZ());
      }

   }

   public boolean validateIC() {
      if(this.signBlock == null) {
         return false;
      } else if(this.signBlock.getTypeId() != Material.WALL_SIGN.getId()) {
         return false;
      } else if(!this.signBlock.getChunk().isLoaded() && !this.core.isLoadUnloadedChunks()) {
         return false;
      } else {
         if(this.signBlock.getLine(1).length() < 1) {
            this.signBlock = (Sign)this.signBlock.getLocation().getBlock().getState();
         }

         return this.ICNumber.equalsIgnoreCase(this.signBlock.getLine(1));
      }
   }

   public void checkCreation(SignChangeEvent event) {}

   public boolean onBreakByPlayer(Player player, Sign signBlock) {
      return true;
   }

   public void onBreakByExplosion(Sign signBlock) {}

   public void onRightClick(Player player, Sign signBlock) {}

   public void onLeftClick(Player player, Sign signBlock) {}

   public void notifyCreationSuccess(Player player) {
      ChatUtils.printSuccess(player, "[FB-IC]", this.ICName + " created.");
   }

   public static Location getICBlock(Sign signBlock) {
      Location leverPos = signBlock.getBlock().getLocation().clone();
      switch(signBlock.getRawData()) {
      case 2:
         leverPos.setZ(leverPos.getZ() + 1.0D);
         break;
      case 3:
         leverPos.setZ(leverPos.getZ() - 1.0D);
         break;
      case 4:
         leverPos.setX(leverPos.getX() + 1.0D);
         break;
      case 5:
         leverPos.setX(leverPos.getX() - 1.0D);
      }

      return leverPos;
   }

   public static Location getICBlock(Sign signBlock, Vector vector) {
      Location position = signBlock.getBlock().getLocation().clone();
      switch(signBlock.getRawData()) {
      case 2:
         position.setZ(position.getZ() + 1.0D + (double)vector.getBlockZ());
         position.setX(position.getX() - (double)vector.getBlockX());
         break;
      case 3:
         position.setZ(position.getZ() - 1.0D - (double)vector.getBlockZ());
         position.setX(position.getX() + (double)vector.getBlockX());
         break;
      case 4:
         position.setX(position.getX() + 1.0D + (double)vector.getBlockZ());
         position.setZ(position.getZ() + (double)vector.getBlockX());
         break;
      case 5:
         position.setX(position.getX() - 1.0D - (double)vector.getBlockZ());
         position.setZ(position.getZ() - (double)vector.getBlockX());
      }

      position.setY(position.getY() + (double)vector.getBlockY());
      return position;
   }

   public void ExportAsWikiHTML() {
      String folderName = "plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "IC-Wiki";
      File folder = new File(folderName);
      folder.mkdirs();
      File templateFile = new File(folderName + System.getProperty("file.separator") + "templates" + System.getProperty("file.separator") + "template_icpage.html");
      if(templateFile.exists()) {
         try {
            String e = folderName + System.getProperty("file.separator") + this.ICNumber.substring(1, this.ICNumber.length() - 1) + ".html";
            WikiPage thisPage = new WikiPage(folderName + System.getProperty("file.separator") + "templates" + System.getProperty("file.separator") + "template_icpage.html");
            thisPage.replaceText("%MCNAME%", this.ICNumber);
            thisPage.replaceText("%MCDESC%", this.ICName);
            thisPage.replaceText("%DESCRIPTION%", this.ICDescription);
            thisPage.replaceText("%IOIMAGE%", this.getIOLine());
            thisPage.replaceText(" " + this.ICNumber.substring(1, this.ICNumber.length() - 1) + " ", " <b>" + this.ICNumber.substring(1, this.ICNumber.length() - 1) + "</b> ");
            thisPage.replaceText("%PERMISSIONS%", this.getPermissionString());
            thisPage.replaceText("%SIGN%", this.getLineString());
            thisPage.replaceText("%INPUTS%", this.getInputString());
            thisPage.replaceText("%OUTPUTS%", this.getOutputString());
            thisPage.savePage(e);
         } catch (Exception var6) {
            FalseBookICCore.printInConsole("Error while exporting Wiki-File of " + this.ICNumber);
         }

      }
   }

   public String getInputString() {
      if(this.chipState.getNameInput1().length() == 0 && this.chipState.getNameInput2().length() == 0 && this.chipState.getNameInput3().length() == 0) {
         return "no input";
      } else {
         String result = "";
         result = "<ol type=\"1\">";
         if(this.chipState.getNameInput1().length() != 0) {
            result = result + "<li>" + this.chipState.getNameInput1() + "</li>";
         }

         if(this.chipState.getNameInput2().length() != 0) {
            result = result + "<li>" + this.chipState.getNameInput2() + "</li>";
         }

         if(this.chipState.getNameInput3().length() != 0) {
            result = result + "<li>" + this.chipState.getNameInput3() + "</li>";
         }

         result = result + "</ol>";
         return result;
      }
   }

   public String getOutputString() {
      if(this.chipState.getNameInput1().length() == 0 && this.chipState.getNameInput2().length() == 0 && this.chipState.getNameInput3().length() == 0) {
         return "no output";
      } else {
         String result = "";
         result = "<ol type=\"1\">";
         if(this.chipState.getNameInput1().length() != 0) {
            result = result + "<li>" + this.chipState.getNameInput1() + "</li>";
         }

         if(this.chipState.getNameInput2().length() != 0) {
            result = result + "<li>" + this.chipState.getNameInput2() + "</li>";
         }

         if(this.chipState.getNameInput3().length() != 0) {
            result = result + "<li>" + this.chipState.getNameInput3() + "</li>";
         }

         result = result + "</ol>";
         return result;
      }
   }

   public String getPermissionString() {
      String result = "";
      result = "<ul>";
      result = result + "<li>*.*</li>";
      result = result + "<li>falsebook.*</li>";
      result = result + "<li>falsebook.anyic</li>";
      result = result + "<li>falsebook.ic.*</li>";
      result = result + "<li>falsebook.ic." + this.Group.name().toLowerCase() + "</li>";
      result = result + "<li>falsebook.ic." + this.ICNumber.toLowerCase().substring(1, this.ICNumber.length() - 1) + "</li>";
      result = result + "</ul>";
      return result;
   }

   public String getLineString() {
      if(this.chipState.getSignLine3().length() == 0 && this.chipState.getSignLine4().length() == 0) {
         return "There are no sign parameters.";
      } else {
         String result = "";
         result = "<ul>";
         if(this.chipState.getSignLine3().length() != 0) {
            result = result + "<li>Line 3: " + this.chipState.getSignLine3() + "</li>";
         }

         if(this.chipState.getSignLine4().length() != 0) {
            result = result + "<li>Line 4: " + this.chipState.getSignLine4() + "</li>";
         }

         result = result + "</ul>";
         return result;
      }
   }

   public String getIOLine() {
      return "<img class=\"floatRight\" src=\"images/IO/" + this.chipState.getInputCount() + "I" + this.chipState.getOutputCount() + "O.png\">";
   }

   public void setSignBlock(Sign signBlock) {
      this.signBlock = signBlock;
   }

   public Sign getSignBlock() {
      return this.signBlock;
   }

   public ICGroup getICGroup() {
      return this.Group;
   }

   public void setICGroup(ICGroup icgroup) {
      this.Group = icgroup;
   }

   public String getICDescription() {
      return this.ICDescription;
   }

   public void setICDescription(String iCDescription) {
      this.ICDescription = iCDescription;
   }

   public BaseChip getChipState() {
      return this.chipState;
   }

   public void setChipState(BaseChip chipState) {
      this.chipState = chipState;
   }

   public String getICName() {
      return this.ICName;
   }

   public void setICName(String ICName) {
      this.ICName = ICName;
   }

   public String getICNumber() {
      return this.ICNumber;
   }

   public void setICNumber(String ICNumber) {
      this.ICNumber = ICNumber;
   }

   public void onImport() {}

   public void initCore() {
      this.core = FalseBookICCore.getInstance();
   }

   protected void switchLever(Lever lever, Sign signBlock, boolean newStatus) {
      switch($SWITCH_TABLE$com$bukkit$gemo$FalseBook$IC$ICs$Lever()[lever.ordinal()]) {
      case 1:
         ICUtils.switchLever(signBlock, newStatus);
         break;
      case 2:
         ICUtils.switchLeverLeft(signBlock, newStatus);
         break;
      case 3:
         ICUtils.switchLeverRight(signBlock, newStatus);
      }

   }

   protected void switchLever(Lever lever, Sign signBlock, boolean newStatus, int distance) {
      switch($SWITCH_TABLE$com$bukkit$gemo$FalseBook$IC$ICs$Lever()[lever.ordinal()]) {
      case 1:
         ICUtils.switchLever(signBlock, newStatus, distance);
         break;
      case 2:
         ICUtils.switchLeverLeft(signBlock, newStatus, distance);
         break;
      case 3:
         ICUtils.switchLeverRight(signBlock, newStatus, distance);
      }

   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$bukkit$gemo$FalseBook$IC$ICs$Lever() {
      if($SWITCH_TABLE$com$bukkit$gemo$FalseBook$IC$ICs$Lever != null) {
         return $SWITCH_TABLE$com$bukkit$gemo$FalseBook$IC$ICs$Lever;
      } else {
         int[] var0 = new int[Lever.values().length];

         try {
            var0[Lever.BACK.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[Lever.LEFT.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[Lever.RIGHT.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$bukkit$gemo$FalseBook$IC$ICs$Lever = var0;
         return var0;
      }
   }
}
