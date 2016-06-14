package io.github.rlazoti.tictacjoe.backend

case class Position(val row: Int, val col: Int)

case class Move(val player: Player, val position: Position)

case class Board(
  val id: Int,
  val settings: GameSettings,
  val player: Player,
  val opponentPlayer: Player,
  val currentState: BoardState) {

  private def isEnded() =
    currentState.over(player, opponentPlayer)

  private def userWon() =
    currentState.won(player)

  private def opponentWon() =
    currentState.won(opponentPlayer)

  private def draw() =
    currentState.draw(player, opponentPlayer)

  def addMove(move: Move): Board = {
    if (isEnded()) this
    else {
      val board = Board(id, settings, player, opponentPlayer, NextBoardState(currentState, move))
      println(s"next move => ${move.player.name} marks ${move.position}")
      board.printCurrentState()
      board
    }
  }

  def printCurrentState() = {
    val message =
      if (userWon()) s"${player.name} won!"
      else if (opponentWon()) s"${opponentPlayer.name} won!"
      else if (draw()) s"Game Draw!"
      else "Game is not over yet!"

    println(s"""
      #=============== Board =================
      #id: $id
      #status: ${message}
      #blanks: ${currentState.blanks}
      #positions:
      #${currentState.positions.map(_.mkString("|")).mkString("\n")}
      #=======================================
      #""".stripMargin('#'))
  }
}
