package com.trn.nlp.understand.tense

import com.trn.nlp.understand.sentence.Sentence
import com.trn.nlp.understand.verb.MatchedVerb


/**
  * First and foremost is identification of Verb and then tense and then subject/object may be
  * There are 12 tenses(easier to spot):
  * 1. Future Perfect Continuous: will have been + ingForm of verb
  * 2. Present Perfect Continuous : have/has been + ingForm of verb
  * 3. Past Perfect Continuous: had been + ingForm of verb
  * 4. Future Perfect: will have + thirdForm of verb
  * 5. Present Perfect: have/has + thirdForm of verb (in case our softawre gives secondform of verb and it contains have, then it surely is third form of verb as many time second and third form of verbs are same!)
  * 6. Past Perfect: had + thirdForm of verb
  * 7. Future Continuous: will/shall be + ingForm of verb
  * 8. Present Continuous: is/am/are + ingForm of verb
  * 9. Past Continuous: was/were + ingForm of verb
  * 10. Future Indefinite: subject + will/shall + firstForm of verb
  * 11. Present Indefinite: Subject + firstForm/sForm/esForm of verb
  * 12. Past Indefinite: subject + secondForm of verb
  *
  * Tenses would require the matchedVerb to be sent along as well
  */
case class Tense(tenseStr: String)

object Tense extends App {

  def isFuturePerfectContinuous(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    (sentence.raw.contains("will") ||
      sentence.raw.contains("shall") ||
      sentence.raw.contains("would") ||
      sentence.raw.contains("should")) &&
      (sentence.raw.contains("has") ||
        sentence.raw.contains("have")) &&
      sentence.raw.contains("been") &&
      matchedVerbList.filter(_.matchedForm == "ing").size > 0

  }

  def isPresentPerfectContinuous(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    (sentence.raw.contains("has") ||
      sentence.raw.contains("have")) &&
      sentence.raw.contains("been") &&
      matchedVerbList.filter(_.matchedForm == "ing").size > 0

  }

  def isPastPerfectContinuous(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    sentence.raw.contains("had") &&
      sentence.raw.contains("been") &&
      matchedVerbList.filter(_.matchedForm == "ing").size > 0

  }

  def isFuturePerfect(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    (sentence.raw.contains("will") ||
      sentence.raw.contains("shall") ||
      sentence.raw.contains("would") ||
      sentence.raw.contains("should")) &&
      sentence.raw.contains("have") &&
      (matchedVerbList.filter(_.matchedForm == "3").size > 0 ||
        matchedVerbList.filter(_.matchedForm == "2").size > 0)

  }

  /**
    * Almost in every case has/have will be adjacent to thirdForm of verb .. In future i would change the implementation of all these methods to look for has/have + thirdForm of verb
    *
    * @param sentence
    * @param matchedVerbList
    * @return
    */
  def isPresentPerfect(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    (sentence.raw.contains("has") ||
      sentence.raw.contains("have")) &&
      (matchedVerbList.filter(_.matchedForm == "3").size > 0 ||
        matchedVerbList.filter(_.matchedForm == "2").size > 0)

  }

  def isPastPerfect(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    sentence.raw.contains("had") &&
      (matchedVerbList.filter(_.matchedForm == "3").size > 0 ||
        matchedVerbList.filter(_.matchedForm == "2").size > 0)

  }

  def isFutureContinuous(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    (sentence.raw.contains("will") ||
      sentence.raw.contains("shall") ||
      sentence.raw.contains("would") ||
      sentence.raw.contains("should")) &&
      sentence.raw.contains("be") &&
      matchedVerbList.filter(_.matchedForm == "ing").size > 0

  }

  def isPresentContinuous(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    (sentence.raw.contains("is") ||
      sentence.raw.contains("am") ||
      sentence.raw.contains("are")) &&
      matchedVerbList.filter(_.matchedForm == "ing").size > 0

  }

  def isPastContinuous(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    (sentence.raw.contains("was") ||
      sentence.raw.contains("were")) &&
      matchedVerbList.filter(_.matchedForm == "ing").size > 0

  }

  def isFutureIndefinite(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    (sentence.raw.contains("will") ||
      sentence.raw.contains("shall")) &&
      matchedVerbList.filter(_.matchedForm == "1").size > 0

  }

  def isPastIndefinite(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    matchedVerbList.filter(_.matchedForm == "2").size > 0 ||
      sentence.raw.contains("was") ||
      sentence.raw.contains("were")

  }

  def isPresentIndefinite(sentence: Sentence, matchedVerbList: List[MatchedVerb]): Boolean = {
    (matchedVerbList.filter(_.matchedForm == "1").size > 0 ||
      matchedVerbList.filter(_.matchedForm == "ses").size > 0 ||
      sentence.raw.contains("is") ||
      sentence.raw.contains("am") ||
      sentence.raw.contains("are")
      )

  }

  def getTense(sentence: Sentence, matchedVerb: List[MatchedVerb]): Tense = {

    val listTensesFunc: List[(Sentence, List[MatchedVerb]) => Boolean] = List(isFuturePerfectContinuous _,
      isPresentPerfectContinuous _,
      isPastPerfectContinuous _,
      isFuturePerfect _,
      isPresentPerfect _,
      isPastPerfect _,
      isFutureContinuous _,
      isPresentContinuous _,
      isPastContinuous _,
      isFutureIndefinite _,
      isPastIndefinite _,
      isPresentIndefinite _
    )

    /**
      * The order of calling these tenses is extremely important! as a sentence can only have one tense wo would have to call these method from top to down as declared in this file.
      */

    val tenseIndex: Int = listTensesFunc.map(_ (sentence, matchedVerb))
      .indexWhere(_ == true)

    tenseIndex match {
      case 0 => Tense("FuturePerfectContinuous")
      case 1 => Tense("PresentPerfectContinuous")
      case 2 => Tense("PastPerfectContinuous")
      case 3 => Tense("FuturePerfect")
      case 4 => Tense("PresentPerfect")
      case 5 => Tense("PastPerfect")
      case 6 => Tense("FutureContinuous")
      case 7 => Tense("PresentContinuous")
      case 8 => Tense("PastContinuous")
      case 9 => Tense("FutureIndefinite")
      case 10 => Tense("PastIndefinite")
      case 11 => Tense("PresentIndefinite")
      case -1 => Tense("Unable to decide!")
    }

  }
}