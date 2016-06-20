package io.github.rlazoti.tictacjoe.backend.services

import io.github.rlazoti.tictacjoe.backend.models._
import scala.concurrent.{ ExecutionContext, Future }

class GameService(implicit val executionContext: ExecutionContext) {

  private var boards = List[Board]()

  def addPlayersMove(playersMove: GameMove): Future[Board] = {
    findGame(playersMove.gameId).flatMap {
      case Some(board) => assignMove(board, playersMove)
      case _ => throw new IllegalArgumentException("There's no board for this game.")
    }
  }

  def createNewGame(newGameData: NewGame): Future[Board] =
    for {
      settings <- createSettings(newGameData)
      (player, opponent) <- createGamePlayers(newGameData)
      board <- generateNewBoard(settings, player, opponent, newGameData.whoStarts)
    } yield board

  private def assignMove(board: Board, playersMove: GameMove): Future[Board] =
    Future {
      val newBoard = board.addMove(Move(playersMove.row, playersMove.col))

      boards = boards.map { board =>
        if (board.settings.gameId == newBoard.settings.gameId) newBoard
        else board
      }

      newBoard
    }

  private def generateNewBoard(settings: GameSettings, player: Player, opponentPlayer: Player,
      whoStarts: String): Future[Board] =
    Future {
      val newBoard = Board.newGame(settings, player, opponentPlayer, whoStarts)
      boards = boards :+ newBoard
      newBoard
    }

  private def findGame(gameId: Int): Future[Option[Board]] =
    Future { boards.find(board => board.settings.gameId == gameId) }

  private def generateGameId(): Future[Int] = {
    val newIdFuture: Future[Int] = Future { scala.util.Random.nextInt(99999999) + 1 }

    newIdFuture.flatMap { newId =>
      findGame(newId).flatMap {
        case Some(board) => generateGameId()
        case _ => Future { newId }
      }
    }
  }

  private def createSettings(newGameData: NewGame): Future[GameSettings] =
    generateGameId().map { id => GameSettings(id, newGameData.getLevel()) }

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
