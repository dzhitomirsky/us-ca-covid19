package edu.dzs.coviddata.valueobjects

case class CaliforniaStateReportRow(
                                   province: String,
                                   active: Int,
                                   deaths: Int,
                                   recovered: Int
                                   )
