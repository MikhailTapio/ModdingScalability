package committee.nova.scalability.implicits

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MathHelper
import net.minecraft.world.World

import scala.language.implicitConversions

package object minecraft {
  implicit class WorldImplicit(val world: World) {
    def runServerSide(c: World => Unit): Unit = if (!world.isRemote) c.apply(world)

    def runClientSide(c: World => Unit): Unit = if (world.isRemote) c.apply(world)
  }

  implicit class EntityPlayerImplicit(val player: EntityPlayer) {
    def isRemote: Boolean = player.worldObj.isRemote

    def runServerSide(c: EntityPlayer => Unit): Unit = if (!player.isRemote) c.apply(player)

    def runClientSide(c: EntityPlayer => Unit): Unit = if (player.isRemote) c.apply(player)

    def tickInt(tagName: String, int2Int: Int => Int): Unit = player.runServerSide(p => p.getEntityData.tickInt(tagName, int2Int))

    def tickInt(tagName: String, delta: Int, min: Int, max: Int): Unit = tickInt(tagName, i => MathHelper.clamp_int(i + delta, min, max))
  }

  implicit class NBTTagCompoundImplicit(val tag: NBTTagCompound) {
    def tickInt(tagName: String, int2Int: Int => Int): Unit = {
      val oldValue = tag.getInteger(tagName)
      val newValue = int2Int.apply(oldValue)
      if (oldValue != newValue) tag.setInteger(tagName, newValue)
    }

    def tickInt(tagName: String, delta: Int, min: Int, max: Int): Unit = tickInt(tagName, i => MathHelper.clamp_int(i + delta, min, max))
  }
}