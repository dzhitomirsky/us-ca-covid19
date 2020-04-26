package edu.dzs.coviddata.reports

import edu.dzs.coviddata.valueobjects.{CaliforniaStateReportRow, RawDataRow}
import org.apache.spark.sql.{Dataset, SparkSession}

object CaliforniaByCountyReport {
  def buildCaliforniaCountyReport(data: Dataset[RawDataRow], spark: SparkSession): List[Any] = {
    import spark.implicits._

    data
      .filter(_.province.contains(","))
      .groupByKey(_.province)
      .mapGroups({ case (_, rows) => rows.maxBy(_.lastUpdate) })
      .map(row => CaliforniaStateReportRow(
        province = row.province.split(",")(0).trim,
        active = row.confirmed,
        deaths = row.deaths,
        recovered = row.recovered
      ))
      .sort($"active".desc)
      .collect()
      .toList
  }
}
