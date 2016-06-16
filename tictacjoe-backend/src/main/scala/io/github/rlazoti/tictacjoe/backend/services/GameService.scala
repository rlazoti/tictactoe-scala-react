package io.github.rlazoti.tictacjoe.backend.services

import io.github.rlazoti.tictacjoe.backend.models._
import scala.concurrent.{ ExecutionContext, Future }

class GameService(implicit val executionContext: ExecutionContext) {

  private var boards = List[Board]()

  def createNewGame(newGameData: NewGame): Future[Board] =
    for {
      settings <- createSettings(newGameData)
      (player, opponent) <- createGamePlayers(newGameData)
      board <- generateNewBoard(settings, player, opponent)
    } yield board

  private def generateNewBoard(settings: GameSettings, player: Player, opponentPlayer: Player): Future[Board] =
    Future { Board.newGame(settings, player, opponentPlayer) }

  private def findGame(gameId: Int): Future[Option[Board]] =
    Future { boards.find(board => board.settings.gameId == gameId) }

  private def generateGameId(): Future[Int] =
    Future { scala.util.Random.nextInt(99999999) + 1 }

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
        (User(newGameData.playerName, playerMark), Computer(opponentMark))
    }

}
