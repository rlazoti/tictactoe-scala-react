package io.github.rlazoti.tictacjoe.backend.services

import io.github.rlazoti.tictacjoe.backend.models._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import scala.concurrent.{ ExecutionContext, Future }
import spray.json._

class GameService(implicit val executionContext: ExecutionContext) extends DefaultJsonProtocol {

  protected implicit val NewGameFormat = jsonFormat3(NewGame)
  protected implicit val BoardFormat = jsonFormat3(Board)
  private var boards = List()

  def createNewGame(newGameData: NewGame): Future[Board] =
    for {
      settings <- createSettings(newGameData)
      (player, opponent) <- createGamePlayers(newGameData)
    } yield Board.newGame(settings, player, opponent)

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
