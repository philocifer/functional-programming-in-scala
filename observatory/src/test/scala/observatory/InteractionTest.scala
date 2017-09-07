package observatory

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

import scala.collection.concurrent.TrieMap

trait InteractionTest extends FunSuite with Checkers {

/**
  test("tileLocation") {
    val loc1 = Tile(0,0,0).location
    assert(roundAt(4)(loc1.lon) === -180.0000)
    assert(roundAt(4)(loc1.lat) === 85.0511)

    val loc2 = Tile(65544,43582,17).location
    assert(roundAt(5)(loc2.lat) === 51.51216)
    assert(roundAt(5)(loc2.lon) === 0.02197)
  }
**/
/*

  test("tile 2015 zoom=2 x=3 y=1") {
    val locatedTemps = Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv")
    val temperatures = Extraction.locationYearlyAverageRecords(locatedTemps)
    val image = Interaction.tile(temperatures, referenceColors, 2, 3, 1)
    image.output(new java.io.File("2015z2x3y1.png"))
    assert(true)
  }

*/
}
