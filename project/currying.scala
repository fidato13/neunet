import sbt._
import Keys._

object HelloBuild extends Build {
    lazy val root = Project(id = "currying",
                            base = file(".")) aggregate(scala_core,spark_core,cookbook)

    lazy val scala_core = Project(id = "scala_core",
                           base = file("scala"))
    lazy val spark_core = Project(id = "spark_core",
                           base = file("spark"))
    lazy val cookbook = Project(id = "cookbook",
                          base = file("cookbook"))
}
