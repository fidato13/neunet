package com.trn.nlp.understand.verb

import scala.util.Try
import com.trn.nlp.understand.Initialization._
import com.trn.nlp.understand.sentence.Sentence

/**
  * => There are some ambiguous words like have,had, which appears in verbs as well as play a role in deciding tenses. So we need to exclude them from being counted in Verb, if required in complex sentences we can write a method which includes these words if there is no verb found.
  * => IF there are multiple verbs that would probably can also mean that one of them is ambiguous and other is really an action verb, so treat the ambiguous verb as any other word and
  * => mor if there are multiple words then in some cases it could form a single verb too...like "going to make" in the sentence "I am going to make some tea"
  * => If there are multiple verbs then decide ambiguous verb..most likely since the subject is at the beginning/first part of the sentence...so the first verb in the list would have the subject before it
  * => If the verb is blank then it may mean that it is a universal statement like "Everything in Java is within classes and objects." ... In this case we can reply like "Hmm, I know :p"
  *
  * @param firstForm
  * @param secondForm
  * @param thirdForm
  * @param ingForm
  * @param sForm
  */
case class Verb(val firstForm: String, val secondForm: String, val thirdForm: String, val sForm: String, val ingForm: String) {
  override def toString: String = {
    s"""
       | First Form is $firstForm
       | Second Form is $secondForm
       | Third Form is $thirdForm
       | sForm is $sForm
       | Ing Form is $ingForm
       |
     """.stripMargin.trim
  }
}

/**
  *
  * @param verb
  * @param matchedForm it can have values 1,2,3,ses,ing
  * @param matchedWord
  * @param matchedIndex is the index of the verb in the raw sentence
  */
case class MatchedVerb(verb: Verb, matchedForm: String, matchedWord: String, matchedIndex: Int) {
  override def toString: String = s" $verb \n matchedForm is $matchedForm \n matchedWord is $matchedWord \n matchedIndex is $matchedIndex \n\n"
}

object VerbList extends App {

  /**
    * Gives back an option of Verb, the verb decided by this method can be overridden as some verbs can be the same for secondForm and thirdForm or else in all three forms...then in that case it would be decided based on the tense.
    * This method accepts a sentence and gives back an option of MatchedVerb
    *
    * @param sentence
    * @param verbList
    * @return
    */
  def findVerb(sentence: Sentence)(implicit verbList: List[Verb]): List[MatchedVerb] = {

    val listWords: List[String] = sentence.raw.split(" ").toList // split a sentence on spaces

    // pass every word of sentence to check if that is a verb
    val matchedVerbList: List[MatchedVerb] = listWords.flatMap { word =>

      val matchWord = Try {
        word.toLowerCase
      }.getOrElse("")

      val optionVerb: Option[Verb] = verbList.find { p =>
        p match {
          case Verb(`matchWord`, _, _, _, _) => true
          case Verb(_, `matchWord`, _, _, _) => true
          case Verb(_, _, `matchWord`, _, _) => true
          case Verb(_, _, _, `matchWord`, _) => true
          case Verb(_, _, _, _, `matchWord`) => true
          case _ => false
        }
      }

      optionVerb match {
        case Some(x) if (x.firstForm == `matchWord`) => Some(MatchedVerb(x, "1" ,`matchWord`, listWords.indexOf(`matchWord`)))
        case Some(x) if (x.secondForm == `matchWord`) => Some(MatchedVerb(x,  "2" , `matchWord`, listWords.indexOf(`matchWord`)))
        case Some(x) if (x.thirdForm == `matchWord`) => Some(MatchedVerb(x, "3" , `matchWord`, listWords.indexOf(`matchWord`)))
        case Some(x) if (x.sForm == `matchWord`) => Some(MatchedVerb(x, "ses" , `matchWord`, listWords.indexOf(`matchWord`)))
        case Some(x) if (x.ingForm == `matchWord`) => Some(MatchedVerb(x, "ing" , `matchWord`, listWords.indexOf(`matchWord`)))
        case _ => None //not found in our list
      }

    }

    matchedVerbList

  } // end of method findVerb

  /**
    * European judges could continue ruling over the UK after Brexit under one plan being drawn up for a transition period
    * How are you doing
    * Trump paints himself as the real victim of Charlottesville in angry speech
    * You know where my heart is
    */

  val sentence = "A source close to the Labour leader said any decision on whether to push for a vote would depend on"
  //findVerb(sentence).foreach(x => println(s"$x"))
}