package committee.nova.scalability.implicits

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MathHelper
import net.minecraft.world.World

import scala.language.implicitConversions

/**
 * The package provides some implicits to patch vanilla minecraft classes with extra functions
 */
package object minecraft {
  implicit class WorldImplicit(val world: World) {
    /**
     * Run a task serverside-only
     *
     * @param task The task to run
     */
    def runServerSide(task: World => Unit): Unit = if (!world.isRemote) task.apply(world)

    /**
     * Run a task clientside-only
     *
     * @param task The task to run
     */
    def runClientSide(task: World => Unit): Unit = if (world.isRemote) task.apply(world)
  }

  implicit class EntityPlayerImplicit(val player: EntityPlayer) {
    /**
     * @return Whether the player is client-side
     */
    def isRemote: Boolean = player.worldObj.isRemote

    /**
     * Run a task serverside-only
     *
     * @param task The task to run
     */
    def runServerSide(task: EntityPlayer => Unit): Unit = if (!player.isRemote) task.apply(player)

    /**
     * Run a task clientside-only
     *
     * @param task The task to run
     */
    def runClientSide(task: EntityPlayer => Unit): Unit = if (player.isRemote) task.apply(player)

    /**
     * Change an integer in the player's entity data
     *
     * @param tagName The name of the integer in the tag
     * @param int2Int The function to process the given int and return a new one to be add into the tag
     */
    def tickInt(tagName: String, int2Int: Int => Int): Unit = player.runServerSide(p => p.getEntityData.tickInt(tagName, int2Int))

    /**
     * Change an integer at a defined incrementation in the player's entity data
     *
     * @param tagName The name of the integer in the tag
     * @param delta   The incrementation
     * @param min     The minimum value can be return
     * @param max     The maximum value can be return
     */
    def tickInt(tagName: String, delta: Int, min: Int, max: Int): Unit = tickInt(tagName, i => MathHelper.clamp_int(i + delta, min, max))
  }

  implicit class ItemStackImplicit(val stack: ItemStack) {
    /**
     * Return the stack's nbt tag;
     * If it's null, set it to be an new one and return
     *
     * @return The nbt tag
     */
    def getOrCreateTag: NBTTagCompound = {
      if (stack.getTagCompound == null) stack.setTagCompound(new NBTTagCompound)
      stack.getTagCompound
    }

    /**
     * Change an integer in the stack's nbt tag
     *
     * @param tagName The name of the integer in the tag
     * @param int2Int The function to process the given int and return a new one to be add into the tag
     */
    def tickInt(tagName: String, int2Int: Int => Int): Unit = stack.getOrCreateTag.tickInt(tagName, int2Int)

    /**
     * Change an integer at a defined incrementation in the stack's nbt tag
     *
     * @param tagName The name of the integer in the tag
     * @param delta   The incrementation
     * @param min     The minimum value can be return
     * @param max     The maximum value can be return
     */
    def tickInt(tagName: String, delta: Int, min: Int, max: Int): Unit = tickInt(tagName, i => MathHelper.clamp_int(i + delta, min, max))
  }

  implicit class NBTTagCompoundImplicit(val tag: NBTTagCompound) {
    /**
     * Change an integer in the tag
     *
     * @param tagName The name of the integer in the tag
     * @param int2Int The function to process the given int and return a new one to be add into the tag
     */
    def tickInt(tagName: String, int2Int: Int => Int): Unit = {
      val oldValue = tag.getInteger(tagName)
      val newValue = int2Int.apply(oldValue)
      if (oldValue != newValue) tag.setInteger(tagName, newValue)
    }

    /**
     * Change an integer in the tag
     *
     * @param tagName The name of the integer in the tag
     * @param delta   The incrementation
     * @param min     The minimum value can be return
     * @param max     The maximum value can be return
     */
    def tickInt(tagName: String, delta: Int, min: Int, max: Int): Unit = tickInt(tagName, i => MathHelper.clamp_int(i + delta, min, max))
  }
}