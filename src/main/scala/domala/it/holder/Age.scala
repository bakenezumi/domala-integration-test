package domala.it.holder

case class Age(value: Int) extends AnyVal {
  def grow: Age = Age(value + 1)
  def grow(v: Int): Age = Age(value + v)
}
