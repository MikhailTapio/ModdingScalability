package committee.nova.scalability.patch

import committee.nova.scalability.implicits.base.DoubleImplicit
import net.minecraft.util.Vec3
import net.minecraftforge.common.util.ForgeDirection


object Vec3i {
  final val ZERO = Vec3i(0, 0, 0)

  def apply(x: Int, y: Int, z: Int): Vec3i = new Vec3i(x, y, z)

  def apply(x: Double, y: Double, z: Double): Vec3i = new Vec3i(x, y, z)
}

class Vec3i(private var x: Int, private var y: Int, private var z: Int) extends Comparable[Vec3i] {
  def this(x1: Double, y1: Double, z1: Double) = this(x1.floor2Int, y1.floor2Int, z1.floor2Int)

  def getX: Int = x

  def getY: Int = y

  def getZ: Int = z

  protected def setX(x: Int): Vec3i = {
    this.x = x
    this
  }

  protected def setY(y: Int): Vec3i = {
    this.y = y
    this
  }

  protected def setZ(z: Int): Vec3i = {
    this.z = z
    this
  }

  def add(dx: Double, dy: Double, dz: Double): Vec3i = if (dx == 0.0D && dy == 0.0D && dz == 0.0D) this else Vec3i(x.toDouble + dx, y.toDouble + dy, z.toDouble + dz)

  def add(dx: Int, dy: Int, dz: Int): Vec3i = if (dx == 0 && dy == 0 && dz == 0) this else Vec3i(x + dx, y + dy, z + dz)

  def add(another: Vec3i): Vec3i = add(another.x, another.y, another.z)

  def subtract(dx: Int, dy: Int, dz: Int): Vec3i = add(-dx, -dy, -dz)

  def subtract(another: Vec3i): Vec3i = subtract(another.x, another.y, another.z)

  def multiply(scale: Int): Vec3i = if (scale == 1) this else Vec3i(x * scale, y * scale, z * scale)

  def cross(vec: Vec3i): Vec3i = Vec3i(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x)

  def offset(direction: ForgeDirection, distance: Int): Vec3i = if (distance == 0) this
  else Vec3i(x + direction.offsetX * distance, y + direction.offsetY * distance, z + direction.offsetZ * distance)

  def offset(direction: ForgeDirection): Vec3i = offset(direction, 1)

  def down(distance: Int): Vec3i = offset(ForgeDirection.DOWN, distance)

  def down: Vec3i = down(1)

  def up(distance: Int): Vec3i = offset(ForgeDirection.UP, distance)

  def up: Vec3i = up(1)

  def north(distance: Int): Vec3i = offset(ForgeDirection.NORTH, distance)

  def north: Vec3i = north(1)

  def south(distance: Int): Vec3i = offset(ForgeDirection.SOUTH, distance)

  def south: Vec3i = south(1)

  def west(distance: Int): Vec3i = offset(ForgeDirection.WEST, distance)

  def west: Vec3i = west(1)

  def east(distance: Int): Vec3i = offset(ForgeDirection.EAST, distance)

  def east: Vec3i = east(1)

  def distanceToSqr(x1: Double, y1: Double, z1: Double): Double = (x - x1).sq + (y - y1).sq + (z - z1).sq

  def centerDistanceToSqr(x1: Double, y1: Double, z1: Double): Double = distanceToSqr(x1 - 0.5D, y1 - 0.5D, z1 - 0.5D)

  def distanceToSqr(x1: Int, y1: Int, z1: Int): Double = distanceToSqr(x1.toDouble, y1.toDouble, z1.toDouble)

  def distanceToSqr(another: Vec3i): Double = distanceToSqr(another.x, another.y, another.z)

  def distanceToSqr(vec3: Vec3): Double = centerDistanceToSqr(vec3.xCoord, vec3.yCoord, vec3.zCoord)

  def isWithinDistance(x1: Double, y1: Double, z1: Double, distance: Double): Boolean = centerDistanceToSqr(x1, y1, z1) < distance.sq

  def isWithinDistance(x1: Int, y1: Int, z1: Int, distance: Double): Boolean = distanceToSqr(x1, y1, z1) < distance.sq

  def isWithinDistance(another: Vec3i, distance: Double): Boolean = isWithinDistance(another.x, another.y, another.z, distance)

  def isWithinDistance(vec3: Vec3, distance: Double): Boolean = isWithinDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord, distance)

  def manhattanDistance(x1: Int, y1: Int, z1: Int): Int = (x1 - x).abs + (y1 - y).abs + (z1 - z).abs

  def manhattanDistance(another: Vec3i): Int = manhattanDistance(another.x, another.y, another.z)

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[AnyRef]) return false
    val ref = obj.asInstanceOf[AnyRef]
    if (this eq ref) return true
    if (!ref.isInstanceOf[Vec3i]) return false
    val vec3i = ref.asInstanceOf[Vec3i]
    this.getX == vec3i.getX && this.getY == vec3i.getY && this.getZ == vec3i.getZ
  }

  override def hashCode(): Int = (this.getY + this.getZ * 31) * 31 + this.getX

  override def compareTo(that: Vec3i): Int = {
    if (this.getY == that.getY) if (this.getZ == that.getZ) this.getX - that.getX
    else this.getZ - that.getZ
    else this.getY - that.getY
  }
}
