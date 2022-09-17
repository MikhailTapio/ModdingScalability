package committee.nova.scalability.implicits

/**
 * The package provides some implicits to patch several base classes in Java/Scala
 */
package object base {
  implicit class DoubleImplicit(val double: Double) {
    def sq: Double = double * double

    def sqrt: Double = Math.sqrt(double)

    def floor2Int: Int = {
      val i = double.toInt
      if (double < i.toDouble) i - 1 else i
    }
  }
}
