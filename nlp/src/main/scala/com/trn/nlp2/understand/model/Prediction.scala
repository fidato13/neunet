package com.trn.nlp2.understand.model

case class Prediction(val inputText: String, val subject: String, val verb: String, val objectIns: String)
case class ProbableSeed(val hypothesis: Hypothesis, val subject: String, val verb: String, val objectIns: String)