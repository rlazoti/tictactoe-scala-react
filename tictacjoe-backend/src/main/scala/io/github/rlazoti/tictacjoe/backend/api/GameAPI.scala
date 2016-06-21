package io.github.rlazoti.tictacjoe.backend.api

import io.github.rlazoti.tictacjoe.backend.models.{ BoardData, GameMove, NewGame }
import io.github.rlazoti.tictacjoe.backend.services.GameService
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContext
import spray.json._

class GameAPI(implicit val executionContext: ExecutionContext) extends DefaultJsonProtocol {

  protected implicit val BoardDataFormat = jsonFormat5(BoardData)
  protected implicit val NewGameFormat = jsonFormat3(NewGame)
  protected implicit val GameMoveFormat = jsonFormat3(GameMove)

  private val service = new GameService()

  val routes =
    pathPrefix("app") {
      getFromResourceDirectory("web")
    } ~
    (pathPrefix("game") & post) {
      (path("new") & entity(as[NewGame])) { newGame =>
        complete(service.createNewGame(newGame).map { board => board.toData.toJson })
      } ~
      (path("move") & entity(as[GameMove])) { newMove =>
        complete(service.addPlayersMove(newMove).map { board => board.toData.toJson })
      }
    }
}
