package committee.nova.scalability.patch

import com.google.common.collect.AbstractIterator
import committee.nova.scalability.implicits.base.DoubleImplicit
import committee.nova.scalability.implicits.functions._
import committee.nova.scalability.patch.BlockPos._
import committee.nova.scalability.patch.{MathHelper => MthPatch}
import net.minecraft.util.Vec3
import net.minecraftforge.common.util.ForgeDirection

import java.lang.{Iterable => JIterable}
import java.util.Random
import scala.collection.JavaConversions._

object BlockPos {
  final val ORIGIN: BlockPos = BlockPos(0, 0, 0)
  final val SIZE_BITS_X: Int = 1 + MthPatch.floorLog2(MthPatch.smallestEncompassingPowerOfTwo(30000000))
  final val SIZE_BITS_Z: Int = {
    SIZE_BITS_X
  }
  final val SIZE_BITS_Y: Int = {
    64 - SIZE_BITS_X - SIZE_BITS_Z
  }
  final val BITS_X = {
    (1L << SIZE_BITS_X) - 1L
  }
  final val BITS_Y = {
    (1L << SIZE_BITS_Y) - 1L
  }
  final val BITS_Z = {
    (1L << SIZE_BITS_Z) - 1L
  }
  final val BIT_SHIFT_Z = {
    SIZE_BITS_Y
  }
  final val BIT_SHIFT_X = {
    SIZE_BITS_Y + SIZE_BITS_Z
  }

  def apply(x: Int, y: Int, z: Int): BlockPos = new BlockPos(x, y, z)

  def apply(x: Double, y: Double, z: Double): BlockPos = new BlockPos(x, y, z)

  def apply(vec3i: Vec3i): BlockPos = new BlockPos(vec3i)

  def apply(vec3: Vec3): BlockPos = new BlockPos(vec3)
}

class BlockPos(x: Int, y: Int, z: Int) extends Vec3i(x, y, z) {
  def this(x: Double, y: Double, z: Double) = this(x.floor2Int, y.floor2Int, z.floor2Int)

  def this(vec3i: Vec3i) = this(vec3i.getX, vec3i.getY, vec3i.getZ)

  def this(vec3: Vec3) = this(vec3.xCoord, vec3.yCoord, vec3.zCoord)

  def unpackLongX(packedPos: Long): Int = (packedPos << 64 - BIT_SHIFT_X - SIZE_BITS_X >> 64 - SIZE_BITS_X).toInt

  def unpackLongY(packedPos: Long): Int = (packedPos << 64 - SIZE_BITS_Y >> 64 - SIZE_BITS_Y).toInt

  def unpackLongZ(packedPos: Long): Int = (packedPos << 64 - BIT_SHIFT_Z - SIZE_BITS_Z >> 64 - SIZE_BITS_Z).toInt

  def fromLong(packedPos: Long): BlockPos = BlockPos(unpackLongX(packedPos), unpackLongY(packedPos), unpackLongZ(packedPos))

  def asLong: Long = asLong(this.getX, this.getY, this.getZ)

  def asLong(x: Int, y: Int, z: Int): Long = {
    var l = 0L
    l |= (x.toLong & BITS_X) << BIT_SHIFT_X
    l |= (y.toLong & BITS_Y) << 0
    l |= (z.toLong & BITS_Z) << BIT_SHIFT_Z
    l
  }

  def removeChunkSectionLocalY(y: Long): Long = y & -16L

  override def add(i: Int, j: Int, k: Int): BlockPos = if (i == 0 && j == 0 && k == 0) this
  else BlockPos(this.getX + i, this.getY + j, this.getZ + k)

  override def add(d: Double, e: Double, f: Double): BlockPos = if (d == 0.0D && e == 0.0D && f == 0.0D) this
  else BlockPos(this.getX.toDouble + d, this.getY.toDouble + e, this.getZ.toDouble + f)

  override def add(vec3i: Vec3i): BlockPos = this.add(vec3i.getX, vec3i.getY, vec3i.getZ)

  override def subtract(vec3i: Vec3i): BlockPos = this.add(-vec3i.getX, -vec3i.getY, -vec3i.getZ)

  override def multiply(i: Int): BlockPos = if (i == 1) this
  else if (i == 0) ORIGIN
  else BlockPos(this.getX * i, this.getY * i, this.getZ * i)

  override def cross(pos: Vec3i): BlockPos = BlockPos(y * pos.getZ - z * pos.getY, z * pos.getX - x * pos.getZ, x * pos.getY - y * pos.getX)

  def offset(value: Long, direction: ForgeDirection): Long = add(value, direction.offsetX, direction.offsetY, direction.offsetZ)

  override def offset(direction: ForgeDirection): BlockPos = BlockPos(this.getX + direction.offsetX, this.getY + direction.offsetY, this.getZ + direction.offsetZ)

  override def offset(direction: ForgeDirection, i: Int): BlockPos = if (i == 0) this
  else BlockPos(this.getX + direction.offsetX * i, this.getY + direction.offsetY * i, this.getZ + direction.offsetZ * i)

  def add(value: Long, x: Int, y: Int, z: Int): Long = asLong(unpackLongX(value) + x, unpackLongY(value) + y, unpackLongZ(value) + z)

  override def up: BlockPos = offset(ForgeDirection.UP)

  override def up(distance: Int): BlockPos = offset(ForgeDirection.UP, distance)

  override def down: BlockPos = offset(ForgeDirection.DOWN)

  override def down(i: Int): BlockPos = offset(ForgeDirection.DOWN, i)

  override def north: BlockPos = offset(ForgeDirection.NORTH)

  override def north(distance: Int): BlockPos = offset(ForgeDirection.NORTH, distance)

  override def south: BlockPos = offset(ForgeDirection.SOUTH)

  override def south(distance: Int): BlockPos = offset(ForgeDirection.SOUTH, distance)

  override def west: BlockPos = offset(ForgeDirection.WEST)

  override def west(distance: Int): BlockPos = offset(ForgeDirection.WEST, distance)

  override def east: BlockPos = offset(ForgeDirection.EAST)

  override def east(distance: Int): BlockPos = offset(ForgeDirection.EAST, distance)

  def withY(y: Int): BlockPos = BlockPos(this.getX, y, this.getZ)

  def toImmutable: BlockPos = this

  def toMutable = new BlockPos.this.Mutable(this.getX, this.getY, this.getZ)

  def randomInCube(random: Random, count: Int, around: BlockPos, range: Int): JIterable[BlockPos] = randomBetweenClosed(random,
    count, around.getX - range, around.getY - range, around.getZ - range, around.getX + range, around.getY + range, around.getZ + range)

  def randomBetweenClosed(random: Random, count: Int, minX: Int, minY: Int, minZ: Int, maxX: Int, maxY: Int, maxZ: Int): JIterable[BlockPos] = {
    val i = maxX - minX + 1
    val j = maxY - minY + 1
    val k = maxZ - minZ + 1
    () => {
      new AbstractIterator[BlockPos]() {
        final val pos = new BlockPos.this.Mutable
        var remaining: Int = count

        override protected def computeNext: BlockPos = if (this.remaining <= 0) this.endOfData
        else {
          val blockPos = this.pos.set(minX + random.nextInt(i), minY + random.nextInt(j), minZ + random.nextInt(k))
          this.remaining -= 1
          blockPos
        }
      }
    }
  }

  def withinManhattan(pos: BlockPos, x: Int, y: Int, z: Int): JIterable[BlockPos] = {
    val i = x + y + z
    val j = pos.getX
    val k = pos.getY
    val l = pos.getZ
    () => {
      new AbstractIterator[BlockPos]() {
        final private val cursor = new Mutable
        private var currentDepth = 0
        private var maxX = 0
        private var maxY = 0
        private var x = 0
        private var y = 0
        private var zMirror = false

        override protected def computeNext: BlockPos = if (this.zMirror) {
          this.zMirror = false
          this.cursor.setZ(l - (this.cursor.getZ - l))
          this.cursor
        }
        else {
          var blockPos: BlockPos = null
          while ( {
            blockPos == null
          }) {
            if (this.y > this.maxY) {
              this.x += 1
              if (this.x > this.maxX) {
                this.currentDepth += 1
                if (this.currentDepth > i) return this.endOfData
                this.maxX = Math.min(x, this.currentDepth)
                this.x = -this.maxX
              }
              this.maxY = Math.min(y, this.currentDepth - Math.abs(this.x))
              this.y = -this.maxY
            }
            val i1 = this.x
            val j1 = this.y
            val k1 = this.currentDepth - Math.abs(i1) - Math.abs(j1)
            if (k1 <= z) {
              this.zMirror = k1 != 0
              blockPos = this.cursor.set(j + i1, k + j1, l + k1)
            }

            this.y += 1
          }
          blockPos
        }
      }
    }
  }

  def findClosestMatch(pos: BlockPos, horizontalRange: Int, verticalRange: Int, condition: BlockPos => Boolean): Option[BlockPos] = {
    withinManhattan(pos, horizontalRange, verticalRange, horizontalRange).foreach(p => if (condition.apply(p)) return Some(p))
    None
  }

  def betweenClosed(start: BlockPos, end: BlockPos): JIterable[BlockPos] = betweenClosed(start.getX min end.getX, start.getY min end.getY, start.getZ min end.getZ,
    start.getX max end.getX, start.getY max end.getY, start.getZ max end.getZ)

  def betweenClosed(startX: Int, startY: Int, startZ: Int, endX: Int, endY: Int, endZ: Int): JIterable[BlockPos] = {
    val i = endX - startX + 1
    val j = endY - startY + 1
    val k = endZ - startZ + 1
    val l = i * j * k
    () => {
      new AbstractIterator[BlockPos]() {
        final private val cursor = new Mutable
        private var index = 0

        override protected def computeNext: BlockPos = if (this.index == l) this.endOfData
        else {
          val i1 = this.index % i
          val j1 = this.index / i
          val k1 = j1 % j
          val l1 = j1 / j
          this.index += 1
          this.cursor.set(startX + i1, startY + k1, startZ + l1)
        }
      }
    }
  }

  class Mutable(x: Int, y: Int, z: Int) extends BlockPos(x, y, z) {
    def this() = this(0, 0, 0)

    def this(x: Double, y: Double, z: Double) = this(x.floor2Int, y.floor2Int, z.floor2Int)

    override def toImmutable: BlockPos = BlockPos(this)

    override def add(d: Double, e: Double, f: Double): BlockPos = super.add(d, e, f).toImmutable

    override def add(i: Int, j: Int, k: Int): BlockPos = super.add(i, j, k).toImmutable

    override def multiply(i: Int): BlockPos = super.multiply(i).toImmutable

    override def offset(direction: ForgeDirection, i: Int): BlockPos = super.offset(direction, i).toImmutable

    def set(x: Int, y: Int, z: Int): BlockPos.this.Mutable = {
      this.setX(x)
      this.setY(y)
      this.setZ(z)
      this
    }

    def set(x: Double, y: Double, z: Double): BlockPos.this.Mutable = set(x.floor2Int, y.floor2Int, z.floor2Int)

    def set(pos: Vec3i): BlockPos.this.Mutable = set(pos.getX, pos.getY, pos.getZ)

    def set(pos: Long): BlockPos.this.Mutable = set(unpackLongX(pos), unpackLongY(pos), unpackLongZ(pos))

    def set(pos: Vec3i, direction: ForgeDirection): BlockPos.this.Mutable = set(pos.getX + direction.offsetX, pos.getY + direction.offsetY, pos.getZ + direction.offsetZ)

    def set(pos: Vec3i, x: Int, y: Int, z: Int): BlockPos.this.Mutable = set(pos.getX + x, pos.getY + y, pos.getZ + z)

    def set(vec1: Vec3i, vec2: Vec3i): BlockPos.this.Mutable = set(vec1.getX + vec2.getX, vec1.getY + vec2.getY, vec1.getZ + vec2.getZ)

    def move(direction: ForgeDirection): BlockPos.this.Mutable = move(direction, 1)

    def move(direction: ForgeDirection, distance: Int): BlockPos.this.Mutable = set(this.getX + direction.offsetX * distance, this.getY + direction.offsetY * distance, this.getZ + direction.offsetZ * distance)

    def move(dx: Int, dy: Int, dz: Int): BlockPos.this.Mutable = set(this.getX + dx, this.getY + dy, this.getZ + dz)

    def move(vec: Vec3i): BlockPos.this.Mutable = set(this.getX + vec.getX, this.getY + vec.getY, this.getZ + vec.getZ)

    override def setX(i: Int): BlockPos.this.Mutable = {
      super.setX(i)
      this
    }

    override def setY(i: Int): BlockPos.this.Mutable = {
      super.setY(i)
      this
    }

    override def setZ(i: Int): BlockPos.this.Mutable = {
      super.setZ(i)
      this
    }
  }
}
