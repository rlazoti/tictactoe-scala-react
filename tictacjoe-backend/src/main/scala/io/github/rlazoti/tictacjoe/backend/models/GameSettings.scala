package io.github.rlazoti.tictacjoe.backend.models

sealed trait Difficulty {
  def gameAI: GameAI
  def name: String
}

case class Easy(val name: String) extends Difficulty {
  val gameAI = new EasyGameAI()
}

case class Normal(val name: String) extends Difficulty {
  val gameAI = new EasyGameAI()
}

case class Hard(val name: String) extends Difficulty {
  val gameAI = new EasyGameAI()
}

case class NewGame(level: String, playerMark: String, whoStarts: String) {
  def getLevel(): Difficulty =
    level match {
      case "normal" => Normal(level)
      case "hard" => Hard(level)
      case _ => Easy("easy")
    }
}

case class GameSettings(val level: Difficulty) {
  val emptyPositionValue: String = "-"
  val boardWidth: Int = 3
}
