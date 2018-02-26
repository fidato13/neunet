package com.trn.nlp2.understand.processing

import com.trn.nlp2.understand.Initialization._
import com.trn.nlp2.understand.model._
import com.trn.nlp2.understand.util.ExternalFile._
import com.trn.nlp2.understand.processing.Rules._


object ParseNaive extends App {

  def sampleSimilarRecords = {
    /**
      * Find those 'Similar' Records who have the same order of occurence of/as matched words both in Dataset and user Input records
      * Few scores/ratings that we would be considering for every similar record:
      * matchedWordScrore => depends upon no of matched Words
      * RelativeorderScore => Relative order of matched word is similar to userInput word 
      */
  }
  
  def calculateSubjectChance(listSimilar: List[Similar]): (Double, Int) = {
    val totalElements = listSimilar.size
    //now find how many times the word was present in the DataSetRecord.subject
    val filteredListSubject = listSimilar.filter { similar =>
      similar.datasetRecord.subject.contains(similar.commonWord)
    }.size
    (filteredListSubject.toDouble / totalElements * 100, filteredListSubject)
  }

  def calculateVerbChance(listSimilar: List[Similar]): (Double, Int) = {
    val totalElements = listSimilar.size
    //now find how many times the word was present in the DataSetRecord.subject
    val filteredListVerb = listSimilar.filter { similar =>
      val contains = similar.datasetRecord.verb.contains(similar.commonWord)
      contains
    }.size
    (filteredListVerb.toDouble / totalElements * 100, filteredListVerb)
    
  }

  def calculateObjectChance(listSimilar: List[Similar]): (Double, Int) = {
    val totalElements = listSimilar.size
    //now find how many times the word was present in the DataSetRecord.subject
    val filteredListObject = listSimilar.filter { similar =>
      similar.datasetRecord.objectIns.contains(similar.commonWord)
    }.size
    (filteredListObject.toDouble / totalElements * 100, filteredListObject)
  }
  
  def groupSimilar(listSimilar: List[Similar]): List[Chances] = {
    val groupedSimilar: Map[Int, List[Similar]] = listSimilar.groupBy(_.indexOfWord)
    
   val chancesPerWord: List[Chances] = groupedSimilar.map { case (index,listSimilar) => 
     val subjectChance = calculateSubjectChance(listSimilar)
      val verbChance = calculateVerbChance(listSimilar)
      val objectChance = calculateObjectChance(listSimilar)
        
        Chances(listSimilar.head.commonWord, subjectChance,verbChance,objectChance)
    }.toList

    chancesPerWord
    
  }
  
  def processChances(listChances: List[Chances], inputSentence: String): Prediction = {
    //sort the chances, first for subject
    //might need to reverse the sorting order ..ie first no of votes and then percentage
    val sortedSubjectList = listChances
      .groupBy(_.chanceSubject._1)
      .map{_._2.sortWith(_.chanceSubject._2 > _.chanceSubject._2).head}
      .toList
      .sortWith(_.chanceSubject._1 > _.chanceSubject._1)

    val sortedVerbList =  listChances
      .groupBy(_.chanceVerb._1)
      .map{_._2.sortWith(_.chanceVerb._2 > _.chanceVerb._2).head}
      .toList
      .sortWith(_.chanceVerb._1 > _.chanceVerb._1)

    val sortedObjectList =  listChances
      .groupBy(_.chanceObject._1)
      .map{_._2.sortWith(_.chanceObject._2 > _.chanceObject._2).head}
      .toList
      .sortWith(_.chanceObject._1 > _.chanceObject._1)
    
    //filter sorted lists to only retain non zero percentage 
    val nonZeroSortedSubjectList = sortedSubjectList.filter( chance => chance.chanceSubject._1 > 0.0)
    val nonZeroSortedVerbList = sortedVerbList.filter( chance => chance.chanceVerb._1 > 0.0)
    val nonZeroSortedObjectList = sortedObjectList.filter( chance => chance.chanceObject._1 > 0.0)

    val derivedSubject = nonZeroSortedSubjectList.size match {
      case 0 => ""
      case _ => nonZeroSortedSubjectList.head.word
    }

    val derivedVerb = nonZeroSortedVerbList.size match {
      case 0 => ""
      case _ => nonZeroSortedVerbList.head.word
    }

    val derivedObject = nonZeroSortedObjectList.size match {
      case 0 => ""
      case _ => nonZeroSortedObjectList.head.word
    }

    Prediction(inputSentence,derivedSubject,derivedVerb,derivedObject)
  }
  
  def persistPrediction(prediction: Prediction) = {
    writeToPredictionReviewFile(prediction.inputText,prediction.subject,prediction.verb,prediction.objectIns)
  }

  def extractSuitableRecordListFromDataset(completeDataset: List[DatasetRecord], userInput: InputRecord): List[Similar] = {
    /**
      * take complete dataset and find relevant records(having common words between user supplied input and the dataset records so that the dataset record can be used as a template) as per the user input,... 
      * with more common words .. we will assign our confidence rating to each stage so that we can lookup other sources when rating is low
      */

    /**
      * Strategy 01: Take the input sentence, for every word of it , find dataset records which have the same position of the same word.
      * Then group this list by words...so every word in input corresponds to a list...
      * Then find out how many times that word was each a subject, verb or object.. then thats it's probability of being that in the sentence.
      * Conflicts will occur when we have multiple candidates for any position
      * for conflicts lets see who has max votes
      * This strategy would fail for compound verbs
      * process/gather multiple words ... like "do not know"
      * 
      * while declaring chances we can also verify the same word in their respective file to gain additional confidence
      * 
      * 
      * "We end meeting" .. when we have taught program that "meeting" is object here, so then it should automatically infer for the sentence "we end jumping" 
      * that "jumping" is object , without having to being explicitly teaching it that jumping at 3rd position is an object
      * 
      * so If we have more than half of the words found in some other sentence then we could take the other approach of seeing whats in their
      * 
      */
    userInput.listWords.zipWithIndex.flatMap { case (inputWord: String,index: Int) => 
      completeDataset.flatMap { datasetRecord =>
    
        val sameIndex = (datasetRecord.listWords.contains(inputWord) && datasetRecord.listWords.indexOf(inputWord) == index)
        sameIndex match {
          case true => Some(Similar(index,inputWord, datasetRecord,userInput))
          case false => None
        }
    
      }
    }.filterNot{x => listCommonWords.contains(x.commonWord)}        //remove stop/common words which could not be termed into subject, verb or object
    
  }
  
  def parseFlowManager = {

    // this is the entry point
    //firstly write to file, to capture and learn from it , append to it
    writeToFile(userInputSimulation.sentence, true)
    
    val listSimilar = extractSuitableRecordListFromDataset(buildDataset, userInputSimulation)

    val zeroChances: List[Chances] = groupSimilar(listSimilar)

    val prediction = processChances(zeroChances,userInputSimulation.sentence)
    
    //after getting prediction , we need to analyse the prediction to see if there are some undecided items and if we can figure them out or improve its results

    /**
      * there are less chances that the subject and object are same...so those kind of results need review
      */

    

    /**
      * printing for debug
      */
    listSimilar.foreach(println)
    zeroChances.foreach(println)
    println(prediction.toString)

    //write to prediction file
    persistPrediction(prediction)
    
  }

  /** Also, everyword in a sentence should get matched, Implies, for ever word in the user input, we should have a record in our dataset featuring that word!
    * 1. The more matching words any 'Similar' record has, more are its chances to be used as template
    * 2. The relative order of found/matched words with that of present in input record ...should be same as this would give us more confidence...else there would be lesser
    * 3. Position of found words in Dataset and position of the found word in the user input , if they are same ...then it would give us more confidence
    * 4. If the max number of found words in the complete list[Similar] are lesser than the total words present in the user Input then we can look for lower ranked Similar classes for finding out the importance of not matched words.
    * 5. While sampling out from the List[Similar] based on the number of matched words , we have to cover for all the words by looking at other lower ranked classes...if the words could not be found there...then we would have to look in the reference files of verb,noun etc to know what those words could be!
    *
    */

 //trigger the process
  parseFlowManager
  



}
