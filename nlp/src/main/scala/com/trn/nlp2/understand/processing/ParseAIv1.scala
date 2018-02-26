package com.trn.nlp2.understand.processing

import com.trn.nlp2.understand.model.{Hypothesis, Prediction, ProbableSeed}
import com.trn.nlp2.understand.observations.Observe._

object ParseAIv1 extends App {

  
  val optimisedHypothesisList = readHypothesisFromFile

  val listOfTemplateWords = List("stemplate", "vtemplate", "otemplate")
  
  //I was working on my table
  val sentenceToAnalyze = "I am sure about it" 
  // template require some keyword... in the case "she likes me" ... everyword is occupied .. so it won't detect any suitable template to match
  // also , i have till now restricted to one keyword right and one left.. for the case "You will be here", "Are you walking" ... where rightmost is the object, which wont be detected as we choose only "be" after will as the rightmost..this needs to be corrected
  
  // for the sentence, "Ram is cooking" ... there are several templates found... probably we can ignore those templates in which word "is" is at the beginning of the sentence ..??? need to be analysed.,..so as to what we can loose
  
  // Also filter out those templates which are greater in length than the length of the input sentence

  /**
    * WHen there is no common word found to be analysed/select a template then this method selects all those templates who have the same length as the length in the input
    * @param listHypothesis
    * @param inputSentence
    * @return
    */
  def findZeroIntersectionTemplates(listHypothesis: List[Hypothesis], inputSentence: String) = {
    val inputList = inputSentence.split(" ").toList
    val inputSize = inputList.size
    
    val sameSizeListHypothesis = listHypothesis.filter { _.listTemplateWords.length == inputSize}
    
    // now further filter out this list to have only those hypothesis which have one of the sentence keywords as either in their subject list, verb list or ibject list
   val shortenedList: List[Hypothesis] = sameSizeListHypothesis.filter { hypothesis =>
      hypothesis.seedSubject.intersect(inputList).size > 0 ||
        hypothesis.seedVerb.intersect(inputList).size > 0 ||
        hypothesis.seedObject.intersect(inputList).size > 0 
    }
    
    shortenedList.foreach(x => println(s"shortened List => $x"))
    
    val probableSubjectTuple: List[(String, Int)] = inputList.map{ word =>
      (word,shortenedList.filter{_.seedSubject.contains(word)}.size)
    }.sortWith(_._2 > _._2)
    
   val probableSubject = probableSubjectTuple.head._2 match {
      case 0 => ""// could not decide on the subject
      case _ => probableSubjectTuple.map(_._1).headOption match {
        case Some(x) => x
        case _ => ""
      }
    }

    val probableVerbTuple = inputList.map{ word =>
      (word,shortenedList.filter{_.seedVerb.contains(word)}.size)
    }.sortWith(_._2 > _._2)

    val probableVerb = probableVerbTuple.head._2 match {
      case 0 => ""// could not decide on the subject
      case _ => probableVerbTuple.map(_._1).headOption match {
        case Some(x) => x
        case _ => ""
      }
    }
    

    //println("probableVerb => "+probableVerb)

    val probableObjectTuple = inputList.map{ word =>
      (word,shortenedList.filter{_.seedObject.contains(word)}.size)
    }.sortWith(_._2 > _._2)

    val probableObject = probableObjectTuple.head._2 match {
      case 0 => ""// could not decide on the subject
      case _ => probableObjectTuple.map(_._1).headOption match {
        case Some(x) => x
        case _ => ""
      }
    }

    
    
    val prediction = Prediction(inputSentence,probableSubject,probableVerb,probableObject)
    println(prediction)
    
    prediction
  }
  
  /**
    * This will take the words of given sentence , see which word is there in the template and filters out those templates
    * Example: 'was' appears in "stemplate was vtemplate" .. so this template will be selected along with others
    */
  def probableTemplateBasedOnKeywords(listHypothesis: List[Hypothesis], inputSentence: String): List[Hypothesis] = {
    val inputWordList = inputSentence.split(" ").toList
    listHypothesis.flatMap{ hypothesis =>
      
      //find the intersection of inputWordList and hypothesisTemplateList ...if the size > 0 , then this template can be applied
      hypothesis.listTemplateWords.intersect(inputWordList).size match {
        case 0 => None
        case _ => Some(hypothesis)
      }
    } // if the flatmap returns blank..ie. could not find any matching common word template, then go for the zero word template
  }
  
  def isTemplateKeyWord(word: String): Boolean = {
    listOfTemplateWords.contains(word)
  }

  def analyseShortlistedTemplates(shortListOfHypothesis: List[Hypothesis], inputSentence: String) = {
    val inputWordList = inputSentence.split(" ").toList
    
   val combinedProabableSeedListFromAllHypothesis: List[ProbableSeed] = shortListOfHypothesis.flatMap { hypothesis => 
      // find the common words
      val commonWordsList: List[String] = hypothesis.listTemplateWords.intersect(inputWordList)
      
    val combinedProabableSeedListFromOneHypothesis: List[ProbableSeed] = commonWordsList.flatMap { commonWord =>
      //find the right nearest template word to this common word ... by creating a sublist first from the word to the right edge
      val subListRight = hypothesis.listTemplateWords.slice(hypothesis.listTemplateWords.indexOf(commonWord) + 1, hypothesis.listTemplateWords.size)
      val rightKeyWordList: List[(String, Int)] = subListRight.zipWithIndex.filter { case (word, index) =>
        isTemplateKeyWord(word)
      }

      val probableSeedsFromRight: List[ProbableSeed] = rightKeyWordList.flatMap { rightFirstKeyWord =>
        // now whatever this keyword is and its index would provide us suggestion , what value in the input list could have those meanings
        rightFirstKeyWord._2 match {
          case -1 => None // not found .. or it doesn't have any keyword to its right
          case x => rightFirstKeyWord._1 match { // it has a template word
            case "stemplate" => {
              val indexOfCommonWordInInput = inputWordList.indexOf(commonWord)
              val targetedIndex = x + 1 + indexOfCommonWordInInput // because x may be zero , indicating that template word is next to common word..so we need to add 1 to it
              inputWordList.slice(targetedIndex, inputWordList.size).headOption match {
                case Some(subject) => Some(ProbableSeed(hypothesis, subject, "", "")) // found some word and that is our probable subject
                case _ => None
              }
            }
            case "vtemplate" => {
              val indexOfCommonWordInInput = inputWordList.indexOf(commonWord)
              val targetedIndex = x + 1 + indexOfCommonWordInInput // because x may be zero , indicating that template word is next to common word..so we need to add 1 to it
              inputWordList.slice(targetedIndex, inputWordList.size).headOption match {
                case Some(verb) => Some(ProbableSeed(hypothesis, "", verb, "")) // found some word and that is our probable subject
                case _ => None
              }
            }
            case "otemplate" => {
              val indexOfCommonWordInInput = inputWordList.indexOf(commonWord)
              val targetedIndex = x + 1 + indexOfCommonWordInInput // because x may be zero , indicating that template word is next to common word..so we need to add 1 to it
              inputWordList.slice(targetedIndex, inputWordList.size).headOption match {
                case Some(objectIns) => Some(ProbableSeed(hypothesis, "", "", objectIns)) // found some word and that is our probable subject
                case _ => None
              }
            }
            case _ => None // something horrible gone wrong, but nonetheless it was not found
          }
        }
      }

      //find the left nearest template word to this common word ... by creating a sublist from 0 to its index
      // the index in the tuple is the index of the next appearing keyword with respect to the common word...
      // example : "my stemplate" .. here the index in the tuple would be 0 
      // "my first vtemplate" .. here index is 1... for left sublist the number would be same as described above, but should be interpreted in reverse..as this is going from right to left
      val subListLeft = hypothesis.listTemplateWords.slice(0, hypothesis.listTemplateWords.indexOf(commonWord)).reverse
      val leftKeyWordList: List[(String, Int)] = subListLeft.zipWithIndex.filter { case (word, index) =>
        isTemplateKeyWord(word)
      }

      val probableSeedsFromLeft: List[ProbableSeed] = leftKeyWordList.flatMap { leftFirstKeyWord =>
      leftFirstKeyWord._2 match {
        case -1 => None // not found .. or it doesn't have any keyword to its right
        case x => leftFirstKeyWord._1 match { // it has a template word
          case "stemplate" => {
            val indexOfCommonWordInInput = inputWordList.indexOf(commonWord)
            val targetedIndex = -x - 1 + indexOfCommonWordInInput // because x may be zero , indicating that template word is next to common word..so we need to add 1 to it
            inputWordList.slice(0, targetedIndex + 1).lastOption match {
              case Some(subject) => Some(ProbableSeed(hypothesis, subject, "", "")) // found some word and that is our probable subject
              case _ => None
            }
          }
          case "vtemplate" => {
            val indexOfCommonWordInInput = inputWordList.indexOf(commonWord)
            val targetedIndex = -x - 1 + indexOfCommonWordInInput // because x may be zero , indicating that template word is next to common word..so we need to add 1 to it
            inputWordList.slice(0, targetedIndex + 1).lastOption match {
              case Some(verb) => Some(ProbableSeed(hypothesis, "", verb, "")) // found some word and that is our probable subject
              case _ => None
            }
          }
          case "otemplate" => {
            val indexOfCommonWordInInput = inputWordList.indexOf(commonWord)
            val targetedIndex = -x - 1 + indexOfCommonWordInInput // because x may be zero , indicating that template word is next to common word..so we need to add 1 to it
            inputWordList.slice(0, targetedIndex + 1).lastOption match {
              case Some(objectIns) => Some(ProbableSeed(hypothesis, "", "", objectIns)) // found some word and that is our probable subject
              case _ => None
            }
          }
          case _ => None // something horrible gone wrong, but nonetheless it was not found
        }
      }
    }
        
        //gives a list of ProbableSeeds for this particular word
        List(probableSeedsFromRight,probableSeedsFromLeft).flatten
        
      }

     combinedProabableSeedListFromOneHypothesis
     
    }

    combinedProabableSeedListFromAllHypothesis.foreach(x => println(s"combinedProabableSeedListFromAllHypothesis => ${x.subject}"))
    combinedProabableSeedListFromAllHypothesis


  }
  
   
  
  def parseFlowManager = {
    
    val initialProbableTemplateList: List[Hypothesis] = probableTemplateBasedOnKeywords(optimisedHypothesisList,sentenceToAnalyze) // this will return an empty list for the case "she likes me"

    initialProbableTemplateList.size match {
      case 0 => findZeroIntersectionTemplates(optimisedHypothesisList,sentenceToAnalyze)
      case _ => analyseShortlistedTemplates(initialProbableTemplateList,sentenceToAnalyze)
    }
    
    //val combinedProbableSeedList = analyseShortlistedTemplates(initialProbableTemplateList,sentenceToAnalyze)
    
    initialProbableTemplateList.foreach(println)
    //combinedProbableSeedList.foreach(x => println(x.verb))
  
  }
  
  //kick-off the proceedings
  parseFlowManager
}
