package committee.nova.scalability.implicits

import committee.nova.scalability.implicits.functions._
import committee.nova.scalability.patch.{AxisAlignedBB, BlockPos, Vec3i}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.pathfinding.PathEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{MathHelper, Vec3, AxisAlignedBB => AABB}
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.{ChunkPosition, EnumSkyBlock, Explosion, World}
import net.minecraftforge.common.util.ForgeDirection

import java.util.{List => JList}
import scala.language.implicitConversions
import scala.util.Try

/**
 * The package provides some implicits to patch vanilla minecraft classes with extra functions
 */
package object minecraft {
  implicit class WorldImplicit(val world: World) {
    // TODO: Stuff about BlockPos 
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

    /**
     * BlockPos param version of getTopBlock
     *
     * @param pos The block pos
     * @return The top block based on the position's x && z
     * @see net.minecraft.world.World#getTopBlock(int x, int z)
     */
    def getTopBlock(pos: BlockPos): Block = world.getTopBlock(pos.getX, pos.getZ)

    def getBlock(pos: BlockPos): Block = world.getBlock(pos.getX, pos.getY, pos.getZ)

    def isAirBlock(pos: BlockPos): Boolean = world.isAirBlock(pos.getX, pos.getY, pos.getZ)

    def blockExists(pos: BlockPos): Boolean = world.blockExists(pos.getX, pos.getY, pos.getZ)

    def doChunksNearChunkExist(pos: BlockPos, range: Int): Boolean = world.doChunksNearChunkExist(pos.getX, pos.getY, pos.getZ, range)

    def checkChunksExist(pos1: BlockPos, pos2: BlockPos): Boolean = world.checkChunksExist(pos1.getX min pos2.getX, pos1.getY min pos2.getY, pos1.getZ min pos2.getZ,
      pos1.getX max pos2.getX, pos1.getY max pos2.getY, pos1.getZ max pos2.getZ)

    def getChunkFromBlockPos(pos: BlockPos): Chunk = world.getChunkFromBlockCoords(pos.getX, pos.getZ)

    def setBlock(pos: BlockPos, block: Block, metadata: Int, flag: Int): Boolean = world.setBlock(pos.getX, pos.getY, pos.getZ, block, metadata, flag)

    def setBlock(pos: BlockPos, block: Block): Boolean = world.setBlock(pos.getX, pos.getY, pos.getZ, block)

    def markAndNotifyBlock(pos: BlockPos, chunk: Chunk, oldBlock: Block, newBlock: Block, flag: Int): Unit = world.markAndNotifyBlock(pos.getX, pos.getY, pos.getZ,
      chunk, oldBlock, newBlock, flag)

    def getBlockMetadata(pos: BlockPos): Int = world.getBlockMetadata(pos.getX, pos.getY, pos.getZ)

    def setBlockMetadataWithNotify(pos: BlockPos, metadata: Int, flag: Int): Boolean = world.setBlockMetadataWithNotify(pos.getX, pos.getY, pos.getZ, metadata, flag)

    def setBlockToAir(pos: BlockPos): Boolean = world.setBlockToAir(pos.getX, pos.getY, pos.getZ)

    def breakBlock(pos: BlockPos, shouldDrop: Boolean): Boolean = world.func_147480_a(pos.getX, pos.getY, pos.getZ, shouldDrop)

    def markBlockForUpdate(pos: BlockPos): Unit = world.markBlockForUpdate(pos.getX, pos.getY, pos.getZ)

    def notifyBlockChange(pos: BlockPos, block: Block): Unit = world.notifyBlockChange(pos.getX, pos.getY, pos.getZ, block)

    def notifyBlocksOfNeighborChange(pos: BlockPos, block: Block): Unit = world.notifyBlocksOfNeighborChange(pos.getX, pos.getY, pos.getZ, block)

    def notifyBlocksOfNeighborChange(pos: BlockPos, block: Block, exception: Int): Unit = world.notifyBlocksOfNeighborChange(pos.getX, pos.getY, pos.getZ, block, exception)

    def notifyBlockOfNeighborChange(pos: BlockPos, block: Block): Unit = world.notifyBlockOfNeighborChange(pos.getX, pos.getY, pos.getZ, block)

    def isBlockTickScheduledThisTick(pos: BlockPos, block: Block): Boolean = world.isBlockTickScheduledThisTick(pos.getX, pos.getY, pos.getZ, block)

    def canBlockSeeTheSky(pos: BlockPos): Boolean = world.canBlockSeeTheSky(pos.getX, pos.getY, pos.getZ)

    def getFullBlockLightValue(pos: BlockPos): Int = world.getFullBlockLightValue(pos.getX, pos.getY, pos.getZ)

    def getBlockLightValue(pos: BlockPos): Int = world.getBlockLightValue(pos.getX, pos.getY, pos.getZ)

    def getBlockLightValue_do(pos: BlockPos, isHalfStep: Boolean): Int = world.getBlockLightValue_do(pos.getX, pos.getY, pos.getZ, isHalfStep)

    def getHeightValue(pos: BlockPos): Int = world.getHeightValue(pos.getX, pos.getZ)

    def getChunkHeightMapMinimum(pos: BlockPos): Int = world.getChunkHeightMapMinimum(pos.getX, pos.getZ)

    @SideOnly(Side.CLIENT)
    def getSkyBlockTypeBrightness(skyBlock: EnumSkyBlock, pos: BlockPos): Int = world.getSkyBlockTypeBrightness(skyBlock, pos.getX, pos.getY, pos.getZ)

    def getSavedLightValue(skyBlock: EnumSkyBlock, pos: BlockPos): Int = world.getSavedLightValue(skyBlock, pos.getX, pos.getY, pos.getZ)

    def setLightValue(skyBlock: EnumSkyBlock, pos: BlockPos, lightValue: Int): Unit = world.setLightValue(skyBlock, pos.getX, pos.getY, pos.getZ, lightValue)

    @SideOnly(Side.CLIENT)
    def getLightBrightnessForSkyBlocks(pos: BlockPos, i: Int): Int = world.getLightBrightnessForSkyBlocks(pos.getX, pos.getY, pos.getZ, i)

    def getLightBrightness(pos: BlockPos): Float = world.getLightBrightness(pos.getX, pos.getY, pos.getZ)

    def playSoundAtBlock(pos: BlockPos, soundId: String, volume: Float, pitch: Float): Unit = world.playSoundEffect(pos.getX.toDouble + 0.5D, pos.getY.toDouble + 0.5D, pos.getZ.toDouble + 0.5D,
      soundId, volume, pitch)

    def playSoundEffect(vec3: Vec3, soundId: String, volume: Float, pitch: Float): Unit = world.playSoundEffect(vec3.xCoord, vec3.yCoord, vec3.zCoord,
      soundId, volume, pitch)

    /**
     * Play a sound in the client world
     *
     * @param vec3    The position the sound will be played
     * @param soundId The id of the sound
     * @param volume  The volume of the sound
     * @param pitch   The pitch of the sound
     * @param delay   Whether a distant sound should be delayed
     */
    def playSound(vec3: Vec3, soundId: String, volume: Float, pitch: Float, delay: Boolean): Unit = world.playSound(vec3.xCoord, vec3.yCoord, vec3.zCoord,
      soundId, volume, pitch, delay)

    def playRecord(soundId: String, vec3i: Vec3i): Unit = world.playRecord(soundId, vec3i.getX, vec3i.getY, vec3i.getZ)

    def spawnParticle(particleName: String, pos: Vec3, vel: Vec3): Unit = world.spawnParticle(particleName, pos.xCoord, pos.yCoord, pos.zCoord, vel.xCoord, vel.yCoord, vel.zCoord)

    def getPrecipitationHeight(pos: BlockPos): Int = world.getPrecipitationHeight(pos.getX, pos.getZ)

    def getTopSolidOrLiquidBlock(pos: BlockPos): Int = world.getTopSolidOrLiquidBlock(pos.getX, pos.getZ)

    def newExplosion(entity: Entity, pos: Vec3, strength: Float, isFlaming: Boolean, isSmoking: Boolean): Explosion = world.newExplosion(entity,
      pos.xCoord, pos.yCoord, pos.zCoord, strength, isFlaming, isSmoking)

    def createExplosion(entity: Entity, pos: Vec3, strength: Float, isSmoking: Boolean): Explosion = newExplosion(entity, pos, strength,
      isFlaming = false, isSmoking)

    def extinguishFire(player: EntityPlayer, pos: BlockPos, direction: ForgeDirection): Boolean = world.extinguishFire(player,
      pos.getX, pos.getY, pos.getZ, direction.ordinal())

    def getTileEntity(pos: BlockPos): Option[TileEntity] = Option.apply(world.getTileEntity(pos.getX, pos.getY, pos.getZ))

    def setTileEntity(pos: BlockPos, tile: TileEntity): Unit = world.setTileEntity(pos.getX, pos.getY, pos.getZ, tile)

    def removeTileEntity(pos: BlockPos): Unit = world.removeTileEntity(pos.getX, pos.getY, pos.getZ)

    def isBlockFullCube(pos: BlockPos): Boolean = world.func_147469_q(pos.getX, pos.getY, pos.getZ)

    def isisBlockNormalCubeDefault(pos: BlockPos, default: Boolean): Boolean = world.isBlockNormalCubeDefault(pos.getX, pos.getY, pos.getZ, default)

    def canBlockFreeze(pos: BlockPos, byWater: Boolean): Boolean = world.canBlockFreeze(pos.getX, pos.getY, pos.getZ, byWater)

    def isBlockFreezable(pos: BlockPos): Boolean = canBlockFreeze(pos, byWater = false)

    def isBlockFreezableNaturally(pos: BlockPos): Boolean = canBlockFreeze(pos, byWater = true)

    def canBlockFreezeBody(pos: BlockPos, byWater: Boolean): Boolean = world.canBlockFreezeBody(pos.getX, pos.getY, pos.getZ, byWater)

    def canSnowAt(pos: BlockPos, checkLight: Boolean): Boolean = world.func_147478_e(pos.getX, pos.getY, pos.getZ, checkLight)

    def canSnowAtBody(pos: BlockPos, checkLight: Boolean): Boolean = world.canSnowAtBody(pos.getX, pos.getY, pos.getZ, checkLight)

    def updateAllLightTypes(pos: BlockPos): Boolean = world.func_147451_t(pos.getX, pos.getY, pos.getZ)

    def updateLightByType(skyBlock: EnumSkyBlock, pos: BlockPos): Boolean = world.updateLightByType(skyBlock, pos.getX, pos.getY, pos.getZ)

    def getEntitiesWithinAABB[T <: Entity](entityClass: Class[T], aabb: AABB): JList[T] = selectEntitiesWithinAABB(entityClass, aabb, null)

    def selectEntitiesWithinAABB[T <: Entity](entityClass: Class[T], aabb: AABB, selector: IEntitySelector): JList[T] =
      world.selectEntitiesWithinAABB(entityClass, aabb, selector).asInstanceOf[JList[T]]

    def findNearestEntityWithinAABB[T <: Entity](entityClass: Class[T], aabb: AABB, entity: Entity): Option[T] =
      Try(world.findNearestEntityWithinAABB(entityClass, aabb, entity).asInstanceOf[T]).toOption

    def canPlaceEntityOnSide(block: Block, pos: BlockPos, blockIgnored: Boolean, param: Int, entity: Entity, stack: ItemStack): Boolean =
      world.canPlaceEntityOnSide(block, pos.getX, pos.getY, pos.getZ, blockIgnored, param, entity, stack)

    def getEntityPathToXYZ
    (entity: Entity, pos: BlockPos, range: Float, canOpenDoor: Boolean, isMovementBlockAllowed: Boolean, pathInWater: Boolean, canDrown: Boolean): PathEntity = {
      world.getEntityPathToXYZ(entity, pos.getX, pos.getY, pos.getZ, range, canOpenDoor, isMovementBlockAllowed, pathInWater, canDrown)
    }

    def isBlockProvidingPowerTo(pos: BlockPos, direction: ForgeDirection): Int = world.isBlockProvidingPowerTo(pos.getX, pos.getY, pos.getZ, direction.ordinal())

    def getBlockPowerInput(pos: BlockPos): Int = world.getBlockPowerInput(pos.getX, pos.getY, pos.getZ)

    def getIndirectPowerOutput(pos: BlockPos, direction: ForgeDirection): Boolean = world.getIndirectPowerOutput(pos.getX, pos.getY, pos.getZ, direction.ordinal())

    def getIndirectPowerLevelTo(pos: BlockPos, direction: ForgeDirection): Int = world.getIndirectPowerLevelTo(pos.getX, pos.getY, pos.getZ, direction.ordinal())

    def isBlockIndirectlyGettingPowered(pos: BlockPos): Boolean = world.isBlockIndirectlyGettingPowered(pos.getX, pos.getY, pos.getZ)

    def getStrongestIndirectPower(pos: BlockPos): Int = world.getStrongestIndirectPower(pos.getX, pos.getY, pos.getZ)

    def getClosestPlayer(vec3: Vec3, distance: Double): Option[EntityPlayer] =
      Option.apply(world.getClosestPlayer(vec3.xCoord, vec3.yCoord, vec3.zCoord, distance))

    def getClosestVulnerablePlayer(vec3: Vec3, distance: Double): Option[EntityPlayer] =
      Option.apply(world.getClosestVulnerablePlayer(vec3.xCoord, vec3.yCoord, vec3.zCoord, distance))

    def setSpawnLocation(pos: BlockPos): Unit = world.setSpawnLocation(pos.getX, pos.getY, pos.getZ)

    def canMineBlock(player: EntityPlayer, pos: BlockPos): Boolean = world.canMineBlock(player, pos.getX, pos.getY, pos.getZ)

    def canMineBlockBody(player: EntityPlayer, pos: BlockPos): Boolean = world.canMineBlockBody(player, pos.getX, pos.getY, pos.getZ)

    def addBlockEvent(pos: BlockPos, block: Block, eventId: Int, eventParam: Int): Unit =
      world.addBlockEvent(pos.getX, pos.getY, pos.getZ, block, eventId, eventParam)

    def canLightningStrikeAt(pos: BlockPos): Boolean = world.canLightningStrikeAt(pos.getX, pos.getY, pos.getZ)

    def isBlockHighHumidity(pos: BlockPos): Boolean = world.isBlockHighHumidity(pos.getX, pos.getY, pos.getZ)

    def playAuxSFX(eventType: Int, pos: BlockPos, eventData: Int): Unit = world.playAuxSFX(eventType, pos.getX, pos.getY, pos.getZ, eventData)

    def playAuxSFXAtEntity(player: EntityPlayer, eventType: Int, pos: BlockPos, eventData: Int): Unit =
      world.playAuxSFXAtEntity(player, eventType, pos.getX, pos.getY, pos.getZ, eventData)

    def findClosestStructure(structureId: String, pos: BlockPos): Option[ChunkPosition] =
      Option.apply(world.findClosestStructure(structureId, pos.getX, pos.getY, pos.getZ))

    def destroyBlockInWorldPartially(entityId: Int, pos: BlockPos, progress: Int): Unit =
      world.destroyBlockInWorldPartially(entityId, pos.getX, pos.getY, pos.getZ, progress)

    @SideOnly(Side.CLIENT)
    def makeFireworks(pos: Vec3, vel: Vec3, tag: NBTTagCompound): Unit = world.makeFireworks(pos.xCoord, pos.yCoord, pos.zCoord, vel.xCoord, vel.yCoord, vel.zCoord, tag)

    def updateNeighborsAboutBlockChange(pos: BlockPos, block: Block): Unit = world.func_147453_f(pos.getX, pos.getY, pos.getZ, block)

    def getTensionFactorForBlock(pos: Vec3): Float = world.func_147462_b(pos.xCoord, pos.yCoord, pos.zCoord)

    def getTensionFactorForBlock(pos: BlockPos): Float = world.func_147473_B(pos.getX, pos.getY, pos.getZ)

    def isSideSolid(pos: BlockPos, side: ForgeDirection): Boolean = world.isSideSolid(pos.getX, pos.getY, pos.getZ, side)

    def isSideSolid(pos: BlockPos, side: ForgeDirection, default: Boolean): Boolean = world.isSideSolid(pos.getX, pos.getY, pos.getZ, side, default)

    def getBlockLightOpacity(pos: BlockPos): Int = world.getBlockLightOpacity(pos.getX, pos.getY, pos.getZ)


  }

  implicit class EntityLivingBaseImplicit(val living: EntityLivingBase) {
    /**
     * Check the amount of mobs targeting at the player
     * If ran on the client-side, it will consistently return 0
     *
     * @param aabb The mobs in such AABB should be checked if their target is the player
     * @return How many mobs in the AABB are targeting at the player
     */
    def isTargetedBy(aabb: AABB): Int = {
      val raw = living.worldObj.getEntitiesWithinAABB(classOf[EntityMob], aabb)
      if (raw.isEmpty) return 0
      val list = raw.asInstanceOf[java.util.List[EntityMob]]
      list.removeIf((e: EntityMob) => living != e.getAttackTarget)
      list.size
    }

    /**
     * Check the amount of mobs targeting at the player
     * If ran on the client-side, it will consistently return 0
     *
     * @param range Half the length of the AABB's side
     * @return How many mobs in the AABB are targeting at the player
     * @see EntityLivingBaseImplicit#isTargetedBy(aabb: AABB)
     */
    def isTargetedBy(range: Int): Int = {
      val center = living.getPosition(1.0F)
      isTargetedBy(AxisAlignedBB(center.addVector(range, range, range), center.addVector(-range, -range, -range)))
    }
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

  implicit class Vec3Implicit(val vec3: Vec3) {
    def atCenterOf(vec3i: Vec3i): Vec3 = new Vec3(vec3i.getX.toDouble + 0.5D, vec3i.getY.toDouble + 0.5D, vec3i.getZ.toDouble + 0.5D)

    def atLowerCornerOf(vec3i: Vec3i): Vec3 = new Vec3(vec3i.getX.toDouble, vec3i.getY.toDouble, vec3i.getZ.toDouble)

    def atBottomCenterOf(vec3i: Vec3i): Vec3 = new Vec3(vec3i.getX.toDouble + 0.5D, vec3i.getY.toDouble, vec3i.getZ.toDouble + 0.5D)

    def upFromBottomCenterOf(vec3i: Vec3i, offset: Double) = new Vec3(vec3i.getX.toDouble + 0.5D, vec3i.getY.toDouble + offset, vec3i.getZ.toDouble + 0.5D)
  }
}