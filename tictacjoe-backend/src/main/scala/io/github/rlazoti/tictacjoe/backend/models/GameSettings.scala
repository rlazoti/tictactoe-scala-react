package io.github.rlazoti.tictacjoe.backend.models

trait Difficulty
case class Easy() extends Difficulty
case class Normal() extends Difficulty
case class Hard() extends Difficulty

case class NewGame(level: String, playerName: String, playerMark: String) {

  def getLevel(): Difficulty =
    level match {
      case "normal" => Normal()
      case "hard" => Hard()
      case _ => Easy()
    }

}


case class GameSettings(val gameId: Int, val level: Difficulty = Normal()) {
  val emptyPositionValue: String = "-"
  val boardWidth: Int = 3
}
