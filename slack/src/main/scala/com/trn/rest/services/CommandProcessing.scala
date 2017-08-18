package com.trn.rest.services

import com.trn.rest.commands.{Command, Joke, RemoteScript, UnSupported}

import scalaj.http.{Http, HttpOptions}

object CommandProcessing {

  def postToSlackDummy(message: String) = {

    val jsonMessage = s"""{"text": "$message"}"""

    //post to slack
    Http("https://hooks.slack.com/services/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").postData(jsonMessage)
      .header("Content-Type", "application/text")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString
  }

  def supportedRemoteExecution(text: String): (Boolean, String) = text match {
      case "trn remote-execute command1" => (true, "command1")
      case "trn remote-execute command2" => (true, "command2")
      case "trn remote-execute command3" => (true, "command3")
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

  def buildCommand(text: String): Command = {
    // This will parse the text string and will create the appropriate command object
    val listText: List[String] = text.split(" ").filterNot(_ == "trn").map(_.toLowerCase).toList

    /**
      * find keyword based on the priority if there more than one keyword that is supported
      */
    val keyword = findKeyword(listText)

    keyword match {
      case "joke" => new Joke(text)
      case "remote-execute" => { // for this we will use the script name if matches
        val isSupported = supportedRemoteExecution(text)
        isSupported._1 match {
          case false => new UnSupported
          case true => new RemoteScript(isSupported._2)
        }
      }
      case _ => new UnSupported
    }


  }
}
