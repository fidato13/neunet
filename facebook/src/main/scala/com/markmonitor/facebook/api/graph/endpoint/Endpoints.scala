package com.markmonitor.facebook.api.graph.endpoint

import io.finch.Endpoint
import io.circe.{Encoder, Json}
import io.finch._
import io.finch.circe._
import com.markmonitor.facebook.api.graph.services.Requests._

object Endpoints {

  val pathFacebook = "facebook"


  //  http://localhost:7071/facebook
  val facebookDummy: Endpoint[String] = get(pathFacebook) {
    // do something with this endpoint
    println("The facebook data => "+getFacebookData)

    Ok("")
  }

  // Endpoints
  val combined =  facebookDummy

  /**
    * sample Endpoint :
    * http://localhost:7070/raw/GBP/INR
    */

  // Convert domain errors to JSON
  implicit val encodeException: Encoder[Exception] = Encoder.instance { e =>
    Json.obj(
      "type" -> Json.fromString(e.getClass.getSimpleName),
      "error" -> Json.fromString(Option(e.getMessage).getOrElse("Internal Server Error"))
    )
  }

  /**
    * Greeting API
    */
  val facebookApi = combined.handle {
    case e: IllegalArgumentException =>
      BadRequest(e)
    case t: Throwable =>
      InternalServerError(new Exception(t.getCause))
  }.toServiceAs[Text.Plain]
  
}
