package committee.nova.scalability.event

import committee.nova.scalability.api.item.IItemTickable
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent.{Phase, PlayerTickEvent}

import scala.util.control.Breaks.{break, breakable}

object FMLEventManager {
  def init(): Unit = FMLCommonHandler.instance().bus().register(new FMLEventManager)
}

class FMLEventManager {
  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent): Unit = {
    if (event.phase != Phase.END) return
    val player = event.player
    val inventory = player.inventory.mainInventory
    for (index <- inventory.indices) {
      breakable {
        val stack = inventory(index)
        if (stack == null) break()
        stack.getItem match {
          case tickable: IItemTickable => tickable.tick(stack, player, index)
          case _ =>
        }
      }
    }
  }
}
