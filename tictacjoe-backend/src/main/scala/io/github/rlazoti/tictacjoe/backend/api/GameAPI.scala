package io.github.rlazoti.tictacjoe.backend.api

import akka.http.scaladsl.model.HttpRequest
import io.github.rlazoti.tictacjoe.backend.models.{ BoardData, GameMove, NewGame }
import io.github.rlazoti.tictacjoe.backend.services.GameService
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContext
import spray.json._

class GameAPI(implicit val executionContext: ExecutionContext) extends DefaultJsonProtocol {

  protected implicit val NewGameFormat = jsonFormat3(NewGame)
  protected implicit val BoardDataFormat = jsonFormat2(BoardData)
  protected implicit val GameMoveFormat = jsonFormat3(GameMove)

  private val service = new GameService()
  // private val allowedCorsVerbs = List(GET)
  // private val allowedCorsHeaders = List(
  //   "X-Requested-With", "content-type", "origin", "accept"
  // )

  // private lazy val enableCORS =
  //   respondWithHeader(`Access-Control-Allow-Origin`.`*`) &
  //   respondWithHeader(`Access-Control-Allow-Methods`(allowedCorsVerbs)) &
  //   respondWithHeader(`Access-Control-Allow-Headers`(allowedCorsHeaders)) &
  //   respondWithHeader(`Access-Control-Allow-Credentials`(true))

  val routes =
    (path("game" / "new") & get & parameters('level, 'playerName, 'playerMark)) {
      (level, playerName, playerMark) =>
      val newGame = NewGame(level, playerName, playerMark)
      complete(service.createNewGame(newGame).map { board => board.toData.toJson })
    } ~
    (path("game" / "move") & post & entity(as[GameMove])) { newMove =>
      complete(service.addPlayersMove(newMove).map { board => board.toData.toJson })
    } ~
    (path("web") & get & entity(as[HttpRequest])) { request =>
      getFromResourceDirectory("web")
    }
}
