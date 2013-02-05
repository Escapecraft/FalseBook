package com.bukkit.gemo.FalseBook.IC.Plugins;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import com.bukkit.gemo.FalseBook.IC.Plugins.SelfmadeICClassLoader;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.plugin.PluginDescriptionFile;

public class SelfmadeICLoader {

   protected final Map classes = new HashMap();
   protected final Map loaders = new HashMap();


   public ExternalICPackage loadPlugin(File file) {
      ExternalICPackage result = null;
      PluginDescriptionFile description = null;
      if(!file.exists()) {
         FalseBookICCore.printInConsole("ERROR: plugin \'" + file.getName() + "\' not found!");
         return result;
      } else {
         JarFile loader;
         try {
            loader = new JarFile(file);
            JarEntry e = loader.getJarEntry("plugin.yml");
            if(e == null) {
               FalseBookICCore.printInConsole("Jar \'" + file.getName() + "\' does not contain plugin.yml");
            }

            InputStream jarClass = loader.getInputStream(e);
            description = new PluginDescriptionFile(jarClass);
            jarClass.close();
            loader.close();
         } catch (Exception var10) {
            var10.printStackTrace();
            return null;
         }

         loader = null;

         SelfmadeICClassLoader loader1;
         try {
            URL[] e1 = new URL[]{file.toURI().toURL()};
            loader1 = new SelfmadeICClassLoader(this, e1, this.getClass().getClassLoader());
            Class jarClass1 = Class.forName(description.getMain(), true, loader1);
            if(!(jarClass1.newInstance() instanceof ExternalICPackage)) {
               return null;
            }

            Class plugin = jarClass1.asSubclass(ExternalICPackage.class);
            Constructor constructor = plugin.getConstructor(new Class[0]);
            result = (ExternalICPackage)constructor.newInstance(new Object[0]);
         } catch (Exception var9) {
            var9.printStackTrace();
            return null;
         }

         this.loaders.put(description.getName(), loader1);
         return result;
      }
   }

   public Class getClassByName(String name) {
      Class cachedClass = (Class)this.classes.get(name);
      if(cachedClass != null) {
         return cachedClass;
      } else {
         Iterator var4 = this.loaders.keySet().iterator();

         while(var4.hasNext()) {
            String current = (String)var4.next();
            SelfmadeICClassLoader loader = (SelfmadeICClassLoader)this.loaders.get(current);

            try {
               cachedClass = loader.findClass(name, false);
            } catch (ClassNotFoundException var7) {
               ;
            }

            if(cachedClass != null) {
               return cachedClass;
            }
         }

         return null;
      }
   }

   public void setClass(String name, Class clazz) {
      if(!this.classes.containsKey(name)) {
         this.classes.put(name, clazz);
      }

   }
}
