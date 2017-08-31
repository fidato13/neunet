import sbt._
import Keys._

object HelloBuild extends Build {
  lazy val root = Project(id = "currying",
    base = file(".")) aggregate(scala_core, spark_core, cookbook, akka, dsalgo,slack,nlp)

  lazy val scala_core = Project(id = "scala_core",
    base = file("scala"))
  lazy val spark_core = Project(id = "spark_core",
    base = file("spark"))
  lazy val cookbook = Project(id = "cookbook",
    base = file("cookbook"))
  lazy val akka = Project(id = "akka",
    base = file("akka"))
  lazy val dsalgo = Project(id = "dsalgo",
    base = file("dsalgo"))
  lazy val slack = Project(id = "slack",
    base = file("slack"))
  lazy val nlp = Project(id = "nlp",
    base = file("nlp"))

}
