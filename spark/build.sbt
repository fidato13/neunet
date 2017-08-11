name := "Spark-Core"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0",
  "org.apache.spark" %% "spark-streaming" % "2.2.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.2.0",
  "com.markmonitor" % "aviator-common" % "0.0.1",
  "com.markmonitor" % "aviator-plugins" % "0.0.1",
  "org.apache.hadoop" % "hadoop-common" % "2.7.3.2.5.0.0-1245"
    exclude("org.mortbay.jetty","jetty")
    exclude("org.mortbay.jetty","jetty-util")
    exclude("javax.servlet","servlet-api")
    exclude("org.codehaus.jackson","jackson-core-asl")
    exclude("org.codehaus.jackson","jackson-mapper-asl")
    ,
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.7.3.2.5.0.0-1245"
    exclude("org.mortbay.jetty","jetty")
    exclude("org.mortbay.jetty","jetty-util")
    exclude("javax.servlet","servlet-api")
    exclude("org.codehaus.jackson","jackson-core-asl")
    exclude("org.codehaus.jackson","jackson-mapper-asl")
  ,
  "redis.clients" % "jedis" % "2.7.3",
  "com.google.code.gson" % "gson" % "2.3.1",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.5.3",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.3",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.5.3",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-hibernate4" % "2.5.3",
  "com.google.guava" % "guava" % "15.0",

  //plugins dependencies
  "com.machinepublishers" % "jbrowserdriver" % "0.17.8",
  "com.github.detro" % "phantomjsdriver" % "1.2.0"
    exclude("xerces","xercesImpl")
    exclude("xml-apis","xml-apis")
  ,
  "org.jsoup" % "jsoup" % "1.10.1",
  "com.jayway.jsonpath" % "json-path" % "2.0.0",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "org.apache.httpcomponents" % "httpmime" % "4.5.2",
  "org.apache.httpcomponents" % "httpclient-cache" % "4.5.2",
  "org.apache.httpcomponents" % "httpcore" % "4.4.5",
  "com.eclipsesource.j2v8" % "j2v8_linux_x86_64" % "4.6.0",
  "commons-validator" % "commons-validator" % "1.5.1",
  "org.apache.commons" % "commons-lang3" % "3.4"

)

resolvers += "Local Maven Repository" at "file:///"+Path.userHome+"/.m2/repository"

