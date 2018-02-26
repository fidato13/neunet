package com.trn.nlp2.understand.util

import java.io.{File, FileWriter, PrintWriter}

import scala.io.Source

object ExternalFile extends App {

  def writeToFile(text: String, flag: Boolean) = {
    val writer = new FileWriter("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/passedInputs",flag)
    writer.write(s"$text // subject: , verb: , object: ")
    writer.write("\n")
    writer.close()
  }
  
  def removeDuplicateFromPassedInputsFile = {
    val bufferedSource = Source.fromFile("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/passedInputs")

    /**
      * Read all the lines and populate them in a list
      */
    val setLines: Set[String] = bufferedSource.getLines().map(_.toLowerCase).toSet
    
    bufferedSource.close()

    val writer = new FileWriter("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/passedInputs",false)
    setLines.foreach { line =>
      writer.write(line)
      writer.write("\n")
    }
    writer.close()
    
    
  }
  
  def writeToPredictionReviewFile(text: String, subject: String, verb: String, objectIns: String) = {
    val writer = new FileWriter("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/predictionreview",true)
    writer.write(s"$text // subject: $subject, verb: $verb, object: $objectIns")
    writer.write("\n")
    writer.close()
    
  }
  
  //occasionally run this class to remove duplicates from file
  removeDuplicateFromPassedInputsFile
}
