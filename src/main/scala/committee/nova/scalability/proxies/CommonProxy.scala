package committee.nova.scalability.proxies

import committee.nova.scalability.event.{FMLEventManager, ForgeEventManager}
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}

class CommonProxy {
  def preInit(event: FMLPreInitializationEvent): Unit = {}

  def init(event: FMLInitializationEvent): Unit = {
    FMLEventManager.init()
    ForgeEventManager.init()
  }

  def postInit(event: FMLPostInitializationEvent): Unit = {}
}
