package com.trn.spark.core

import org.apache.spark.{SparkConf, SparkContext}

object Main {


    val conf = new SparkConf().setAppName("Scraper").setMaster("local[2]")
    val sc = new SparkContext(conf)

  val rdd1 = sc.parallelize(List(1,2,3,4,5))


}
