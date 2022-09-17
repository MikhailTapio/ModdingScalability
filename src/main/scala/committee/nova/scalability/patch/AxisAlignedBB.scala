package committee.nova.scalability.patch

import net.minecraft.util.{Vec3, AxisAlignedBB => AABB}

object AxisAlignedBB {
  def apply(c1: Vec3, c2: Vec3): AABB = AABB.getBoundingBox(
    c1.xCoord min c2.xCoord, c1.yCoord min c2.yCoord, c1.zCoord min c2.zCoord,
    c1.xCoord max c2.xCoord, c1.yCoord max c2.yCoord, c1.zCoord max c2.zCoord
  )
}
