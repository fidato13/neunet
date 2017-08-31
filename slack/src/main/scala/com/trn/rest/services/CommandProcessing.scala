package com.trn.rest.services

import com.trn.rest.commands._

import scalaj.http.{Http, HttpOptions}

object CommandProcessing {

  def postToSlack(message: String) = {

    val jsonMessage = s"""{"text": "$message"}"""

    //post to slack
    // trn-dev https://hooks.slack.com/services/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
    //trn-test https://hooks.slack.com/services/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
    Http("https://hooks.slack.com/services/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").postData(jsonMessage)
      .header("Content-Type", "application/text")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString
  }

  def postToSlackSolr(message: String) = {

    val jsonMessage = s"""{"text": "$message"}"""

    //post to slack
    // trn-dev https://hooks.slack.com/services/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
    //trn-test https://hooks.slack.com/services/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
    Http("https://hooks.slack.com/services/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").postData(jsonMessage)
      .header("Content-Type", "application/text")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString
  }

  def isAuthoizedUser(user: String, team: String): Boolean = {
    (user,team) match {
      case ("tarunk","T043G0AGW") => true
      case ("luca.zanconato","T043G0AGW") => true
      case ("pawel","T043G0AGW") => true
      case _ => false
    }
  }

  def supportedRemoteExecution(text: String): (Boolean, String) = text match {
      case "trn remote-execute restart-hbase-indexer-master1" => (true, "restart-hbase-indexer-master1")
      case "trn remote-execute restart-listener-normal-master1" => (true, "restart-listener-normal-master1")
      case "trn remote-execute restart-listener-realtime-master1" => (true, "restart-listener-realtime-master1")
      case "trn remote-execute restart-hbase-indexer-master2" => (true, "restart-hbase-indexer-master2")
      case "trn remote-execute restart-listener-normal-master2" => (true, "restart-listener-normal-master2")
      case "trn remote-execute restart-listener-realtime-master2" => (true, "restart-listener-realtime-master2")
      case "trn remote-execute restart-screen0-sandbox" => (true, "restart-screen0-sandbox")
      case "trn remote-execute restart-screen1-sandbox" => (true, "restart-screen1-sandbox")
      case "trn remote-execute restart-screen2-sandbox" => (true, "restart-screen2-sandbox")
      case _ => (false, "")
    }


  /**
    * find keyword based on the priority if there more than one keyword that is supported
    */
  def findKeyword(listWords: List[String]): String = {
    val recognizedKeyWords = List[String]("remote-execute", "joke")

    val filteredList = listWords.filter(recognizedKeyWords.contains(_))

    val keyword: Option[String] = recognizedKeyWords.find(filteredList.contains(_))

    keyword match {
      case Some(x) => x
      case None => "UnSupported!"
    }
  }

  def buildCommand(text: String, user: String, team: String): Command = {
    // This will parse the text string and will create the appropriate command object
    val listText: List[String] = text.split(" ").filterNot(_ == "trn").map(_.toLowerCase).toList

    /**
      * find keyword based on the priority if there more than one keyword that is supported
      */
    val keyword = findKeyword(listText)

    keyword match {
      case "joke" => new Joke(text)
      case "remote-execute" => { // for this we will use the script name if matches
        if (isAuthoizedUser(user, team)) {
          val isSupported: (Boolean, String) = supportedRemoteExecution(text)
          isSupported._1 match {
            case false => new UnSupported
            case true => new RemoteScript(isSupported._2)
          }
        }
        else {
          new UnAuthorized
        }
      }
      case _ => new UnSupported
    }


  }
}
