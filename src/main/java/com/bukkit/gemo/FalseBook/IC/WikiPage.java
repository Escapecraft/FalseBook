package com.bukkit.gemo.FalseBook.IC;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class WikiPage {

   ArrayList lines = new ArrayList();


   public WikiPage(String template) {
      try {
         File e = new File(template);
         if(!e.exists()) {
            return;
         }

         BufferedReader in = new BufferedReader(new FileReader(template));
         String zeile = "";

         while((zeile = in.readLine()) != null) {
            this.lines.add(zeile.trim());
         }

         in.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void replaceText(String placeHolder, String newText) {
      String line = "";

      for(int i = 0; i < this.lines.size(); ++i) {
         line = (String)this.lines.get(i);
         if(line.contains(placeHolder)) {
            this.lines.set(i, line.replace(placeHolder, newText));
         }
      }

   }

   public void savePage(String fileName) {
      try {
         File e = new File(fileName);
         if(e.exists()) {
            e.delete();
         }

         File savedFile = new File(fileName);
         FileWriter writer = new FileWriter(savedFile, false);

         for(int i = 0; i < this.lines.size(); ++i) {
            writer.write((String)this.lines.get(i) + System.getProperty("line.separator"));
         }

         writer.flush();
         writer.close();
      } catch (Exception var6) {
         FalseBookICCore.printInConsole("Error while writing Wikipage: " + fileName);
      }

   }
}
