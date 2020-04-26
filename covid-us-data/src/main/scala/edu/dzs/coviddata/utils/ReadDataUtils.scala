package edu.dzs.coviddata.utils

import edu.dzs.coviddata.utils.FileUtils.listDataFiles
import edu.dzs.coviddata.utils.MappingUtils.dfRow2ReportRow
import edu.dzs.coviddata.valueobjects.RawDataRow
import org.apache.spark.sql.{Dataset, SparkSession}

object ReadDataUtils {

  private val sparkReadOption = Map(
    "header" -> "true",
    "inferSchema" -> "true"
  )

  def readFilesToDs(spark: SparkSession, dataFolder: String): Dataset[RawDataRow] = {
    import spark.implicits._

    val files = listDataFiles(dataFolder)
//    val files = listDataFiles(dataFolder).filter(_.endsWith("02-27-2020.csv"))
    // as long is csv file mapping is different in different csv files we have
    // to read them separately because otherwise Spark messes up column order

    val dataSets: Seq[Dataset[RawDataRow]] = files.map(filePath =>
      spark
        .read
        .options(sparkReadOption)
        .csv(filePath)
        .map(dfRow2ReportRow)
    )
    val unionDs = spark.emptyDataset[RawDataRow]

    dataSets
      .foldLeft(unionDs){(acc, ds) => acc.union(ds)}
      .filter(_.isValid())
  }
}
