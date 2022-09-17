package committee.nova.scalability.utils

object Conversions {
  /**
   * Convert "scala.Char" in the args to "java.lang.Character"
   *
   * @param args The args to be processed
   * @return The converted args
   */
  def convertCharInVarArgs(args: Any*): Array[AnyRef] = {
    val newParams = args.toArray
    for (i <- newParams.indices if newParams(i).isInstanceOf[Char]) {
      newParams(i) = newParams(i).asInstanceOf[Char].asInstanceOf[Character]
    }
    newParams.asInstanceOf[Array[AnyRef]]
  }
}
