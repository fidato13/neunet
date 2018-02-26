package com.trn.nlp2.understand.model


/**
  * This class is used to represent the list of common words between the passed user input and a record in dataset(memory)
  * @param commonWord
  * @param datasetRecord
  * @param inputRecord
  */

case class Similar(val indexOfWord: Int, val commonWord: String, val datasetRecord: DatasetRecord, val inputRecord: InputRecord)
