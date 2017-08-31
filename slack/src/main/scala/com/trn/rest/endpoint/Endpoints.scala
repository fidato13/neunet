package com.trn.rest.endpoint


import com.trn.rest.commands.Command
import io.circe.{Encoder, Json}
import io.finch._
import io.finch.circe._
import com.trn.rest.services.CommandProcessing._
import com.trn.nlp.understand.verb.VerbList._
import com.trn.nlp.understand.Initialization._
import com.trn.solr.SolrMonitor._

object Endpoints {

  val pathSolr = "solr"
  val pathVerb = "verb"
  val pathRaw = "raw"
  val pathTransfer = "transfer"
  val pathINR = "INR"
  val pathGBP = "GBP"
  val pathEUR = "EUR"
  val pathCMD = "cmd"
  //val postedCommand: Endpoint[Command] = body.as[Command]

  val postCommandsFromSlack: Endpoint[String] =
    post(pathCMD :: param("text").as[String] :: param("user_name").as[String] :: param("team_id").as[String]) { (message: String, user: String, team: String) =>

      // process command
      val command: Command = buildCommand(message, user, team)
      val response = command.response


      postToSlack(s"Hey @$user!, $response")
      Ok("")

    }

  //  http://localhost:7070/solr
  val checkSolr: Endpoint[String] = get(pathSolr) {

    //chek if solr shards are up and running
    ifAllShardsAreUp match {
      case true => println("All shards are up!")//do nothing
      case false => postToSlackSolr("<!channel> Some of the Solr shards are down, you might wanna have a look!")// post to slack
    }

    Ok("")
  }

  // Endpoints
  val combined = postCommandsFromSlack :+: checkSolr

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
