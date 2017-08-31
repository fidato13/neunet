package com.trn.solr

import java.util

import org.apache.solr.client.solrj.impl.CloudSolrServer
import org.apache.solr.common.cloud.Slice
import play.api.libs.json._

import scala.collection.JavaConverters._
import scalaj.http.Http

object SolrMonitor { 
  
  case class Shard(state:String)
  
  implicit val shards = Json.reads[Shard]
  
  //ec2-54-246-60-187.eu-west-1.compute.amazonaws.com
  //http://ec2-54-246-60-187.eu-west-1.compute.amazonaws.com:8983/solr/admin/cores?wt=json&indexInfo=false&_=1504109848146
  //.indexOf(",\"tree\":")
  
  def checkCollections(collectionName: String): List[Shard] = {
    val jsonStateCollection: String = Http(s"http://ec2-54-246-60-187.eu-west-1.compute.amazonaws.com:8983/solr/admin/zookeeper?wt=json&detail=true&path=/collections/$collectionName/state.json").asString.body
    
    val jsvalueString: JsValue = Json.parse(jsonStateCollection)
    val dataString = (jsvalueString \ "znode" \ "data").get.toString.replace("\\n","").replace("\\","")
    val finalDataString = dataString.substring(1,dataString.length - 1)
    
    val jsValueData: String = (Json.parse(finalDataString) \ collectionName \ "shards").get.toString
      .replace("{\"shard1\"","[{\"shard1\"")
      .replace("{\"shard1\":","")
      .replace("\"shard2\":","")
      .replace("\"shard3\":","")
      .replace("\"shard4\":","")
      .replace("\"shard5\":","")
      .replace("\"shard6\":","")
      .replace("\"shard7\":","")
      .replace("\"shard8\":","")
      .replace("\"shard9\":","")
      .replace("\"shard10\":","")
      .replace("\"shard11\":","")
      .replace("\"shard12\":","")
      .replace("\"shard13\":","")
      .replace("\"shard14\":","")
      .replace("\"shard15\":","")
      .replace("\"shard16\":","")
      .replace("\"shard17\":","")
      .replace("\"shard18\":","")
      .replace("}}}}","}}}]")
    val jsValueDataFinal: JsValue = Json.parse(jsValueData)
    
    val value: List[Shard] = Json.fromJson[List[Shard]](jsValueDataFinal).get
    
  
    value
  }
  
  
  def ifAllShardsAreUp = {
    val downloadCollectionList = checkCollections("download")
    val eventCollectionList = checkCollections("event")
    val missingDownloadCollectionList = checkCollections("missingDownload")
    val seedurlsCollectionList = checkCollections("seedurls")
    val textVerifiedLinkCollectionList = checkCollections("textVerifiedLink")
    val userActionLogCollectionList = checkCollections("userActionLog")
    
    val combinedList = downloadCollectionList ++ eventCollectionList ++ missingDownloadCollectionList ++ seedurlsCollectionList ++ textVerifiedLinkCollectionList ++ userActionLogCollectionList
    
    
    combinedList.filter{_.state == "active"}.size == combinedList.size 
    
    
    
  }
  
  
}
