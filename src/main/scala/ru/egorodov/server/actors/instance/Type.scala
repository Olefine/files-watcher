package ru.egorodov.server.actors.instance

object Type extends Enumeration {
  type Type = Value

  /**
    * Enum to select the instance type
    * Small, Medium, Large solves as (available RAM = coefficent * resourceSize)
    * Full means - utilize all available resources(it would be helpful for standalone mode)
    */
  val Small, Medium, Large, Full = Value
}
