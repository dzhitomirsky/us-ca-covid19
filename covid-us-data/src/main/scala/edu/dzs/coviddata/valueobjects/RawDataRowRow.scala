package edu.dzs.coviddata.valueobjects

case class RawDataRowRow(
                      country: String,
                      province: String,
                      lastUpdate: String,
                      confirmed: Int,
                      deaths: Int,
                      recovered: Int
                    ) {

  def isValid(): Boolean = {
    List(country, province, lastUpdate).forall(!_.isEmpty) &&
    confirmed != -1
  }
}
