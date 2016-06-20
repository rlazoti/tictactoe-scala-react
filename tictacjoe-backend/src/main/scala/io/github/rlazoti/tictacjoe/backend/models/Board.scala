package io.github.rlazoti.tictacjoe.backend.models

object Board {

  def newGame(settings: GameSettings, user: Player, computer: Player, whoStarts: String): Board =
    whoStarts match {
      case "You" => Board(settings, user, InitialBoardState(settings, computer, user))
      case _ => Board(settings, user, InitialBoardState(settings, user, computer)).generateOpponentMove()
    }

}

case class Move(val row: Int, val col: Int)
case class GameMove(val row: Int, val col: Int, val gameId: Int)
case class BoardData(val positions: Array[Array[String]], val gameId: Int, val userMark: String,
  val computerMark: String)

case class Board(
  val settings: GameSettings,
  val userPlayer: Player,
  val currentState: BoardState) {

  private def isEnded() =
    currentState.over()

  private def playerWon() =
    currentState.won(currentState.player)

  private def opponentWon() =
    currentState.won(currentState.opponentPlayer)

  private def draw() =
    currentState.draw()

  private def generateOpponentMove() =
    if (isEnded()) this
    else {
      settings.level.gameAI.generateMove(this) match {
        case Some(move) => {
          val board = Board(settings, userPlayer, NextBoardState(currentState, move))
          println(s"${board.currentState.player.name} marks ${move}")
          board.printCurrentState()
          board
        }
        case None => this
      }
    }

  def toData: BoardData = {
    val (userMark, computerMark) =
      if (userPlayer.equals(currentState.player)) (currentState.player.getMark, currentState.opponentPlayer.getMark)
      else (currentState.opponentPlayer.getMark, currentState.player.getMark)

      BoardData(currentState.positions, settings.gameId, userMark, computerMark)
  }

  def addMove(move: Move): Board =
    if (isEnded()) this
    else {
      val board = Board(settings, userPlayer, NextBoardState(currentState, move))
      println(s"${board.currentState.player.name} marks piece ${move}")
      board.generateOpponentMove()
    }

  def printCurrentState() = {
    val message =
      if (playerWon()) s"${currentState.player.name} won!"
      else if (opponentWon()) s"${currentState.opponentPlayer.name} won!"
      else if (draw()) s"Game Draw!"
      else "Game is not over yet!"

    println(s"""
      #=============== Board =================
      #id: ${settings.gameId}
      #status: ${message}
      #blanks: ${currentState.blanks}
      #positions:
      #${currentState.positions.map(_.mkString("|")).mkString("\n")}
      #=======================================
      #""".stripMargin('#'))
  }
}
