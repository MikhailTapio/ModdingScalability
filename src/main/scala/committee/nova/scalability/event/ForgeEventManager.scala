package committee.nova.scalability.event

import net.minecraftforge.common.MinecraftForge

object ForgeEventManager {
  def init(): Unit = MinecraftForge.EVENT_BUS.register(new ForgeEventManager)
}

class ForgeEventManager {

}
