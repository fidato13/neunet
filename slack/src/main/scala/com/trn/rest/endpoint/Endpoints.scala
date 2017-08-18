package com.trn.rest.endpoint

import com.trn.rest.commands.Command
import io.circe.{Encoder, Json}
import io.finch._
import io.finch.circe._
import com.trn.rest.services.CommandProcessing._

object Endpoints {

  val pathRaw = "raw"
  val pathTransfer = "transfer"
  val pathINR = "INR"
  val pathGBP = "GBP"
  val pathEUR = "EUR"
  val pathCMD = "cmd"
  //val postedCommand: Endpoint[Command] = body.as[Command]

  val postCommandsFromSlack:Endpoint[String] = post(pathCMD :: param("text").as[String] :: param("user_name").as[String]) { (message: String, user: String) =>

    // process command
    val command: Command = buildCommand(message)
    val response = command.response


    postToSlackDummy(s"Hey @$user!, $response")
    Ok("")

  }

  // Endpoints
  val combined = postCommandsFromSlack //:+: getINRTransfer

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
  val slackApi = combined.handle {
    case e: IllegalArgumentException =>
      BadRequest(e)
    case t: Throwable =>
      InternalServerError(new Exception(t.getCause))
  }.toServiceAs[Text.Plain]

}
