package io.github.rlazoti.tictactoe.services

import io.github.rlazoti.tictactoe.models._
import scala.concurrent.{ ExecutionContext, Future }

class GameService(implicit val executionContext: ExecutionContext) {

  def addPlayersMove(playersMove: GameMove): Future[Board] =
    for {
      gameData <- createGameData(playersMove.board.level, playersMove.board.userMark)
      settings <- createSettings(gameData)
      (player, opponent) <- createGamePlayers(gameData)
      board <- generateCurrentBoard(settings, player, opponent, playersMove.board.positions)
      newBoard <- assignMove(board, playersMove)
    } yield newBoard

  def createNewGame(newGameData: NewGame): Future[Board] =
    for {
      settings <- createSettings(newGameData)
      (player, opponent) <- createGamePlayers(newGameData)
      board <- generateNewBoard(settings, player, opponent, newGameData.whoStarts)
    } yield board

  private def createGameData(level: String, userMark: String): Future[NewGame] =
    Future { NewGame(level, userMark, "") }

  private def assignMove(board: Board, playersMove: GameMove): Future[Board] =
    Future { board.addMove(Move(playersMove.row, playersMove.col)) }

  private def generateCurrentBoard(settings: GameSettings, player: Player, opponentPlayer: Player,
      positions: Array[Array[String]]): Future[Board] =
    Future { Board.buildGame(settings, player, opponentPlayer, positions) }

  private def generateNewBoard(settings: GameSettings, player: Player, opponentPlayer: Player,
      whoStarts: String): Future[Board] =
    Future { Board.newGame(settings, player, opponentPlayer, whoStarts) }

  private def createSettings(newGameData: NewGame): Future[GameSettings] =
    Future { GameSettings(newGameData.getLevel()) }

  private def createPlayersMarks(newGameData: NewGame): Future[(Mark, Mark)] =
    Future {
      val playerMark = Mark.getByType(newGameData.playerMark)
      val opponentMark = Mark.getOpponentMark(playerMark)
      (playerMark, opponentMark)
    }

  private def createGamePlayers(newGameData: NewGame): Future[(Player, Player)] =
    createPlayersMarks(newGameData).map {
      case (playerMark, opponentMark) =>
        (User(playerMark), Computer(opponentMark))
    }

}
