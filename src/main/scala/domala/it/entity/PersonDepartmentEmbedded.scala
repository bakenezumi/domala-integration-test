package domala.it.entity

import domala.it.holder.{ID, PersonName}

case class PersonDepartmentEmbedded(
    id: ID[Person],
    name: PersonName,
    department: Department
)
