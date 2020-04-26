package edu.dzs.coviddata

import edu.dzs.coviddata.reports._
import edu.dzs.coviddata.utils.FileUtils.usaReportDs2Json
import edu.dzs.coviddata.utils.ReadDataUtils.readFilesToDs
import edu.dzs.coviddata.valueobjects.RawDataRow
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

  /**
   * Wrapper to return action execution result with execution time
   *
   * @param action function () => Any
   * @return (execution result, execution time)
   */
  def executeWithMeter(action: () => Any): (Any, Double) = {
    val start = System.currentTimeMillis()
    val executionResult = action()
    val tookSeconds = (System.currentTimeMillis() - start) / 1000.0

    (executionResult, tookSeconds)
  }


  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()

    val usData: Dataset[RawDataRow] = readFilesToDs(spark, SOURCE_FOLDER)
      .filter(_.country == "US")
      .repartition(1)
      .persist()

    val californiaData = usData
      .filter(row => row.province.toLowerCase.contains("california") || row.province.contains("CA"))
      .persist()

    val startCalculations = System.currentTimeMillis()
    val dataRead = (startCalculations - start) / 1000.0

    val (usaReport, usaReportTime) = executeWithMeter(() => USAReport.buildUSAReport(usData, spark))
    val (caReport, caReportTime) = executeWithMeter(() => CaliforniaReport.buildCaliforniaReport(californiaData, spark))
    val (statesReport, statesReportTime) = executeWithMeter(() => SatesReport.buildStatesReport(usData, spark))
    val (californiaByCountyReport, californiaByCountyReportTime) = executeWithMeter(() => CaliforniaByCountyReport.buildCaliforniaCountyReport(californiaData, spark))
    val (californiaCountiesReport, californiaCountiesReportTime) = executeWithMeter(() => CaliforniaCountiesReport.buildCaliforniaCountiesReport(californiaData, spark))
    val (bayAreaReport, bayAreaReportTine) = executeWithMeter(() => SfBayAreaReport.buildSfBayAreaReportReport(californiaData, spark))

    usaReportDs2Json(usaReport, "usa-data.json")
    usaReportDs2Json(caReport, "usa-california-data.json")
    usaReportDs2Json(statesReport, "usa-by-state-data.json")
    usaReportDs2Json(californiaByCountyReport, "usa-california-by-county-data.json")
    usaReportDs2Json(californiaCountiesReport, "usa-california-counties-data.json")
    usaReportDs2Json(bayAreaReport, "california-sf-bay-data.json")

    val calculationDone = System.currentTimeMillis()

    val calculusTook = (calculationDone - startCalculations) / 1000.0


    spark.stop()

    print("\n")
    println("Total Calculus took: " + calculusTook)
    println("Data read took: " + dataRead)

    print("\n")
    println("Usa Report: " + usaReportTime)
    println("California By County report: " + caReportTime)
    println("State report took: " + statesReportTime)
    println("California by county bar chart report: " + californiaByCountyReportTime)
    println("California Counties line charts:  " + californiaCountiesReportTime)
    println("California sf bay line chart:  " + bayAreaReportTine)
  }
}

