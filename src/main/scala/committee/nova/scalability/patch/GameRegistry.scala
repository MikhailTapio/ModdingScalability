package committee.nova.scalability.patch

import committee.nova.scalability.utils.Conversions.{convertCharInVarArgs => convert}
import cpw.mods.fml.common.registry.GameRegistry.{addShapedRecipe => addShaped, addShapelessRecipe => addShapeless}
import net.minecraft.item.ItemStack

object GameRegistry {
  def addRecipe(output: ItemStack, args: Any*): Unit = addShapedRecipe(output, args)

  def addShapedRecipe(output: ItemStack, args: Any*): Unit = addShaped(output, convert(args: _*).toSeq: _*)

  def addShapelessRecipe(output: ItemStack, args: Any*): Unit = addShapeless(output, convert(args: _*).toSeq: _*)
}
