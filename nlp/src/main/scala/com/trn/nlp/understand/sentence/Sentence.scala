package com.trn.nlp.understand.sentence

import com.trn.nlp.understand.verb.VerbList._
import com.trn.nlp.understand.Initialization._
import com.trn.nlp.understand.verb.MatchedVerb
import com.trn.nlp.understand.tense.Tense._
import com.trn.nlp.understand.subject.Subject._

/**
  * This contains the raw sentence. A sentence in our case would be delimited by a fullStop(.), which would be converted to Arrar/List[String] and would be sent for Verb processing
  * => So would be required to remove commas from the sentence
  *
  */

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

case class Sentence(val raw: String, interrogative: Boolean)

/**
  * At this moment , we will only focus on one sentence. So if multiple sentences are sent for processing , it will pick the first sentence. So better only
  * send a sentence. Later, we will look at combining sentences
  */
object Sentence extends App{

  /**
    * Because identifying subject in interrogative sentence requires extra processing
    */
  val interrogativeWords = List("do","did","does","how","when","where","what","which", "who", "why")

  def replacements(input: String) = {
    input
      .replace(",","")
      .replace("!","")
      .replace("?","")
      .replace("i'll","i will")
      .replace("it's","it is")
  }

  def getSentence(input: String): Sentence = {
    val text = replacements(input.split("\\.")(0).toLowerCase)
    val interrogative = isInterrogative(text)
    Sentence(text,interrogative) //pick first sentence of the complete string and remove "," and send this sentence as raw sentence
  }

  def isInterrogative(text: String): Boolean = {
    val firstWord = text.split(" ")(0)
    interrogativeWords.contains(firstWord)
  }

  val message = "The detective pulled down the brim of his hat and walked home."

  val sentenceObject: Sentence = getSentence(message)

  println(s"sentenceObject => $sentenceObject")

  val verbList: List[MatchedVerb] = findVerb(sentenceObject)(initializeVerbList)

  val tenseObbject = getTense(sentenceObject,verbList)

  val SubjectObject = getSubject(sentenceObject,verbList,tenseObbject)

  println(s"the word is => $verbList")
  println(s"The sentence was => $sentenceObject")
  println(s"The tenseObbject was => $tenseObbject")
  println(s"The SubjectObject was => $SubjectObject")
}