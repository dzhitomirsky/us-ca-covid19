package edu.dzs.coviddata

import edu.dzs.coviddata.valueobjects.RawDataRowRow
import edu.dzs.coviddata.utils.ReadDataUtils.readFilesToDs
import edu.dzs.coviddata.utils.FileUtils.usaReportDs2Json
import edu.dzs.coviddata.reports.{CaliforniaReport, SatesReport, USAReport, CaliforniaProvinceReport}
import org.apache.spark.sql.Dataset

object Main {

  import org.apache.spark.sql.SparkSession

  private val SOURCE_FOLDER = "src/main/resources/COVID-19/csse_covid_19_data/csse_covid_19_daily_reports"

  private val spark: SparkSession =
    SparkSession
      .builder()
      .appName("COVID19")
      .master("local")
      .getOrCreate()


  def main(args: Array[String]): Unit = {
    val data: Dataset[RawDataRowRow] = readFilesToDs(spark, SOURCE_FOLDER).persist()

    val usaReport = USAReport.buildUSAReport(data, spark)
    val caliReport = CaliforniaReport.buildCaliforniaReport(data, spark)
    val statesReport = SatesReport.buildStatesReport(data, spark)
    val californiaCountyReport = CaliforniaProvinceReport.buildCaliforniaCountyReport(data, spark)

    usaReportDs2Json(usaReport, "usa-data.json")
    usaReportDs2Json(caliReport, "usa-california-data.json")
    usaReportDs2Json(statesReport, "usa-by-state-data.json")
    usaReportDs2Json(californiaCountyReport, "usa-california-by-county-data.json")

    spark.stop()
  }

}

