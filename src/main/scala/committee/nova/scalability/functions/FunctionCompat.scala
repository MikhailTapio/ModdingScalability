package committee.nova.scalability.functions

import committee.nova.scalability.utils.Conversions.{convertCharInVarArgs => convert}
import cpw.mods.fml.common.registry.GameRegistry.{addShapedRecipe => addShaped, addShapelessRecipe => addShapeless}
import net.minecraft.item.ItemStack
import net.minecraft.util.{ChatComponentText, ChatComponentTranslation}

import scala.language.implicitConversions

object FunctionCompat {
  object GameRegistry {
    def addRecipe(output: ItemStack, params: Any*): Unit = addShapedRecipe(output, params)

    def addShapedRecipe(output: ItemStack, params: Any*): Unit = addShaped(output, convert(params: _*).toSeq: _*)

    def addShapelessRecipe(output: ItemStack, params: Any*): Unit = addShapeless(output, convert(params: _*).toSeq: _*)
  }

  object IChatComponent {
    def literal(key: String): ChatComponentText = new ChatComponentText(key)

    def translatable(key: String, args: Any*): ChatComponentTranslation = new ChatComponentTranslation(key, args)
  }
}
