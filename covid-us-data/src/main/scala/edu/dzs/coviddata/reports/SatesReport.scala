package edu.dzs.coviddata.reports

import edu.dzs.coviddata.valueobjects.{RawDataRow, StateReportRow}
import org.apache.spark.sql.{Dataset, SparkSession}

object SatesReport {
  def buildStatesReport(data: Dataset[RawDataRow], spark: SparkSession): List[Any] = {
    import org.apache.spark.sql.expressions.scalalang.typed
    import spark.implicits._

    data
      .filter(row =>
          row.province.contains(',')
          && !row.province.contains('.')
          && !row.province.contains('(')
          && !row.province.contains(')')
      )
      .groupByKey(_.province)
      .mapGroups({ case (_, rows) => rows.maxBy(_.lastUpdate) })
      .groupByKey(r => {
        val stateData = r.province.split(",")
        if (stateData.size > 1) stateData(1).trim
        else r.province
      })
      .agg(typed.sum(_.confirmed), typed.sum(_.deaths), typed.sum(_.recovered))
      .map(r => StateReportRow(
        state = r._1,
        active = r._2.toInt,
        deaths = r._3.toInt,
        recovered = r._4.toInt
      ))
      .filter(r => r.state.length > 2 && r.state != "Wuhan Evacuee")
      .orderBy($"active".desc)
      .collect()
      .toList
  }
}

