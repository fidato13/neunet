package com.trn.nlp2.understand

import com.trn.nlp2.understand.model.{DatasetRecord, InputRecord}

import scala.io.Source

object Initialization  {

  lazy val buildDataset: List[DatasetRecord] = {

    val bufferedSource = Source.fromFile("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/raw/sentences")

    /**
      * Read all the lines and populate them in a list
      */
    val lines: Iterator[String] = bufferedSource.getLines().map(_.toLowerCase).map(replacements(_))

    val listRecords = lines.map{ line =>
      //println(s"line is => $line")
      val lineElementsArray = line.split("//")
      val rawRecord: String = lineElementsArray(0).replace(",","") // sentence
      //lineElementsArray.foreach(x =>s" the  lineElementsArray values array is $x")
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

      DatasetRecord(rawRecord,rawRecord.split(" ").toList,subject,verb,objectIns)
    }.toList

    listRecords
  }
  
  lazy val buildNoun: List[String] = {
    val bufferedSource = Source.fromFile("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/lists/noun")
    val line = bufferedSource.getLines().map(_.toLowerCase.trim).toList(0)
    line.split(",").toList
    
  }

  lazy val buildPronoun: List[String] = {
    val bufferedSource = Source.fromFile("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/lists/pronoun")
    val line = bufferedSource.getLines().map(_.toLowerCase.trim).toList(0)
    line.split(",").toList

  }
  
  def replacements(input: String) = {
    input
        .toLowerCase
      //.replace(",","")
      .replace("\\.","")
      .replace("!","")
      .replace("?","")
      .replace("'ll"," will")
      .replace("that's","that is")
      .replace("let's","let us")
      .replace("n't"," not")
        .replace("?", "")
      .replace("'ve", " have")
        .split(" ")
        .filter(_.length > 0)
      .map(_.trim)
      .mkString(" ") + " " //for the end of the sentence, as the trim is removing all the spaces from the end, if the end is not having spaces then it complains.
      
  }
  
  def userInputSimulation: InputRecord = {
    //The tall, dark stranger was singing
    //She is waving an American flag
    // I love you fails for interrogative sentences,... because of interrogative sentences ...It is shown as subject... so might need to treat interrogative sentences differently..or else provide more of the input
    val sentence = replacements("His blue eyes were colder than the sky on a winter morning in Virginia")
    InputRecord(sentence, sentence.split(" ").toList, List.empty[String],List.empty[String],List.empty[String])
  }
  
  // call parse with both the above inputs for processing of main algo
  
  

}
