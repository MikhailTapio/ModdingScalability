package committee.nova.scalability.api.item

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

trait IItemTickable {
  def tick(stack: ItemStack, player: EntityPlayer, slot: Int): Unit
}
