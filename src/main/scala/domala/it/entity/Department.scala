package domala.it.entity

import domala.it.holder.ID

case class Department(
    departmentId: ID[Department],
    departmentName: String
)
