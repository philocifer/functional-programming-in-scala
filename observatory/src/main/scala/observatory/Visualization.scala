package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import scala.math._
import ParaCommon._

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  val radiusOfEarth = ((2*6378.137)+6356.752)/3
  val inverseDistanceWeightingPowerParameter = 3


  def greatCircleDistance(loc1: Location, loc2: Location): Double = {
    val lat1 = toRadians(loc1.lat)
    val lat2 = toRadians(loc2.lat)
    val lon1 = toRadians(loc1.lon)
    val lon2 = toRadians(loc2.lon)
    val deltaLon = abs(lon1 - lon2)
    radiusOfEarth * acos((sin(lat1)) * sin(lat2) + ((cos(lat1) * cos(lat2)) * cos(deltaLon)))

  }

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    val gcdTemperatures = temperatures.map{case (loc, temperature) => (greatCircleDistance(loc, location), temperature)}
    val (minGCD, minGCDTemperature) = gcdTemperatures.minBy(_._1)
    if (minGCD < 1)
      minGCDTemperature
    else {
      val (inverseDistanceWeightingFunctionNumerator, inverseDistanceWeightingFunctionDenominator) =
        gcdTemperatures.aggregate[(Double, Double)]((0,0))({ case ((numeratorSum, denominatorSum), (gcd, temperature)) => {
          val inverseDistanceRatio = 1 / pow(gcd, inverseDistanceWeightingPowerParameter)
          (numeratorSum + (inverseDistanceRatio * temperature), denominatorSum + inverseDistanceRatio)
        }
      }, { case ((numeratorP1, denominatorP1), (numeratorP2, denominatorP2)) => (numeratorP1 + numeratorP2, denominatorP1 + denominatorP2) })
      val inverseDistanceWeightingFunctionResult = inverseDistanceWeightingFunctionNumerator / inverseDistanceWeightingFunctionDenominator
      inverseDistanceWeightingFunctionResult
    }
  }

  def interpolate(y0: Double, y1: Double, x0: Double, x1: Double, x: Double): Double =
    ((y0 * (x1 - x)) + (y1 * (x - x0))) / (x1 - x0)

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    def findInterpolationPoints(points: List[(Double, Color)], value: Double): (Option[(Double, Color)], Option[(Double, Color)]) = {
      points.find{case (temperature, _) => temperature == value} match {
        case Some(point) => (Some(point), None)
        case None => {
          val (smallerPoints, greaterPoints) = points.partition{case (temperature, _) => temperature < value}
          (smallerPoints.reverse.headOption, greaterPoints.headOption)
        }
      }
    }
    val sortedPoints = points.toList.sortBy{case (temperature, _) => temperature}
    val (point1, point2) = findInterpolationPoints(sortedPoints, value)
    (point1, point2) match {
      case (None, Some((_, color))) => color
      case (Some((_, color)), None) => color
      case (Some((temperature1, color1)), Some((temperature2, color2))) => {
        val interpolatedRed = round(interpolate(color1.red, color2.red, temperature1, temperature2, value)).toInt
        val interpolatedGreen = round(interpolate(color1.green, color2.green, temperature1, temperature2, value)).toInt
        val interpolatedBlue = round(interpolate(color1.blue, color2.blue, temperature1, temperature2, value)).toInt
        Color(interpolatedRed, interpolatedGreen, interpolatedBlue)
      }
      case _ => Color(0,0,0)
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {

    val pixels: Array[Pixel] = new Array[Pixel](360 * 180)

    def parPixels(xStart: Int, xEnd: Int, yStart: Int, yEnd: Int, pixels: Array[Pixel]) = {
      for {
        y <- yStart until yEnd
        x <- xStart until xEnd
      } {
        val temperature = predictTemperature(temperatures, Location(90 - y, x - 180))
        val color = interpolateColor(colors, temperature)
        pixels(x + (y * (xEnd - xStart))) = Pixel(color.red, color.green, color.blue, 255)
      }
    }

    val ySeg = 180/4
    parallel(
      parPixels(0, 360, 0*ySeg, 1*ySeg, pixels),
      parPixels(0, 360, 1*ySeg, 2*ySeg, pixels),
      parPixels(0, 360, 2*ySeg, 3*ySeg, pixels),
      parPixels(0, 360, 3*ySeg, 4*ySeg, pixels)
    )

    Image(360, 180, pixels)
  }

}

