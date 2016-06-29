package io.github.rlazoti.tictactoe.models

sealed trait Difficulty {
  protected[this] def gameAI: GameAI

  def name: String
  def generateMove(board: Board): Option[Move] =
    gameAI.generateMove(board)
}

case class Easy(val name: String) extends Difficulty {
  protected[this] val gameAI = new EasyGameAI()
}

case class Normal(val name: String) extends Difficulty {
  protected[this] val gameAI = new NormalGameAI()
}

case class Hard(val name: String) extends Difficulty {
  protected[this] val gameAI = new HardGameAI()
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
