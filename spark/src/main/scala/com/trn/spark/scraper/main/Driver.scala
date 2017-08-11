package com.trn.spark.scraper.main

import com.markmonitor.aviator.common.writable.LinkInterface
import com.markmonitor.aviator.plugins.plugin.ScraperPlugins
import com.markmonitor.aviator.plugins.plugin.parser.FetcherPlugin
import com.markmonitor.aviator.plugins.utils.HTTPRequestImpl.HTTPRequestFactory
import com.markmonitor.aviator.plugins.utils.{HTTPRequest, HTTPResponse, Link}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import com.trn.spark.scraper.util.Helper._


object Driver extends App{


  val conf = new SparkConf().setAppName("Scraper").setMaster("local[2]")
  val sc = new SparkContext(conf)


  //read input file and create pairrdd
  val rddFile: RDD[(String, String)] = sc.textFile("spark/src/main/resources/urls.txt").map(x => (x.split(",")(0) , x.split(",")(1)))


  /**
    * Scraper Plugins contains all the plugins...
    * Fetcher Plugin is for that particular type of url
    *
    * We have to call AbstractHttpRequest's fetch method from aviator-plugins.... this method expects LinkInterface object..
    * FetchRequest object in scraper-common project is extending LinkInterface...so we can borrow the definition from there to create similar object in this project
    * or else we can use Link object in aviator-plugins ...this one also extends LinkInterface
    *
    * We have to create the httpreq from HTTPRequestFactory.create method .. it expects object of FetcherPlugin and LinkInterface
    */


  /**
    * for every link we have to download the source
    */

  val downloadedRdd: RDD[(String, HTTPResponse)] = rddFile.mapPartitions { partition: Iterator[(String, String)] =>

    val scraperPlugins: ScraperPlugins = getScraperPlugins

    /**
      * Evaluate this partition ... then create a new partition which has fetcher plugin from above scraperPlugins
      */
    val newPartition: Iterator[(String, HTTPResponse)] = partition.map{ record: (String, String) =>
      //create a linkInterface object... ie link object
      val link: LinkInterface = new Link
      link.setUrl(record._2)
      link.setLinkAbstract(record._1)


      val fetcherPlugin: FetcherPlugin = scraperPlugins.getFetcherPlugin(link.getUrl)


      val httpReq: HTTPRequest = HTTPRequestFactory.create(fetcherPlugin,link)
      (link.getUrl,httpReq.fetch(link))

    }

    newPartition.toIterator // remove this
  }

  downloadedRdd.foreach(x => println(s"Each new row is => ${x._2.getContent}"))
}
