/*
package com.trn.nlp2.main

import scala.io.Source

case class Record(sentence: String, subject: List[String], verb: List[String], objectIns: List[String])


object Main extends App{
  
  def buildDataset: List[Record] = {

    val bufferedSource = Source.fromFile("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/raw/sentences")

    /**
      * Read all the lines and populate them in a list
      */
    val lines = bufferedSource.getLines()

    val listRecords = lines.map{ line =>
      val lineElementsArray = line.split("//")
      val rawRecord = lineElementsArray(0) // sentence
      lineElementsArray.foreach(x =>s" the  lineElementsArray values array is $x")
      val propertiesArray = lineElementsArray(1).split(",")
      val subjectArray = propertiesArray(0).split(":")(1).trim // each array may or may not have a delimiter
    val verbArray = propertiesArray(1).split(":")(1).trim
      val objectArray = propertiesArray(2).split(":")(1).trim

      val subject: List[String] = subjectArray.contains("|") match {
        case true => subjectArray.split("\\|").toList
        case false => List(subjectArray)
      }

      val verb: List[String] = verbArray.contains("|") match {
        case true => verbArray.split("\\|").toList
        case false => List(verbArray)
      }

      val objectIns: List[String] = objectArray.contains("|") match {
        case true => objectArray.split("\\|").toList
        case false => List(objectArray)
      }

      Record(rawRecord,subject,verb,objectIns)
    }.toList

    listRecords
  }
 
  // Once the dataset is built then
}
*/
