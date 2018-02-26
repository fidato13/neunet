package com.trn.nlp2.understand.model

case class Chances(val word: String, val chanceSubject: (Double, Int), val chanceVerb: (Double, Int), val chanceObject: (Double, Int))