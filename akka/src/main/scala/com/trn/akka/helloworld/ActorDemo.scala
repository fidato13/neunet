package com.trn.akka.helloworld

import akka.actor.{ActorSystem, Props}

object ActorDemo {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("drink-system")
    val props = Props[DrinkActor]
    val drinkActor = system.actorOf(props,"drinkActor-1")

    //messages
    drinkActor ! "coffee"
    drinkActor ! "coffee"
    drinkActor ! "tea"
    drinkActor ! "water"

    system.terminate()
  }
}
