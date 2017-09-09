package com.markmonitor.facebook.api.graph.services

import com.typesafe.config.ConfigFactory

import scalaj.http.Http

object Requests extends App {

  /**
    * Application configuration object.
    */
  final val config = ConfigFactory.load()
  
  val userAccessToken: String = config.getString("app.userAccessToken")
  val appToken: String = config.getString("app.appToken")
  //val accessToken: String = "265982633890221|b34b148d6dcfc299e1677874639ad5da"
  
  val graphApiBaseUrl = "https://graph.facebook.com/v2.10"
  
  val fieldsRequired = "id,content_category,length,backdated_time,created_time"
  //me?fields=id,name
  
   
  
  val videoId = "1435254349902737" 
  
  val myProfileQuery = s"$graphApiBaseUrl/me?fields=id,name&access_token=$userAccessToken"
  val videoOnlyQuery = s"$graphApiBaseUrl/$videoId?&access_token=$userAccessToken&fields=id,content_category,length,backdated_time,created_time"
  val videoEdgeCaptionsQuery = s"$graphApiBaseUrl/$videoId/auto_generated_captions?access_token=$userAccessToken&fields=data"
  val videoEdgeCommentsQuery = s"$graphApiBaseUrl/$videoId/comments?access_token=$userAccessToken&order=reverse_chronological"
  
  val copyrightVideoQuery = s"$graphApiBaseUrl/copyright_video_search?since=1499897678&until=1499984078&keyword=puppy&limit=10&access_token=$appToken&fields=$fieldsRequired"
  
  
  def getFacebookData =  Http(s"$copyrightVideoQuery").asString.body
  
  def getAccesstoken = Http(s"$graphApiBaseUrl/oauth/access_token?client_id=265982633890221&client_secret=b34b148d6dcfc299e1677874639ad5da").asString.body
  
  println(getFacebookData)
}
