package committee.nova.scalability.implicits

/**
 * The package provides some implicits to patch several base classes in Java/Scala
 */
package object base {
  implicit class DoubleImplicit(val double: Double) {
    def sq: Double = double * double

    def sqrt: Double = Math.sqrt(double)
  }
}
