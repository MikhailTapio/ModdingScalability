package committee.nova.scalability

import committee.nova.scalability.proxies.CommonProxy
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, SidedProxy}

@Mod(modid = Scalability.MODID, useMetadata = true, modLanguage = "scala", acceptedMinecraftVersions = "*")
object Scalability {
  final val MODID = "scalability"
  final val proxyPrefix = "committee.nova.scalability.proxies."

  @SidedProxy(serverSide = proxyPrefix + "CommonProxy", clientSide = proxyPrefix + "ClientProxy")
  var proxy: CommonProxy = _

  @EventHandler def preInit(e: FMLPreInitializationEvent): Unit = proxy.preInit(e)

  @EventHandler def init(e: FMLInitializationEvent): Unit = proxy.init(e)

  @EventHandler def postInit(e: FMLPostInitializationEvent): Unit = proxy.postInit(e)
}