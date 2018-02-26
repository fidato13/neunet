val listOfTemplateWords = List("stemplate", "vtemplate", "otemplate")

def isTemplateKeyWord(word: String): Boolean = {
  listOfTemplateWords.contains(word)
}

val dd = List("my", "stemplate", "vtemplate", "quickly")
val jj = dd.reverse

val kk = List("hello")

val oo = dd.indexWhere(listOfTemplateWords.contains(_))

dd.slice(0,dd.indexOf("vtemplate")) //.find(_ == "vtemplate") 