package committee.nova.scalability

import committee.nova.scalability.utils.Conversions.{convertCharInVarArgs => convert}
import cpw.mods.fml.common.registry.GameRegistry.{addShapedRecipe => addShaped, addShapelessRecipe => addShapeless}
import net.minecraft.item.ItemStack
import net.minecraft.util.{ChatComponentText, ChatComponentTranslation}

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
}
