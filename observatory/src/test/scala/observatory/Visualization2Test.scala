package observatory

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import math._
import ParaCommon._

trait Visualization2Test extends FunSuite with Checkers {

  def getTemperatures(year: Int): Iterable[(Location, Double)] = {
    val locatedTemps = Extraction.locateTemperatures(year, "/stations.csv", "/" + year.toString + ".csv")
    println("located temperatures " + year.toString)
    val temperatures = Extraction.locationYearlyAverageRecords(locatedTemps)
    println("got temperatures " + year.toString)
    temperatures
  }

  def getNormalYearsTemperatures(year: Int, endYear: Int, temperaturess: List[Iterable[(Location, Double)]]): List[Iterable[(Location, Double)]] = {
    if (year > endYear) temperaturess
    else {
      val temperatures = getTemperatures(year)
      temperaturess match {
        case Nil => getNormalYearsTemperatures(year + 1, endYear, List(temperatures))
        case x :: xs => getNormalYearsTemperatures(year + 1, endYear, temperatures :: temperaturess)
      }
    }
  }

  test("Deviation tiles generation") {
    val (temperaturess1, temperaturess2) = parallel(
        getNormalYearsTemperatures(1975, 1981, Nil),
        getNormalYearsTemperatures(1982, 1989, Nil))
    val temperaturess = temperaturess1 ::: temperaturess2
    val normals = Manipulation.average(temperaturess)
    for (year <- 1990 to 1990 /*2015*/) {
      val temperatures = getTemperatures(year)
      val grid = Manipulation.deviation(temperatures, normals)
      for {
        zoom <- 0 to 0 //3
        y <- 0 to (round(pow(2, zoom)).toInt - 1)
        x <- 0 to (round(pow(2, zoom)).toInt - 1)
      } {
        val image = Visualization2.visualizeGrid(grid, deviationReferenceColors, zoom, x, y)
        image.output(new java.io.File("/target/deviations/" + year.toString + "/" + zoom.toString + "/" + x.toString + "-" + y.toString + ".png"))
      }
    }
    assert(true)
  }

}
