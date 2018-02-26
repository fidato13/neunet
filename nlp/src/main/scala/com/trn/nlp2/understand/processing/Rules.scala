package com.trn.nlp2.understand.processing

object Rules {
  
  val listCommonWords = List("a","an","the","can", "could")

  /**
    * Pre prediction rules
    * 1. look for stop/common words like "the,a,an etc" which can't be subject, object or verb...remove them from scanning ... 
    * but if we remove them then it might impact the algorithm as they play a part in the position of the words ...
    * when the initial scanning from dataset based on the index of the words has been done then we can remove those stopwords list from further processing
    * -- The above point has been taken care of
    */

  /**
    * During the training phase, can we introduce some sort of Analyser/Rule writer/Observations for itself, for example:
    * if we found a word/subject in a sentence .... try to write something about it, "like if a word comes after 'the' , it has to be a subject" ... like a hypothesis , then see how it behaves with other found words and their sentences....
    * if we a rule getting true again in any new sentence, we can increment the points of that rule...thus we can establish some insights/rules and general behaviours.
    * The rules which would not get any points will be discarded by AI. Thus it will write its own rule!!
    * 
    * Few Rules could be like... A word appearing after/before X places of a certain word is likely/%percent chance to be a key entity etc...
    * or a word beginning with _______ something always has next word as subject
    * 
    * => Adjacent Rule ... the keyword that we found has a particular word before or after with it associated
    * => two word Adjacent rule ... the keyword that we found has always a condition like this "he was singing" ... now singing is our verb that is present in training data
    * we can establish a rule that before any verb , there are these words present "he was" .. so following this rule...AI , whenever it sees "He was", it can 
    * assign the next word to be a verb blindly ... like "he was dancing" , "he was lying" , "he was shivering"
    * 
    * As we can't do it for every word to check it's every neighbour as it is going to be a massive 
    * 
    * This process can spawn multiple iterations...First phase being jotted down the observation aka "writing the rules"... Following iteration ... 
    * testing them out , voting as they are being tested.. third iteration being making them formal ... apply them in actual tests....
    * 
    * One way of figuring out rules would be :
    * grab the words around Keywords... like "he was verb(singing)" , another  "he was verb(laughing)" ... "he was verb(dancing)"...
    * so analyse this collected data... as we can see he was always present..."he was verb" is mostly true ...  
    * so one rule would be after observation:
    * "He was ___" ...this blank is a verb
    * 
    * 
    * We can build our observations one time, as in the dataset we have they raw sentence as well as the values/keywords(subject,verb,object)
    * 
    */
  
  /** Post Prediction Rules(after 1st iteration)
    * 1. if there are missing features from 1st iteration of prediction, like subject we could not find etc
    * 2. if the subject and object are same or any two/three features are same then that requires review
    */

}
