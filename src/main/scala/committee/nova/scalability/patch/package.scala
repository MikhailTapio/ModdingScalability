package committee.nova.scalability

import committee.nova.scalability.utils.Conversions.{convertCharInVarArgs => convert}
import cpw.mods.fml.common.registry.GameRegistry.{addShapedRecipe => addShaped, addShapelessRecipe => addShapeless}
import net.minecraft.item.ItemStack
import net.minecraft.util.{ChatComponentText, ChatComponentTranslation, Vec3, AxisAlignedBB => AABB}

/**
 * The package provides some objects to provide extra functions for their same-name classes
 */
package object patch {
  object GameRegistry {
    def addRecipe(output: ItemStack, args: Any*): Unit = addShapedRecipe(output, args)

    def addShapedRecipe(output: ItemStack, args: Any*): Unit = addShaped(output, convert(args: _*).toSeq: _*)

    def addShapelessRecipe(output: ItemStack, args: Any*): Unit = addShapeless(output, convert(args: _*).toSeq: _*)
  }

  object IChatComponent {
    def literal(key: String): ChatComponentText = new ChatComponentText(key)

    def translatable(key: String, args: Any*): ChatComponentTranslation = new ChatComponentTranslation(key, args)
  }

  object AxisAlignedBB {
    def apply(c1: Vec3, c2: Vec3): AABB = AABB.getBoundingBox(
      math.min(c1.xCoord, c2.xCoord), math.min(c1.yCoord, c2.yCoord), math.min(c1.zCoord, c2.zCoord),
      math.max(c1.xCoord, c2.xCoord), math.max(c1.yCoord, c2.yCoord), math.max(c1.zCoord, c2.zCoord)
    )
  }
}
