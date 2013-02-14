package com.bukkit.gemo.utils;


public class MyEventStatistic {

   public long allTime;
   public long minTime;
   public long maxTime;
   public long eventCount = 0L;


   public void update(long duration) {
      this.allTime += duration;
      if(duration < this.minTime) {
         this.minTime = duration;
      }

      if(duration > this.maxTime) {
         this.maxTime = duration;
      }

      ++this.eventCount;
   }
}
