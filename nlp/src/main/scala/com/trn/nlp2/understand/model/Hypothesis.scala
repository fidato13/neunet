package com.trn.nlp2.understand.model

/**
  * A hypothesis is created for every dataset record first...
  * while other records trying to test this hypothesis will match a subset of template words to figure out any possible assertions...
  * we may go with iterations on and on, until there are positive score being increased...as in an iteration we may be able to create a new hypothesis
  * Every round of iterations, we will remove the hypothesis with zero score, since they did n't fit in any case
  * when new records are added to training set then we may need to either re run the entire procedure or devise a mechanism to handle that, but thats for later!
  * 
  * Ultimately Hypothesis will turn into learntRules eventually after sufficient confidence level!
  * initial hypothesis are fine! but to be more precise, they need to be analysed further to bring some new rules
  * @param template
  * @param listTemplateWords
  */
case class Hypothesis(template: String, 
                      listTemplateWords: List[String], 
                      seedSubject: List[String], 
                      seedVerb: List[String], 
                      seedObject: List[String]){
  override def toString: String = {
    s"$template // ${seedSubject.mkString(",")} // ${seedVerb.mkString(",")} // ${seedObject.mkString(",")}"  // as the list of words of template are the same as template , so no point in persisting them
  }
}