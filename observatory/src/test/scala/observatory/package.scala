package object observatory {

  def roundAt(p: Int)(n: Double): Double = {
    val s = math.pow(10, p)
    math.round(n * s) / s
  }

  def referenceColors: List[(Double, Color)] = List(
    (60.0, Color(255,255,255)),
    (32.0, Color(255,0,0)),
    (12.0, Color(255,255,0)),
    (0.0, Color(0,255,255)),
    (-15.0, Color(0,0,255)),
    (-27.0, Color(255,0,255)),
    (-50.0, Color(33,0,107)),
    (-60.0, Color(0,0,0))
  )

  def deviationReferenceColors: List[(Double, Color)] = List(
    (7.0, Color(0,0,0)),
    (4.0, Color(255,0,0)),
    (2.0, Color(255,255,0)),
    (0.0, Color(255,255,255)),
    (-2.0, Color(0,255,255)),
    (-7.0, Color(0,0,255))
  )

}
