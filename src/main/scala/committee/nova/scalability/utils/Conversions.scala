package committee.nova.scalability.utils

object Conversions {
  def convertCharInVarArgs(params: Any*): Array[AnyRef] = {
    val newParams = params.toArray
    for (i <- newParams.indices if newParams(i).isInstanceOf[Char]) {
      newParams(i) = newParams(i).asInstanceOf[Char].asInstanceOf[Character]
    }
    newParams.asInstanceOf[Array[AnyRef]]
  }
}
