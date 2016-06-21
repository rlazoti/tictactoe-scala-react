package io.github.rlazoti.tictacjoe.backend.models

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

case class BoardData(val level: String, val userMark: String, val computerMark: String,
  val positions: Array[Array[String]], val status: String)

case class GameMove(val row: Int, val col: Int, val board: BoardData)

case class Board(
  val settings: GameSettings,
  val userPlayer: Player,
  val currentState: BoardState) {

  private def isEnded() =
    currentState.over()

  private def userWon() =
    currentState.won(userPlayer)

  private def computerWon() =
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
      settings.level.gameAI.generateMove(this) match {
        case Some(move) => Board(settings, userPlayer, NextBoardState(currentState, move))
        case None => this
      }
    }

  private def currentStatus() =
    if (userWon()) "user-won"
    else if (computerWon()) "computer-won"
    else if (draw()) "draw"
    else "active"

  def toData: BoardData = {
    val (userMark, computerMark) =
      if (userPlayer.equals(currentState.player)) (currentState.player.getMark, currentState.opponentPlayer.getMark)
      else (currentState.opponentPlayer.getMark, currentState.player.getMark)

    BoardData(settings.level.name, userMark, computerMark, currentState.positions, currentStatus())
  }

  def addMove(move: Move): Board =
    if (isEnded()) this
    else Board(settings, userPlayer, NextBoardState(currentState, move)).generateOpponentMove()

}
