package edu.dzs.coviddata.reports

import edu.dzs.coviddata.valueobjects.RawDataRow
import org.apache.spark.sql.{Dataset, SparkSession}

object CaliforniaCountiesReport {

  case class CountyReport(county: String, date: String, active: Int, deaths: Int)

  def buildCaliforniaCountiesReport(usCaData: Dataset[RawDataRow], spark: SparkSession): collection.Map[String, Iterable[CountyReport]] = {
    import spark.implicits._
    usCaData
      .filter(r => r.province.contains(",") && !r.province.contains("County"))
      .groupByKey(r => (r.lastUpdate, r.province))
      .mapGroups({ case (_, rows) => rows.maxBy(_.confirmed) })
      .map(r => CountyReport(
        county = r.province.split(",")(0).trim,
        active = r.confirmed,
        deaths = r.deaths,
        date = r.lastUpdate
      ))
      .orderBy("date")
      .rdd
      .groupBy(_.county)
      .filter({case (_, countyData) => countyData.size > 15 && countyData.last.active >= 50})
      .collectAsMap()
  }
}
