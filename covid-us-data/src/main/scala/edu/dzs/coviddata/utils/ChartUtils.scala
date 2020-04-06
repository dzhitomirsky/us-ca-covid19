package edu.dzs.coviddata.utils

import edu.dzs.coviddata.valueobjects.USAReportRow

object ChartUtils {
  def smoothData(data: List[USAReportRow]): List[USAReportRow] = {
    data
      .sliding(3)
      .flatMap(triplet => {
        if(triplet.size != 3) triplet
        else {
          val first = triplet.head
          val middle = triplet(1)
          val last = triplet(2)

          if(middle.active < first.active / 10) {
            middle.active = (first.active + last.active) /2
            middle.deaths = (first.deaths + last.deaths) /2
            middle.recovered = (first.recovered + last.recovered) /2
          }

          triplet
        }
      })
      .toList
      .groupBy(_.date)
      .mapValues(_.maxBy(_.active))
      .values
      .toList
      .sortBy(_.date)
  }
}
