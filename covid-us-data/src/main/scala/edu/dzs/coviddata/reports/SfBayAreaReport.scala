package edu.dzs.coviddata.reports

import edu.dzs.coviddata.valueobjects.RawDataRow
import org.apache.spark.sql.expressions.scalalang.typed
import org.apache.spark.sql.{Dataset, SparkSession}

object SfBayAreaReport {
  private val SF_BAY_AREA_COUNTIES = List(
    "Marin",
    "Sonoma",
    "Napa",
    "Solano",
    "Contra Costa",
    "Alameda",
    "Santa Clara",
    "San Mateo",
    "San Francisco",
  )

  private case class BayAreaReportRow(date: String, active: Int, deaths: Int)

  def buildSfBayAreaReportReport(caData: Dataset[RawDataRow], spark: SparkSession): Any = {
    import spark.implicits._

    caData
      .filter(r => SF_BAY_AREA_COUNTIES
        .exists(sfBayCounty => r
          .province
          .toLowerCase()
          .contains(sfBayCounty.toLowerCase())
        )
      )
      .map(r => (
        r
          .province
          .split(",")(0)
          .replace("County", "")
          .trim,
        r.confirmed,
        r.deaths,
        r.lastUpdate
      ))
      .groupByKey(r => (r._1, r._4))
      .mapGroups({ case (_, rows) => rows.maxBy(_._2) })
      .groupByKey(_._4)
      .agg(typed.sum(_._2), typed.sum(_._3))
      .map(r => BayAreaReportRow(
        date = r._1,
        active = r._2.toInt,
        deaths = r._3.toInt
      ))
      .orderBy("date")
      .collect()
      .toList
  }
}
