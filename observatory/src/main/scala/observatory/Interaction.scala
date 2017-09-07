package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import math._
import ParaCommon._
import Visualization._

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {
    Tile(x, y, zoom).location
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return A 256×256 image showing the contents of the tile defined by `x`, `y` and `zooms`
    */
  def tile(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {
    val imageHeight = 256
    val imageWidth = 256

    val pixels: Array[Pixel] = new Array[Pixel](imageHeight * imageWidth)

    def parPixels(xStart: Int, xEnd: Int, yStart: Int, yEnd: Int, pixels: Array[Pixel]) = {
      for {
        yPos <- yStart until yEnd
        xPos <- xStart until xEnd
      } {
        val location = Tile((xPos.toDouble / imageWidth) + x, (yPos.toDouble / imageHeight) + y, zoom).location
        val temperature = predictTemperature(temperatures, location)
        val color = interpolateColor(colors, temperature)
        pixels(xPos + (yPos * (xEnd - xStart))) = Pixel(color.red, color.green, color.blue, 127)
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

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Int, Data)],
    generateImage: (Int, Int, Int, Int, Data) => Unit
  ): Unit = {
    for {
      (year, data) <- yearlyData
      zoom <- 0 to 3
      x <- 0 until round(pow(2, zoom)).toInt
      y <- 0 until round(pow(2, zoom)).toInt
    } (generateImage(year, zoom, x, y, data))
  }

}
