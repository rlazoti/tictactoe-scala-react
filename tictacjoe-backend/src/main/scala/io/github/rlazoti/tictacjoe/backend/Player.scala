package io.github.rlazoti.tictacjoe.backend

trait Player {
  def name: String
  def mark: String
}

case class Computer() extends Player {
  val name = "CPU"
  val mark = "O"
}

case class User(val name: String) extends Player {
  val mark = "X"
}
