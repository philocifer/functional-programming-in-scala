package observatory

import java.time.LocalDate

import org.apache.spark.sql._
import org.apache.spark.sql.types._
import java.nio.file.Paths

import org.apache.log4j.{Level, Logger}

/**
  * 1st milestone: data extraction
  */
object Extraction {

//  System.setProperty("hadoop.home.dir", "c:\\winutil\\")
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  import org.apache.spark.sql.SparkSession
  import org.apache.spark.sql.functions._

  val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Observatory")
      .master("local[6]")
//      .config("spark.executor.heartbeatInterval", "100s")
      .getOrCreate()

  import spark.implicits._


  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    **/
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {

    def fsPath(resource: String): String =
      Paths.get(getClass.getResource(resource).toURI).toString

    val temperatureDF = spark.read.csv(fsPath(temperaturesFile))
                            .select(
                              concat_ws("~", coalesce($"_c0", lit("")), $"_c1").alias("id"),
                              $"_c2".alias("month").cast(IntegerType),
                              $"_c3".alias("day").cast(IntegerType),
                              $"_c4".alias("temperature").cast(DoubleType)
                            )

    val stationsDF = spark.read.csv(fsPath(stationsFile))
      .select(
        concat_ws("~", coalesce($"_c0", lit("")), $"_c1").alias("id"),
        $"_c2".alias("latitude").cast(DoubleType),
        $"_c3".alias("longitude").cast(DoubleType)
      )
      .where($"_c2".isNotNull && $"_c3".isNotNull && $"_c2" =!= 0 && $"_c3" =!= 0)

    val joinedDF = stationsDF.join(temperatureDF, usingColumn = "id")

    joinedDF.rdd.map {
      case Row(id: String, latitude: Double, longitude: Double, month: Integer, day: Integer, temperature: Double) =>
        (LocalDate.of(year, month, day), Location(latitude, longitude), (temperature - 32) * 5 / 9)
    }.collect().toSeq
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    def temperatureSchema: StructType = {
      val latitude = StructField("latitude", DoubleType, true)
      val longitude = StructField("longitude", DoubleType, true)
      val temperature = StructField("temperature", DoubleType, false)
      StructType(List(latitude, longitude, temperature))
    }
    def temperatureRow(record: (LocalDate, Location, Double)): Row = {
      val rowVals = List(record._2.lat, record._2.lon, record._3)
      Row.fromSeq(rowVals)
    }
    val temperatureRDD = spark.sparkContext.parallelize(records.toSeq)
    val temperatureData = temperatureRDD.map(temperatureRow)
    val temperatureDF = spark.createDataFrame(temperatureData, temperatureSchema)
    val avgTemperatureDF = temperatureDF.groupBy($"latitude", $"longitude").agg(avg($"temperature"))

    avgTemperatureDF.collect().par.map{
      case Row(latitude: Double, longitude: Double, temperature: Double) => (Location(latitude, longitude), temperature)
    }.seq

  }

}
