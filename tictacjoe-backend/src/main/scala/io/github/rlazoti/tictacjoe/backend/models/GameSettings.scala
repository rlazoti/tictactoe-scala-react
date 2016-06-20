package io.github.rlazoti.tictacjoe.backend.models

trait Difficulty {
  def gameAI: GameAI
}

case class Easy() extends Difficulty {
  val gameAI = new EasyGameAI()
}

case class Normal() extends Difficulty {
  val gameAI = new EasyGameAI()
}
case class Hard() extends Difficulty {
  val gameAI = new EasyGameAI()
}

case class NewGame(level: String, playerMark: String, whoStarts: String) {

  def getLevel(): Difficulty =
    level match {
      case "normal" => Normal()
      case "hard" => Hard()
      case _ => Easy()
    }

}

case class GameSettings(val gameId: Int, val level: Difficulty) {
  val emptyPositionValue: String = "-"
  val boardWidth: Int = 3
}
