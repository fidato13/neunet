package com.markmonitor.facebook.api.graph

import com.twitter.finagle.Http
import com.twitter.util.Await
import com.typesafe.config.ConfigFactory
import com.markmonitor.facebook.api.graph.endpoint.Endpoints._

object Main extends App {


  /**
    * Application configuration object.
    */
  final val config = ConfigFactory.load()


  val port = config.getInt("app.port")
  Await.ready(Http.server.serve(s":$port", facebookApi))

}
