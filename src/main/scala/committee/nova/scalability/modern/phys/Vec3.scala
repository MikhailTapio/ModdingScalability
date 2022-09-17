package committee.nova.scalability.modern.phys

import committee.nova.scalability.implicits.base.DoubleImplicit

import java.lang.{Double => JDouble}

class Vec3(val x: Double, val y: Double, val z: Double) {
  def add(dx: Double, dy: Double, dz: Double): Vec3 = new Vec3(x + dx, y + dy, z + dz)

  def add(another: Vec3): Vec3 = add(another.x, another.y, another.z)

  def subtract(dx: Double, dy: Double, dz: Double): Vec3 = add(-dx, -dy, -dz)

  def subtract(another: Vec3): Vec3 = subtract(another.x, another.y, another.z)

  def dot(px: Double, py: Double, pz: Double): Double = x * px + y * py + z * pz

  def dot(another: Vec3): Double = dot(another.x, another.y, another.z)

  def cross(cx: Double, cy: Double, cz: Double): Vec3 = new Vec3(y * cz - z * cy, z * cx - x * cz, x * cy - y * cx)

  def cross(another: Vec3): Vec3 = cross(another.x, another.y, another.z)

  def lengthSqr: Double = x.sq + y.sq + z.sq

  def length: Double = lengthSqr.sqrt

  def distanceToSqr(ax: Double, ay: Double, az: Double): Double = (x - ax).sq + (y - ay).sq + (z - az).sq

  def distanceToSqr(another: Vec3): Double = distanceToSqr(another.x, another.y, another.z)

  def distanceTo(ax: Double, ay: Double, az: Double): Double = distanceToSqr(ax, ay, az).sqrt

  def distanceTo(another: Vec3): Double = distanceTo(another.x, another.y, another.z)

  def multiply(mx: Double, my: Double, mz: Double): Vec3 = new Vec3(x * mx, y * my, z * mz)

  def multiply(another: Vec3): Vec3 = multiply(another.x, another.y, another.z)

  def scale(scale: Double): Vec3 = multiply(scale, scale, scale)

  def reverse: Vec3 = scale(-1.0D)

  override def equals(obj: Any): Boolean = {
    obj match {
      case ref: AnyRef =>
        if (this eq ref) return true
        if (!ref.isInstanceOf[Vec3]) return false
        val vec3 = ref.asInstanceOf[Vec3]
        if (JDouble.compare(vec3.x, this.x) != 0) false
        if (JDouble.compare(vec3.y, this.y) != 0) false
        JDouble.compare(vec3.z, this.z) == 0
      case _ => false
    }
  }

  override def hashCode(): Int = {
    var j = JDouble.doubleToLongBits(this.x)
    var i = (j ^ j >>> 32).toInt
    j = JDouble.doubleToLongBits(this.y)
    i = 31 * i + (j ^ j >>> 32).toInt
    j = JDouble.doubleToLongBits(this.z)
    31 * i + (j ^ j >>> 32).toInt
  }

  override def toString: String = "(" + this.x + ", " + this.y + ", " + this.z + ")"
}
