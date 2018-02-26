package com.trn.nlp.understand

import com.trn.nlp.understand.sentence.Sentence
import com.trn.nlp.understand.verb.Verb

import scala.io.Source

/**
  * This object will be used to initialize all the heavy work like reading from verbs, etc files
  */
object Initialization {

  implicit val verbList: List[Verb] = initializeVerbList

  def initializeVerbList: List[Verb] = {
    println("Reading resource file for verbs")
    // The absolute path so that when writing to this file, only a single file is modified and not tht local file packaged with Jar
    val bufferedSource = Source.fromFile("/trn/git/gitlab/currying/nlp/src/main/resources/understand/verb/verbs.csv")

    /**
      * Read all the verbs and populate them in a list
      */
    bufferedSource.getLines().map(_.split(",")).map(x => Verb(x(0), x(1), x(2), x(3), x(4))).toList


  }


}
