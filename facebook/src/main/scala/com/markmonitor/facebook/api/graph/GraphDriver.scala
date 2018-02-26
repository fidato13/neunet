package com.markmonitor.facebook.api.graph

import java.util.Calendar

object GraphDriver extends App {

  // lets start!!

  import java.util.Calendar

  val cal = Calendar.getInstance
  val year = cal.get(Calendar.YEAR)
  val month = cal.get(Calendar.MONTH)
  val date = cal.get(Calendar.DATE - 1)
  cal.clear()
  cal.set(year, month, date)
  val todayMillis2 = cal.getTimeInMillis

  

  val cali = Calendar.getInstance
  cali.add(Calendar.DAY_OF_YEAR, -5)
  /*cali.set(Calendar.HOUR_OF_DAY, 0)
  cali.set(Calendar.MINUTE, 0)
  cali.set(Calendar.SECOND, 0)
  cali.set(Calendar.MILLISECOND, 0)*/
  val fiveDaysAgo = cali.getTimeInMillis

  println("date time is => "+System.currentTimeMillis()/1000)
  println("days ago => "+ fiveDaysAgo/1000)
  //1505216935676
  //1505216980722
  //1505217006777
  //1505217026286
  //1505217046036
  //1505217059777
  
  val url1 = "https://graph.facebook.com/v2.10/copyright_video_search&since=1505001600&until=1505143022&access_token=1145777948856035%7COX50tOxjCWNT9ASVz1rs0_7jDD0"
  
  val indexOfSince = url1.indexOf("until=")
  val indexOfNextAmp = url1.indexOf("&", indexOfSince + 1);
  
  val sinceComplete = url1.substring(indexOfSince,indexOfNextAmp)
  
  println(sinceComplete)
  
}
