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

  protected implicit val BoardDataFormat = jsonFormat4(BoardData)

  private val service = new GameService()
  private val allowedCorsVerbs = List(GET)
  private val allowedCorsHeaders = List(
    "X-Requested-With", "content-type", "origin", "accept"
  )

  private lazy val enableCORS =
    respondWithHeader(`Access-Control-Allow-Origin`.`*`) &
    respondWithHeader(`Access-Control-Allow-Methods`(allowedCorsVerbs)) &
    respondWithHeader(`Access-Control-Allow-Headers`(allowedCorsHeaders)) &
    respondWithHeader(`Access-Control-Allow-Credentials`(true))

  val routes =
    pathPrefix("web") {
      getFromResourceDirectory("web")
    } ~
    (pathPrefix("game") & get & enableCORS) {
      (path("new") & parameters("level", "playerMark", "whoStarts").as(NewGame)) { newGame =>
        complete(service.createNewGame(newGame).map { board => board.toData.toJson })
      } ~
      (path("move") & parameters("row".as[Int], "col".as[Int], "gameId".as[Int]).as(GameMove)) { newMove =>
        complete(service.addPlayersMove(newMove).map { board => board.toData.toJson })
      }
    }
}
