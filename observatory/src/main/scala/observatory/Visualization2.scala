package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import ParaCommon._
import math._

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {

  /**
    * @param x X coordinate between 0 and 1
    * @param y Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(
    x: Double,
    y: Double,
    d00: Double,
    d01: Double,
    d10: Double,
    d11: Double
  ): Double = {
    (d00 * (1 - x) * (1 - y)) + (d10 * x * (1 - y)) + (d01 * (1 - x) * y) + (d11 * x * y)
  }

  /**
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param zoom Zoom level of the tile to visualize
    * @param x X value of the tile to visualize
    * @param y Y value of the tile to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
    grid: (Int, Int) => Double,
    colors: Iterable[(Double, Color)],
    zoom: Int,
    x: Int,
    y: Int
  ): Image = {
    val imageHeight = 256
    val imageWidth = 256

    val pixels: Array[Pixel] = new Array[Pixel](imageHeight * imageWidth)

    def parPixels(xStart: Int, xEnd: Int, yStart: Int, yEnd: Int, pixels: Array[Pixel]) = {
      for {
        yPos <- yStart until yEnd
      } {
//        println("yPos=" + yPos.toString)
        for {
          xPos <- xStart until xEnd
        } {
          val location = Tile((xPos.toDouble / imageWidth) + x, (yPos.toDouble / imageHeight) + y, zoom).location
          val lat0 = floor(location.lat).toInt
          val lon0 = floor(location.lon).toInt
          val lat1 = ceil(location.lat).toInt
          val lon1 = ceil(location.lon).toInt
          val (d00, d01, d10, d11) = parallel(
              grid(lat0, lon0),
              grid(lat0, lon1),
              grid(lat1, lon0),
              grid(lat1, lon1))
          val deviation = bilinearInterpolation(location.lat - lat0.toDouble, location.lon - lon0.toDouble, d00, d01, d10, d11)
          val color = Visualization.interpolateColor(colors, deviation)
          pixels(xPos + (yPos * (xEnd - xStart))) = Pixel(color.red, color.green, color.blue, 127)
        }
      }
    }

    val ySeg = imageHeight / 4
    parallel(
      parPixels(0, imageWidth, 0*ySeg, 1*ySeg, pixels),
      parPixels(0, imageWidth, 1*ySeg, 2*ySeg, pixels),
      parPixels(0, imageWidth, 2*ySeg, 3*ySeg, pixels),
      parPixels(0, imageWidth, 3*ySeg, 4*ySeg, pixels)
    )

    Image(imageWidth, imageHeight, pixels)

  }

}
