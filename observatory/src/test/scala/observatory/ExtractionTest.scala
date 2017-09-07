package observatory

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import java.time.LocalDate

trait ExtractionTest extends FunSuite with BeforeAndAfterAll {

/**
  test("locateTemperatures for 2015") {
    val temps = Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv").toList
    assert(temps.contains((LocalDate.of(2015,6,2),Location(40.9,14.3),72.8)))
    assert(temps.contains((LocalDate.of(2015,1,1),Location(37,35.417),52.6)))
    assert(temps.contains((LocalDate.of(2015,9,10),Location(35.75,139.35),69.4)))
  }

  test("locationYearlyAverageRecords for 2015") {
    val temps = Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv")
    val avgTemps = Extraction.locationYearlyAverageRecords(temps).toList
    assert(avgTemps.contains((Location(48.794,-122.537),11.655098934550992)))
    assert(avgTemps.contains((Location(62.6,9.667),4.757382039573816)))
    assert(avgTemps.contains((Location(-21.45,115.017),25.26308539944905)))
  }
**/

}