package io.github.rlazoti.tictacjoe.backend.models

object Board {

  def newGame(settings: GameSettings, player: Player, opponentPlayer: Player) =
    Board(settings, InitialBoardState(settings, player, opponentPlayer))

}

case class Move(val row: Int, val col: Int)
case class GameMove(val row: Int, val col: Int, val gameId: Int)
case class BoardData(val positions: Array[Array[String]], val gameId: Int)

case class Board(
  val settings: GameSettings,
  val currentState: BoardState) {

  private def isEnded() =
    currentState.over()

  private def playerWon() =
    currentState.won(currentState.player)

  private def opponentWon() =
    currentState.won(currentState.opponentPlayer)

  private def draw() =
    currentState.draw()

  def toData: BoardData =
    BoardData(currentState.positions, settings.gameId)

  def addMove(move: Move): Board = {
    if (isEnded()) this
    else {
      val board = Board(settings, NextBoardState(currentState, move))
      println(s"next move => ${board.currentState.player.name} marks ${move}")
      board.printCurrentState()
      board
    }
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
