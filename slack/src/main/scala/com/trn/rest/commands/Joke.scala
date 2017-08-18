package com.trn.rest.commands

import scalaj.http.Http
import play.api.libs.json._


class Joke(text: String) extends Command {

  case class Value(id: Int , joke: String , categories: List[String])
  //case class Response(types: String, value: Value)

  implicit val residentReads = Json.reads[Value]

  val command = text

  override def action: String = {
    val jsonJoke: String = Http("http://api.icndb.com/jokes/random").asString.body.replace("\"type\": \"success\", ","").replace("{ \"value\":","").replace("] }","]")

    val jsvalueJoke = Json.parse(jsonJoke)

    val valueFromJson: JsResult[Value] = Json.fromJson[Value](jsvalueJoke)

    valueFromJson match {
      case JsSuccess(r: Value, path: JsPath) => s"Get ready to Laugh, HAHAHA! \n `${r.joke}`"
      case e: JsError => "No Joke for you today!!"
    }


  }

  override def response: String =  action
}