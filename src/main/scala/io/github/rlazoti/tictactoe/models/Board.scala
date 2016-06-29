package io.github.rlazoti.tictactoe.models

object Board {

  def buildGame(settings: GameSettings, user: Player, computer: Player, positions: Array[Array[String]]): Board =
    Board(settings, user, CurrentBoardState(settings, computer, user, positions))

  def newGame(settings: GameSettings, user: Player, computer: Player, whoStarts: String): Board =
    whoStarts match {
      case "user" | "User" => Board(settings, user, InitialBoardState(settings, computer, user))
      case _ => Board(settings, user, InitialBoardState(settings, user, computer)).generateOpponentMove()
    }

}

case class Move(val row: Int, val col: Int)

case class BoardData(val level: String, val userPiece: String, val computerPiece: String,
    val positions: Array[Array[String]], val status: String)

case class GameMove(val row: Int, val col: Int, val board: BoardData)

case class Board(
  val settings: GameSettings,
  val userPlayer: Player,
  val currentState: BoardState) {

  private[models] def isEnded() =
    currentState.over()

  private[models] def userWon() =
    currentState.won(userPlayer)

  private[models] def computerWon() =
    if (userPlayer.equals(currentState.player)) opponentWon()
    else playerWon()

  private def playerWon() =
    currentState.won(currentState.player)

  private def opponentWon() =
    currentState.won(currentState.opponentPlayer)

  private def draw() =
    currentState.draw()

  private def generateOpponentMove() =
    if (isEnded()) this
    else {
      settings.level.generateMove(this) match {
        case Some(move) => Board(settings, userPlayer, NextBoardState(currentState, move))
        case None => this
      }
    }

  private[models] def currentStatus() =
    if (userWon()) "user-won"
    else if (computerWon()) "computer-won"
    else if (draw()) "draw"
    else "active"

  private[models] def availablePositions(): Seq[Move] =
    currentState.positions
      .view
      .zipWithIndex
      .map { case (row, rowIndex) =>
        row.view.zipWithIndex.map { case (value, colIndex) => (Move(rowIndex, colIndex), value) }
      }
      .flatten
      .filter { case (move, piece) => piece.equals(settings.emptyPositionValue) }
      .map { case (move, piece) => move }

  private[models] def computerPlayer: Player =
    if (userPlayer.equals(currentState.player)) currentState.opponentPlayer
    else currentState.player

  def toData: BoardData = {
    val (userPiece, computerPiece) =
      if (userPlayer.equals(currentState.player)) (currentState.player.getPiece, currentState.opponentPlayer.getPiece)
      else (currentState.opponentPlayer.getPiece, currentState.player.getPiece)

    BoardData(settings.level.name, userPiece, computerPiece, currentState.positions, currentStatus())
  }

  def addMove(move: Move): Board =
    if (isEnded()) this
    else Board(settings, userPlayer, NextBoardState(currentState, move)).generateOpponentMove()

}
