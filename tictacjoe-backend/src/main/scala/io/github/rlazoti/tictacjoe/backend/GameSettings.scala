package io.github.rlazoti.tictacjoe.backend

trait Difficulty
case class Easy() extends Difficulty
case class Normal() extends Difficulty
case class Hard() extends Difficulty

case class GameSettings(val gameId: Int, val level: Difficulty = Normal()) {
  val emptyPositionValue: String = "-"
  val boardWidth: Int = 3
}
