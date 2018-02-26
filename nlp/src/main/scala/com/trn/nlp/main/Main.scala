package com.trn.nlp.main

import scala.io.Source

object Main extends App {

  /**
    * http://www.worldclasslearning.com/english/sentences.html
    *
    * A Sentence is a group of words that makes a complete sense.
    * A sentence must have a subject and a verb, but it may or may not have an object.
    *
    * For Example:-
    * a) He is reading
    * In this sentence He = Subject & is reading = verb
    *
    * Four Kinds of Sentences :-
    * 1) Declarative / Assertive Sentence => A declarative sentence makes a statement.
    * 2) Interrogative Sentence => An interrogative sentence asks a question.
    * 3) Exclamatory Sentence => An exclamatory sentence makes a very strong called an exclamation. It shows a strong feeling sucha as surprise or anger.
    * 4) Imperative Sentence => An imperative sentence gives an order.
    *
    * The Subject & The Object
    * The Subject of a sentence sometimes does something to someone or something else.
    * The person or thing that receives the action is called the object
    */

  case class Verb(val sno: Int, val firstForm: String, val secondForm: String, val thirdForm: String,  val sForm: String, val ingForm: String){
    override def toString: String = {
      s"$firstForm,$secondForm,$thirdForm,$sForm,$ingForm"
    }
  }


  // The absolute path so that when writing to this file, only a single file is modified and not tht local file packaged with Jar
  val bufferedSource = Source.fromFile("/trn/git/gitlab/currying/nlp/src/main/resources/understand/verb/verbs1000.csv")

  /**
    * Read all the verbs and populate them in a list
    */
  implicit val verbList: Seq[Verb] = bufferedSource.getLines().map(_.split(",")).map(x => Verb(x(0).toInt, x(1), x(2), x(3), x(4), x(5))).toList


  verbList.foreach(println)

  /**
    * The flow of execution goes like this:
    * First Create an object of Sentence by calling Sentence.getSentence(text) ... then find the appropriate verbs from this Sentence by calling findVerb(sentence)
    * Also track the verb position in the sentence
    * identify tense
    * identify subject
    */


}
