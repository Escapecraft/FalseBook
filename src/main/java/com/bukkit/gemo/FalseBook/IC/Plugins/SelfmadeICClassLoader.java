package com.bukkit.gemo.FalseBook.IC.Plugins;

import com.bukkit.gemo.FalseBook.IC.Plugins.SelfmadeICLoader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SelfmadeICClassLoader extends URLClassLoader {

   private final SelfmadeICLoader loader;
   private final Map classes = new HashMap();


   public SelfmadeICClassLoader(SelfmadeICLoader loader, URL[] urls, ClassLoader parent) {
      super(urls, parent);
      this.loader = loader;
   }

   protected Class findClass(String name) throws ClassNotFoundException {
      return this.findClass(name, true);
   }

   protected Class findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
      Class result = (Class)this.classes.get(name);
      if(result == null) {
         if(checkGlobal) {
            result = this.loader.getClassByName(name);
         }

         if(result == null) {
            result = super.findClass(name);
            if(result != null) {
               this.loader.setClass(name, result);
            }
         }

         this.classes.put(name, result);
      }

      return result;
   }

   public Set getClasses() {
      return this.classes.keySet();
   }
}
