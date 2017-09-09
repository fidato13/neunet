name := "facebook"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.jsoup" % "jsoup" % "1.8.1",
  "commons-codec" % "commons-codec" % "1.10",
  "org.apache.commons" % "commons-email" % "1.1",
  "com.github.finagle" %% "finch-core" % "0.12.0" exclude("org.openjdk.jmh", "*"),
  "com.github.finagle" %% "finch-circe" % "0.12.0" exclude("org.openjdk.jmh", "*"),
  "com.typesafe" % "config" % "1.3.1",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "org.slf4j" % "slf4j-simple" % "1.7.21",
  "org.scalaj" % "scalaj-http_2.11" % "2.3.0",
  "io.circe" %% "circe-generic" % "0.8.0",
  "io.circe" %% "circe-parser" % "0.8.0",
  "com.typesafe.play" % "play-json_2.11" % "2.6.3",
  "com.jcraft" % "jsch" % "0.1.54",
  "com.trn" %% "nlp" % "0.1",
  "org.apache.solr" % "solr-solrj" % "5.5.2"

)

val mvnLocal = "Local Maven Repository" at "file:///"+Path.userHome+"/.m2/repository"

val ivyLocal = Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
//externalResolvers := Seq(ivyLocal, mvnLocal)

enablePlugins(JavaAppPackaging)

enablePlugins(DockerPlugin)

// setting a maintainer which is used for all packaging types
maintainer:= "fidato"

// exposing the play ports
dockerExposedPorts in Docker := Seq(7070)
