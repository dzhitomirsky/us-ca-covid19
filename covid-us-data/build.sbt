import java.io.File
import scala.reflect.io.Directory
import scala.sys.process._

name := "covid-us-data"
version := "0.1"
scalaVersion := "2.12.8"
scalacOptions ++= Seq("-language:implicitConversions", "-deprecation")
libraryDependencies ++= Seq(
  "com.novocode" % "junit-interface" % "0.11" % Test,
  ("org.apache.spark" %% "spark-core" % "2.4.3"),
  ("org.apache.spark" %% "spark-sql" % "2.4.3")
)
dependencyOverrides ++= Seq(
  ("com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7"),
  ("com.fasterxml.jackson.module" % "jackson-module-scala" % "2.6.7")
)

(compile in Compile) := ((compile in Compile) dependsOn loadData).value

mainClass in (Compile, run)  := Some("edu.dzs.coviddata.Main")

lazy val loadData = taskKey[Unit]("get data from JohnHopkins repository")

loadData := {
  println("Trying to update data directory...")
  val dataDir = new File("src/main/resources/COVID-19")
  val outputDir = new File("output")

  if(outputDir.exists) {
    println("Output directory exists, deleting")
    new Directory(outputDir).deleteRecursively()
  }

  if(dataDir.exists() && dataDir.isDirectory) {
    println("Data directory already exists, deleting")
    new Directory(dataDir).deleteRecursively()

  }

  println("Cloning repo: https://github.com/CSSEGISandData/COVID-19")
  "git clone https://github.com/CSSEGISandData/COVID-19 src/main/resources/COVID-19" !
}
