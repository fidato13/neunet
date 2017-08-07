package com.trn.recepies.scala

import scala.io.Source

object ReadFile extends App {

  val bufferedSource = Source.fromFile("/absolutePath/filename")
  for (line <- bufferedSource.getLines) {
    println(line.toUpperCase)
  }

  bufferedSource.close
}