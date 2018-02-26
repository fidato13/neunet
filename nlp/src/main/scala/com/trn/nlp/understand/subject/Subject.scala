package com.trn.nlp.understand.subject

import com.trn.nlp.understand.sentence.Sentence
import com.trn.nlp.understand.tense.Tense
import com.trn.nlp.understand.verb.MatchedVerb

/**
  * https://www.thoughtco.com/find-the-subject-of-a-sentence-1691013
  * http://www.worldclasslearning.com/english/parts-of-speech.html
  * http://www.worldclasslearning.com/english/pronoun.html
  * http://writingexplained.org/grammar-dictionary/interrogative-sentence
  *
  * A sentence must have a subject and verb , but may or may not have an object!
  * The subject is commonly a noun, pronoun, or noun phrase
  *
  *
  * => Subject usually is in the first half of the sentence ie before action or predicate
  * => Subject can be composed of more than one word like
  * "The Johnsons" have returned ,
  * "Dead men" tell no tales,
  * "We" will try,
  * "Time" flies,
  *
  * => Subject usually comes just before the verb or the tense :
  * "The teachers" are tired
  * "Dead men" tell no tales
  *
  * => Subject comes as next word to tense, if there is an Interrogative sentence
  * Will "you" play with me?
  *
  * => Two (or more) nouns, pronouns, or noun phrases may be linked by and to make a compound subject.
  * Winnie and her sister will sing at the recital this evening.
  * In the above example, the compound subject is Winnie and her sister:
  *
  * => Nouns can be :
  * Common Nouns: City, table, television, oven, sea, continent, camera, house boat.
  * Proper Nouns: Australia, Harry Potter, Jog Falls, Pacific Ocean, Tajmahal.
  * Collective Nouns: Audience, pack, deck, cluster.
  * Abstract Nouns: Determination, proposal, happiness, failure, victory.
  * Material Noun: Nylon, polyester, jute, silk, wood.
  *
  * => Pronouns can be :
  * 1. Personal Pronouns : It can have three sub categories:
  * a) Subjective Case Personal Pronouns (applicable for subject, Usuallu subject is one of these words) :  I, you, he, she, it, we, they
  * b) Objective Case Personal Pronouns(applicable for objects only): me, you , him ,her, it, us, them
  * c) Possessive Case Personal Pronouns(These pronouns combine with the following thing noun to form a subject, like 'my watch'): my, mine , your, yours, his, her, hers, its, our, ours,their, theirs
  *
  * 2. Reflexive Pronouns : They can only be objects and never subjects => myself, yourself, himself, herself, itself, ourselves, yourselves, themselves
  *
  * 3. Demonstrative Pronouns : It refer to things in relation to number and distance. => This , That , These , Those
  *
  * 4. Indefinite Pronouns : It can refer to unspecified people, places or things. => another, anyone, no one, anybody , anything, nobody, everyone, everybody, one, each, either, somebody, something, both, few, many, all , most, none, neither
  *
  * 5. Interrogative Pronouns: These are pronouns that begin questions : who, whom, whose, which and what
  *
  * 6. Relative Pronouns are that, which, who and whom - which relate to another noun that precedes it in the sentence.
  *
  *
  *
  * => So the strategy could be to spot the verb and the tense ... and then from the beginning of the sentence(If that is not an interrogative sentence) to the tense followed by the word could be your subject
  * like "she will sing" .. "THe first person in line will shout" etc ...
  *
  * => Identifying subject in Interrogative sentences
  * http://writingexplained.org/grammar-dictionary/interrogative-sentence
  *
  * => Sentences starting with THE always have the subject right there!
  **/
case class Subject(word: String)

object Subject {
  // For subject Identification, it requires First verb to be known, then it requires tense to be known as well..
  // this alsp requires the raw sentence from which it would find the subject
  // we will first implement covering only the pronoun...in future we will implement support for Nouns

  def getTextTillFirstVerb(sentence: Sentence, matchedVerb: MatchedVerb) = {

    sentence.raw
      .split(" ")
      .slice(0, matchedVerb.matchedIndex + 1)
      .mkString(" ") // might throw an error if the verb is at first
  }

  def beginsWithThe(text: String) = {

  }

  //combining all pronouns here
  def isPronoun(text: String): (Boolean, String) = {
    val listPersonalSubjectivePronouns = List("i", "you", "he", "she", "it", "we", "they")
    val listDemonstrativePronouns = List("this", "that", "these", "those")
    val listIndefinitePronouns = List("another", "anyone", "anybody", "anything", "nobody", "everyone", "everybody", "one", "each", "either", "somebody", "something", "both", "few", "many", "all", "most", "none", "neither")
    val listInterrogativePronouns = List("who", "whom", "whose", "which", "what")
    val listConcatPronouns: List[String] = listPersonalSubjectivePronouns ++ listDemonstrativePronouns ++ listIndefinitePronouns ++ listInterrogativePronouns

    println(s"List Concat => $listConcatPronouns")
    val listWords = text.split(" ").toList // last word in this list is always the verb so we can safely remove that from consideration
    listWords.find {
      listConcatPronouns.contains
    } match {
      case Some(x) => (true, x)
      case _ => {
        val listPersonalPossessivePronouns = List("my", "mine", "your", "yours", "his", "her", "hers", "its", "our", "ours", "their", "theirs") // this requires a special case as this will contain the word following this word as well
        listWords.find {
          listPersonalPossessivePronouns.contains
        } match {
          case Some(x) => {
            //first find index of the word in text
            val index: Int = listWords.indexWhere(_ == x) + 1
            val word: String = listWords.slice(index - 1, index + 1).mkString(" ")
            (true, word)
          }
          case _ => (false, "") // it was not in any list , so there might be a Noun
        }
      }
    }
  }

  def getSubject(sentence: Sentence, matchedVerbList: List[MatchedVerb], tense: Tense): Subject = {
    //  pick the first verb, if the list is empty consider the whole snetence
    val partialText = matchedVerbList.size match {
      case 0 => sentence.raw // no verb was found hence the complete sentence
      case _ => getTextTillFirstVerb(sentence, matchedVerbList(0)) // first verb
    }

    println(s"partialText => $partialText")

    val pronounSubject = isPronoun(partialText)
    pronounSubject._1 match {
      case true => Subject(pronounSubject._2)
      case false => Subject("No Subject Found!")
    }
  }

}
