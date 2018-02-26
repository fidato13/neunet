package com.trn.nlp2.understand.observations

import java.io.FileWriter

import com.trn.nlp2.understand.model.{DatasetRecord, Hypothesis, ObservationRecord}
import com.trn.nlp2.understand.Initialization._

import scala.collection.immutable
import scala.util.matching.Regex
import scala.io.Source

object Observe extends App {

  /**
    * template word replcaements, as the normal subject, object, verb could feature in normal sentences:
    * subject => stemplate
    * verb => vtemplate
    * object => otemplate
    */

  val listOfTemplateWords = List("stemplate", "vtemplate", "otemplate")

  /**
    * Let's formulate some rules or rather let AI create some rules of its own! ;)
    *
    * let's create rule for templates with 
    */

  def persistToFile(text: String) = {
    val writer = new FileWriter("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/observations/observation", true)
    writer.write(s"$text")
    writer.write("\n")
    writer.close()
  }

  def getObservationRecord(datasetRecord: DatasetRecord): ObservationRecord = {
    val templateListOfWords: List[String] = datasetRecord.listWords.map { word =>
      datasetRecord.subject.contains(word) match {
        case true => "stemplate"
        case false => datasetRecord.verb.contains(word) match {
          case true => "vtemplate"
          case false => datasetRecord.objectIns.contains(word) match {
            case true => "otemplate"
            case false => word
          }
        }
      }

    }

    val template = templateListOfWords.mkString(" ")

    ObservationRecord(datasetRecord.sentence,
      datasetRecord.listWords,
      template,
      templateListOfWords,
      datasetRecord.subject,
      datasetRecord.verb,
      datasetRecord.objectIns
    )
  }

  /**
    * This one formulates initial hypothesis ...ie replaces subject words with stemplate etc 
    *
    * @param completeDataset
    * @return
    */
  def formaulateHypothesis(completeDataset: List[DatasetRecord]): List[Hypothesis] = {

    // lets create a/multiple hypothesis per record
    val listHypothesis: List[Hypothesis] = completeDataset.map { datasetRecord =>
      //create observation record from DatasetRecord
      val observationRecord: ObservationRecord = getObservationRecord(datasetRecord)

      // lets create a hypothesis ..
      Hypothesis(observationRecord.template, observationRecord.listTemplateWords, datasetRecord.subject,datasetRecord.verb,datasetRecord.objectIns)
    }

    listHypothesis

  }

  def writeHypothesisToFile(listHypothesis: List[Hypothesis], fileName: String, rewrite: Boolean) = {
    val writer = new FileWriter(s"/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/hypothesis/$fileName", rewrite) // false - rewrite , true - append

    listHypothesis.foreach { x =>
      writer.write(s"$x")
      writer.write("\n")

    }
    writer.close
  }

  /**
    * Implementation needs to change for the correct Hypothesis elements 
    * @return
    */
  def readHypothesisFromFile: List[Hypothesis] = {
    val bufferedSource = Source.fromFile("/trn/git/gitlab/currying/nlp/src/main/resources/nlp2/hypothesis/hypothesis")

    /**
      * Read all the lines and populate them in a list
      */
    val lines = bufferedSource.getLines().map(_.toLowerCase)

    lines.map { line =>
      val lineElementsArray = line.split("//")
      Hypothesis(lineElementsArray(0).trim, 
        lineElementsArray(0).split(" ").map(_.trim).toList, 
        lineElementsArray(1).split(",").toList,
        lineElementsArray(2).split(",").toList,
        lineElementsArray(3).split(",").toList)
    }.toList
  }

  /**
    * This method gives a probable list of hypothesis ie combinations of all the words
    *
    * @param hypothesisToTestWordList
    * @return
    */
  def probableHypothesisList(hypothesisToTestWordList: List[String]) = {

    val hypothesisToTestWordListSize = hypothesisToTestWordList.size
    // make patterns out of hypothesisToTestWordList , because we want to test that hypothesis out... to see if the patterns out of that hypothesis exist in other ones as well.
    val possibleListOfPatterns: List[String] = hypothesisToTestWordList.zipWithIndex.flatMap { case (word, index) =>
      val range: Range = index until hypothesisToTestWordListSize
      val patternList: List[String] = {
        for (
          i <- range
        ) yield (hypothesisToTestWordList.slice(index, i + 1).mkString(" "))
      }.toList //.flatten.toList
      //println("patternList => "+patternList)
      patternList
    }
    possibleListOfPatterns

  }

  def analyzeHypothesis(listOfInitialHypothesis: List[Hypothesis], listTemplateWords: List[String]) = {


    val listOfInitialHypothesisSize = listOfInitialHypothesis.size
    val listOfInitialHypothesisZip = listOfInitialHypothesis.zipWithIndex

    listOfInitialHypothesisZip.foreach { case (element, index) =>
      val hypothesisToTest: Hypothesis = element // may throw exception


      val listWordsFromHypothesisToTest = hypothesisToTest.listTemplateWords

      // The below has given a sutiable list of probable hypothesis for any given hypothesis  hypothesisToTest
      val possibleListOfPatterns = probableHypothesisList(listWordsFromHypothesisToTest)

      //println(possibleListOfPatterns)

      val analystHypothesisList = listOfInitialHypothesis.slice(index + 1, listOfInitialHypothesisSize) // this will throw exception if our hypothesis list is just an element long ie it doesn't have a tail

      val listFromAllTestersForSingleHypothesis: List[Hypothesis] = analystHypothesisList.flatMap { testerHypothesis =>

        val listWordsFromTester = testerHypothesis.listTemplateWords

        // list of intersecting words , every word may create a new hypothesis
        val intersectingWordsList = listWordsFromTester.intersect(listWordsFromHypothesisToTest).filter {
          listTemplateWords.contains(_)
        }

        // from possibleListOfPatterns ... remove all those elements which does not contain any of the two template words and the intersecting word
        val probablePatternHypothesisList: List[String] = possibleListOfPatterns.filter { pattern =>
          val patternListLocal = pattern.split(" ").toList
          patternListLocal.intersect(listTemplateWords).size > 1 &&
            patternListLocal.intersect(intersectingWordsList).size > 0

        }
       
        // now for each probablePatternHypothesisList , find this pattern in this testerList...if found something, persist that on file
        //val b = new Regex("stemplate is vtemplate")
        val matchedPatternListwithTester: List[String] = probablePatternHypothesisList.flatMap { patternElement =>
          val pattern = new Regex(patternElement)
          pattern.findAllIn(testerHypothesis.template).toList
        }

        //println(matchedPatternListwithTester)
        // either persist this here on file ....
        // or keep in memory... might throw an exception later
        matchedPatternListwithTester.map { x => Hypothesis(x, 
          x.split(" ").toList,
          (testerHypothesis.seedSubject ::: hypothesisToTest.seedSubject).toSet.toList,
          (testerHypothesis.seedVerb ::: hypothesisToTest.seedVerb).toSet.toList,
          (testerHypothesis.seedObject ::: hypothesisToTest.seedObject).toSet.toList
        ) }
      }
      
      val deduplicatedHypothesisList: List[Hypothesis] = deduplicateHypothesisListBasedOnTemplate(listFromAllTestersForSingleHypothesis)
      
       

      // next action would be to persist this
      /**
        * Wither persist it after every hypothesis or combine for all the records and then process , better to write it after every record and then optimise further if required
        */
      writeHypothesisToFile(deduplicatedHypothesisList, "hypothesis", true)

    }

  }
  
  def deduplicateHypothesisListBasedOnTemplate(listHypothesis: List[Hypothesis]): List[Hypothesis] = {
    listHypothesis.groupBy(_.template)  // this is the set for single hypothesis being tested against all of the testers
      .map { case (key, listOfHypothesis) =>
      val combinedSeedSubject: List[String] = listOfHypothesis.flatMap { x => x.seedSubject}.map{x: String => x.trim}.filter{_.length > 0}.toSet.toList
      val combinedSeedVerb: List[String] = listOfHypothesis.flatMap { x => x.seedVerb}.map{x: String => x.trim}.filter{_.length > 0}.toSet.toList
      val combinedSeedObject: List[String] = listOfHypothesis.flatMap { x => x.seedObject}.map{x: String => x.trim}.filter{_.length > 0}.toSet.toList

      Hypothesis(key, key.split(" ").toList, combinedSeedSubject, combinedSeedVerb, combinedSeedObject)

    }.toList
  }

  def optimiseHypothesis: List[Hypothesis] = {
    val hypothesisListFromFile: List[Hypothesis] = readHypothesisFromFile 
    
    // group and deduplicate
    val deduplicatedHypothesisList: List[Hypothesis] = deduplicateHypothesisListBasedOnTemplate(hypothesisListFromFile)

    /**
      * write optimised hypothesis to the file
      */
    writeHypothesisToFile(deduplicatedHypothesisList, "hypothesis", false)

    deduplicatedHypothesisList
  }
  

  def observationFlowManager = {
    val initialHypothesisList: List[Hypothesis] = formaulateHypothesis(buildDataset)

    //write initial hypothesis to file  // only for debugging, will be removedx
    writeHypothesisToFile(initialHypothesisList, "InitialHypothesis", false)

    // this analyses the initial hypothesis generated from the first step
    analyzeHypothesis(initialHypothesisList, listOfTemplateWords)
    
    // optimise above generated file of hypothesis and rewrite by deduplicating the records.
   val updatedHypothesisList =  optimiseHypothesis
   
  }


  observationFlowManager

  //debug - check/populate Hypothesis list from file
  //val hypothesisListFromFile = readHypothesisFromFile

}
