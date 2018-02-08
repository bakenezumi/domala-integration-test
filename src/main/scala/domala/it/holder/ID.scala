package domala.it.holder

case class ID[T] private (value: Int) extends AnyVal

object ID {
  def notAssigned[T]: ID[T] = new ID[T](-1)
}
