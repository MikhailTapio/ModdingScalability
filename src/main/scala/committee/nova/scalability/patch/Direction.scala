package committee.nova.scalability.patch

import committee.nova.scalability.patch.Directions._

sealed abstract class Direction
(val id: Int, val idOpposite: Int, val idHorizontal: Int, val name: String, val vec: Vec3i) {
  final val VALUES = Array(DOWN, UP, NORTH, SOUTH, WEST, EAST)

  def getOffsetX: Int = this.vec.getX

  def getOffsetY: Int = this.vec.getY

  def getOffsetZ: Int = this.vec.getZ

  def byId(id: Int): Direction = VALUES((id % VALUES.length).abs)

  def getOpposite: Direction = byId(this.idOpposite)

  override def toString: String = this.name
}

object Directions {
  case object DOWN extends Direction(0, 1, -1, "down", Vec3i(0, -1, 0))

  case object UP extends Direction(1, 0, -1, "up", Vec3i(0, 1, 0))

  case object NORTH extends Direction(2, 3, 2, "north", Vec3i(0, 0, -1))

  case object SOUTH extends Direction(3, 2, 0, "south", Vec3i(0, 0, 1))

  case object WEST extends Direction(4, 5, 1, "west", Vec3i(-1, 0, 0))

  case object EAST extends Direction(5, 4, 3, "east", Vec3i(1, 0, 0))
}
