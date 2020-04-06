package edu.dzs.coviddata.valueobjects

case class USAReportRow(
                         date: String,
                         var active: Int,
                         var recovered: Int,
                         var deaths: Int
                       )
