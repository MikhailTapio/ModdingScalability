package committee.nova.scalability.patch

object MathHelper {
  private val MULTIPLY_DE_BRUIJN_BIT_POSITION = Array[Int](0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9)

  def floorLog2(value: Int): Int = ceilLog2(value) - (if (isPowerOfTwo(value)) 0 else 1)

  def ceilLog2(value: Int): Int = {
    val v2 = if (isPowerOfTwo(value)) value else smallestEncompassingPowerOfTwo(value)
    MULTIPLY_DE_BRUIJN_BIT_POSITION((v2.toLong * 125613361L >> 27).toInt & 31)
  }

  def smallestEncompassingPowerOfTwo(value: Int): Int = {
    var i = value - 1
    i |= i >> 1
    i |= i >> 2
    i |= i >> 4
    i |= i >> 8
    i |= i >> 16
    i + 1
  }

  def isPowerOfTwo(value: Int): Boolean = value != 0 && (value & value - 1) == 0
}
