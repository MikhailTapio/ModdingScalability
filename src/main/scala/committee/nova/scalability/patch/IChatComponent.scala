package committee.nova.scalability.patch

import net.minecraft.util.{ChatComponentText, ChatComponentTranslation}

object IChatComponent {
  def literal(key: String): ChatComponentText = new ChatComponentText(key)

  def translatable(key: String, args: Any*): ChatComponentTranslation = new ChatComponentTranslation(key, args)
}
