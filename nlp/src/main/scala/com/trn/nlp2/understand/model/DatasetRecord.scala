package com.trn.nlp2.understand.model

trait Record
case class DatasetRecord(sentence: String, listWords: List[String], subject: List[String], verb: List[String], objectIns: List[String]) extends Record
case class InputRecord(sentence: String, listWords: List[String], subject: List[String], verb: List[String], objectIns: List[String]) extends Record
case class ObservationRecord(sentence: String, listWords: List[String], template: String, listTemplateWords: List[String], subject: List[String], verb: List[String], objectIns: List[String]) extends Record
