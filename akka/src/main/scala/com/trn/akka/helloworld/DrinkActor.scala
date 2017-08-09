package com.trn.akka.helloworld

import akka.actor.Actor
import akka.event.Logging

class DrinkActor extends Actor {

  val log = Logging(context.system,this)

  def receive = {
    case "tea" => log.info("Its tea time!")
    case "coffee" => log.info("Its coffee time!")
    case _ => log.info("Hmm...")
  }

}
