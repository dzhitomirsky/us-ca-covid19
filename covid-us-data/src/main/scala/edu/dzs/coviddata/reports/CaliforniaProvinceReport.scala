package edu.dzs.coviddata.reports

import edu.dzs.coviddata.valueobjects.{CaliforniaStateReportRow, RawDataRowRow}
import org.apache.spark.sql.{Dataset, SparkSession}

object CaliforniaProvinceReport {
  def buildCaliforniaCountyReport(data: Dataset[RawDataRowRow], spark: SparkSession): List[Any] = {
    import spark.implicits._

    data
      .filter(row =>
        row.country == "US"
          && row.province.contains(",")
          && row.province.toLowerCase.contains("california")
      )
      .groupByKey(_.province)
      .mapGroups({ case (_, rows) => rows.maxBy(_.lastUpdate) })
      .map(row => {
        val stateData = row.province.split(",")
        val calProvince = if (stateData.size > 1) stateData(0).trim
        else row.province

        CaliforniaStateReportRow(
          province = calProvince,
          active = row.confirmed,
          deaths = row.deaths,
          recovered = row.recovered
        )
      })
      .sort($"active".desc)
      .collect()
      .toList
  }
}
