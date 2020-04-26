package edu.dzs.coviddata.reports

import edu.dzs.coviddata.valueobjects.{RawDataRow, USAReportRow}
import org.apache.spark.sql.expressions.scalalang.typed
import org.apache.spark.sql.{Dataset, SparkSession}
import edu.dzs.coviddata.utils.ChartUtils.smoothData

object USAReport {

  def buildUSAReport(data: Dataset[RawDataRow], spark: SparkSession): List[USAReportRow] = {
    import spark.implicits._

    smoothData(data
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
      .collect()
      .toList)
  }
}
