package committee.nova.scalability.implicits

package object base {
  implicit class DoubleImplicit(val double: Double) {
    def sq: Double = double * double

    def sqrt: Double = Math.sqrt(double)
  }
}
