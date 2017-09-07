package observatory


import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

trait VisualizationTest extends FunSuite with Checkers {

/*
  test("predictTemperature1") {
    val temperatures = List(
      (Location(45.917,15.967),-48.0),
      (Location(43.817,28.583),-42.0),
      (Location(79.383,102.417),-6.0),
      (Location(67.55,33.35),1.0),
      (Location(49.033,23.517),73.0),
      (Location(-15.267,12.15),-44.0),
      (Location(-37.833,77.567),63.0),
      (Location(-12.483,34.083),63.0),
      (Location(-29.462,27.553),34.0),
      (Location(-10.583,142.3),-33.0),
      (Location(62.117,-136.183),11.0),
      (Location(78.667,-74.5),-3.0),
      (Location(32.533,-114.517),21.0),
      (Location(37.167,-5.617),18.0),
      (Location(30.317,-9.417),-20.0),
      (Location(-28.5,-47.533),-70.0),
      (Location(-7.933,-14.417),69.0),
      (Location(-15.933,-5.667),-9.0),
      (Location(-71.05,-10.9),39.0),
      (Location(-37.05,-12.317),66.0)
    )
    val predictedTemperature = Visualization.predictTemperature(temperatures,Location(20, 20))
    assert(roundAt(8)(predictedTemperature) === -6.31886437)
  }

  test("predictTemperature2") {
    val temperatures = List(
      (Location(45.917,15.967),-48.0),
      (Location(43.817,28.583),-42.0),
      (Location(79.383,102.417),-6.0),
      (Location(67.55,33.35),1.0),
      (Location(49.033,23.517),73.0),
      (Location(-15.267,12.15),-44.0),
      (Location(-37.833,77.567),63.0),
      (Location(-12.483,34.083),63.0),
      (Location(-29.462,27.553),34.0),
      (Location(-10.583,142.3),-33.0),
      (Location(62.117,-136.183),11.0),
      (Location(78.667,-74.5),-3.0),
      (Location(32.533,-114.517),21.0),
      (Location(37.167,-5.617),18.0),
      (Location(30.317,-9.417),-20.0),
      (Location(-28.5,-47.533),-70.0),
      (Location(-7.933,-14.417),69.0),
      (Location(-15.933,-5.667),-9.0),
      (Location(-71.05,-10.9),39.0),
      (Location(-37.05,-12.317),66.0)
    )
    val predictedTemperature = Visualization.predictTemperature(temperatures,Location(30, 20))
    assert(roundAt(8)(predictedTemperature) === -18.11801980)
  }

  test("predictTemperature3") {
    val temperatures = List(
      (Location(45.917,15.967),-48.0),
      (Location(43.817,28.583),-74.0),
      (Location(79.383,102.417),75.0),
      (Location(67.55,33.35),9.0),
      (Location(49.033,23.517),24.0),
      (Location(-15.267,12.15),65.0),
      (Location(-37.833,77.567),48.0),
      (Location(-12.483,34.083),2.0),
      (Location(-29.462,27.553),19.0),
      (Location(-10.583,142.3),-79.0),
      (Location(62.117,-136.183),20.0),
      (Location(78.667,-74.5),46.0),
      (Location(32.533,-114.517),64.0),
      (Location(37.167,-5.617),-35.0),
      (Location(30.317,-9.417),66.0),
      (Location(-28.5,-47.533),-47.0),
      (Location(-7.933,-14.417),42.0),
      (Location(-15.933,-5.667),-63.0),
      (Location(-71.05,-10.9),61.0),
      (Location(-37.05,-12.317),-1.0)
    )
    val predictedTemperature = Visualization.predictTemperature(temperatures,Location(-15.267, 12.15))
    assert(predictedTemperature === 65.0)
  }

  test("predictTemperature4") {
    val temperatures = List(
      (Location(45.917,15.967),-48.0),
      (Location(43.817,28.583),-74.0),
      (Location(79.383,102.417),75.0),
      (Location(67.55,33.35),9.0),
      (Location(49.033,23.517),24.0),
      (Location(-15.267,12.15),65.0),
      (Location(-37.833,77.567),48.0),
      (Location(-12.483,34.083),2.0),
      (Location(-29.462,27.553),19.0),
      (Location(-10.583,142.3),-79.0),
      (Location(62.117,-136.183),20.0),
      (Location(78.667,-74.5),46.0),
      (Location(32.533,-114.517),64.0),
      (Location(37.167,-5.617),-35.0),
      (Location(30.317,-9.417),66.0),
      (Location(-28.5,-47.533),-47.0),
      (Location(-7.933,-14.417),42.0),
      (Location(-15.933,-5.667),-63.0),
      (Location(-71.05,-10.9),61.0),
      (Location(-37.05,-12.317),-1.0)
    )
    val predictedTemperature = Visualization.predictTemperature(temperatures,Location(43.825, 28.5831))
    assert(predictedTemperature === -74.0)
  }
*/

/**
  test("interpolate max color") {
    assert(Visualization.interpolateColor(referenceColors, 70) === Color(255,255,255))
  }

  test("interpolate min color") {
    assert(Visualization.interpolateColor(referenceColors, -65) === Color(0,0,0))
  }

  test("interpolate mid colors") {
    assert(Visualization.interpolateColor(referenceColors, 50) === Color(255,164,164))
    assert(Visualization.interpolateColor(referenceColors, 20) === Color(255,153,0))
    assert(Visualization.interpolateColor(referenceColors, 8) === Color(170,255,85))
    assert(Visualization.interpolateColor(referenceColors, -10) === Color(0,85,255))
    assert(Visualization.interpolateColor(referenceColors, -20) === Color(106,0,255))
    assert(Visualization.interpolateColor(referenceColors, -40) === Color(130,0,171))
    assert(Visualization.interpolateColor(referenceColors, -55) === Color(17,0,54))
  }
**/

/*

  test("visualize 2015") {
    val locatedTemps = Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv")
    println("located temperatures")
    val temperatures = Extraction.locationYearlyAverageRecords(locatedTemps)
    println("got temperatures")
    val image = Visualization.visualize(temperatures, referenceColors)
    image.output(new java.io.File("2015.png"))
    assert(true)
  }

*/

}
