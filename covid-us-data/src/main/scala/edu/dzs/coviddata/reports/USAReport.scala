package edu.dzs.coviddata.reports

import edu.dzs.coviddata.valueobjects.{RawDataRowRow, USAReportRow}
import edu.dzs.coviddata.utils.ChartUtils.smoothData
import org.apache.spark.sql.{Dataset, SparkSession}

object USAReport {

  def buildUSAReport(data: Dataset[RawDataRowRow], spark: SparkSession): List[USAReportRow] = {
    import org.apache.spark.sql.expressions.scalalang.typed
    import spark.implicits._

    val result = data
      .filter(row => row.country == "US")
      .groupByKey(r => (r.lastUpdate, r.province))
      .mapGroups({ case (_, rows) => rows.maxBy(_.confirmed) })
      .groupByKey(_.lastUpdate)
      .agg(typed.sum(_.confirmed), typed.sum(_.deaths), typed.sum(_.recovered))
      .map(row => USAReportRow(
        date = row._1,
        active = row._2.toInt,
        recovered = row._3.toInt,
        deaths = row._4.toInt
      ))
      .orderBy("date")

    smoothData(result.collect().toList)
  }
}
