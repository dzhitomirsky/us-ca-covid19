package edu.dzs.coviddata.utils

import java.io.{File, PrintWriter}

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import edu.dzs.coviddata.valueobjects.USAReportRow

import scala.reflect.io.Directory

object FileUtils {
  private val CSV_EXTENSION = ".csv"

  def listDataFiles(sourceFolder: String): List[String] = {
    val filesDir = new File(sourceFolder)
    if (filesDir.exists() && filesDir.isDirectory) filesDir
      .listFiles
      .filter(_.isFile)
      .toList
      .sortBy(_.getName)
      .map(_.getPath)
      .filter(_.contains(CSV_EXTENSION))
    else Nil
  }


  def usaReportDs2Json(data: List[Any], filePath: String) = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)

    val outDir = new File("output")
    outDir.mkdir()

    mapper.writeValue(new PrintWriter(new File(outDir, filePath)), data)
  }

}
