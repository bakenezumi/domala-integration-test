package domala.it.entity

import domala._

case class PersonDepartmentEmbedded(
  id: Int,
  name: String,
  department: Department
)
