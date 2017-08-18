package com.trn.rest

import com.typesafe.config.ConfigFactory
import com.twitter.finagle.Http
import io.finch._
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.{Await, Timer}
import com.trn.rest.endpoint.Endpoints._
import io.circe.Json

object Main extends App {


  /**
    * Application configuration object.
    */
  final val config = ConfigFactory.load()


  val port = config.getInt("app.port")
  Await.ready(Http.server.serve(s":$port", slackApi))

}
