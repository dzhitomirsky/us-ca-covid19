package edu.dzs.coviddata.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat

import edu.dzs.coviddata.valueobjects.RawDataRow
import org.apache.spark.sql.Row

object MappingUtils {

  private case class FiledMapping(filedName: String, columnNames: Set[String], extractor: (Row, String) => Any)

  private val dateOutputFormat = new SimpleDateFormat("MM-dd-yyyy")

  private val datePatterns = Seq(
    new SimpleDateFormat("M/dd/yyyy"),
    new SimpleDateFormat("yyyy-MM-dd")
  )

  private val optionalNumericExtractor = (row: Row, column: String) => row.getAs[Any](column) match {
    case v: Int => v
    case _ => if (column == "Confirmed") -1
    else 0
  }

  private val optionalStringExtractor = (row: Row, column: String) => row.getAs[Any](column) match {
    case v: String => v
    case _ => ""
  }

  private val mappings = Map(
    "country" -> FiledMapping(
      filedName = "country",
      columnNames = Set("Country_Region", "Country/Region"),
      extractor = optionalStringExtractor
    ),

    "province" -> FiledMapping(
      filedName = "country",
      columnNames = Set("Province/State", "Province_State"),
      extractor = (row: Row, column: String) => {
        val province = optionalStringExtractor(row, column)
        try {
          row.getAs[Any]("Admin2") match {
            case v: String => s"$v, $province"
            case _ => province
          }
        } catch {
          case e: Exception => province
        }
      }
    ),

    "lastUpdate" -> FiledMapping(
      filedName = "country",
      columnNames = Set("Last Update", "Last_Update"),
      extractor = (row, column) => {
        val dateString = row.getAs[Any](column) match {
          case v: String => v
          case v: Timestamp => v.toString
          case _ => ""
        }

        if (dateString.isEmpty) dateString
        else {
          val foundFormatterOption = datePatterns
            .find(f => {
              try {
                f.parse(dateString)
                true
              } catch {
                case e: Throwable => false
              }
            })

          if (foundFormatterOption.isDefined) {
            val formattedValue = dateOutputFormat.format(foundFormatterOption.get.parse(dateString))
            if (formattedValue.startsWith("0020")) formattedValue.replace("0020", "2020")
            else formattedValue
          }
          else {
            ""
          }
        }
      }
    ),

    "confirmed" -> FiledMapping(
      filedName = "country",
      columnNames = Set("Confirmed"),
      extractor = optionalNumericExtractor
    ),

    "deaths" -> FiledMapping(
      filedName = "country",
      columnNames = Set("Deaths"),
      extractor = optionalNumericExtractor
    ),

    "recovered" -> FiledMapping(
      filedName = "country",
      columnNames = Set("Recovered"),
      extractor = optionalNumericExtractor
    )
  )

  def dfRow2ReportRow(row: Row): RawDataRow = {
    def getField(fieldName: String) = {
      val mapping = mappings(fieldName)
      val colName = row
        .schema
        .fields
        .map(_.name)
        .toSet
        .intersect(mapping.columnNames)
        .head

      mapping.extractor(row, colName)
    }

    RawDataRow(
      country = getField("country").asInstanceOf[String],
      province = getField("province").asInstanceOf[String],
      lastUpdate = getField("lastUpdate").asInstanceOf[String],
      confirmed = getField("confirmed").asInstanceOf[Int],
      deaths = getField("deaths").asInstanceOf[Int],
      recovered = getField("recovered").asInstanceOf[Int]
    )
  }
}
