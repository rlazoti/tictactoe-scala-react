package io.github.rlazoti.tictactoe.models

import org.scalatest._

class BoardSpec extends FunSuite with Matchers {

  private val settingsEasyMode = GameSettings(Easy("easy"))
  private val settingsNormalMode = GameSettings(Normal("normal"))
  private val settingsHardMode = GameSettings(Hard("hard"))

  private val opponent = Computer(Nought())
  private val user = User(Cross())

  private val emptyRow = Array(
    settingsEasyMode.emptyPositionValue,
    settingsEasyMode.emptyPositionValue,
    settingsEasyMode.emptyPositionValue
  )

  test("Board's new game should create a new Board") {
    val board = Board.newGame(settingsEasyMode, user, opponent, "user")
    board shouldNot be(null)
  }

  test("Board's new game should export it's data properly") {
    val boardData = Board.newGame(settingsEasyMode, user, opponent, "user").toData

    boardData.status shouldBe "active"
    boardData.userPiece shouldBe user.getPiece
    boardData.computerPiece shouldBe opponent.getPiece
    boardData.positions.foreach { row =>
      row should contain theSameElementsAs emptyRow
    }
  }

  test("Board's new game in easy mode should export it's level properly") {
    val boardData = Board.newGame(settingsEasyMode, user, opponent, "user").toData
    boardData.level shouldBe settingsEasyMode.level.name
  }

  test("Board's new game in normal mode should export it's level properly") {
    val boardData = Board.newGame(settingsNormalMode, user, opponent, "user").toData
    boardData.level shouldBe settingsNormalMode.level.name
  }

  test("Board's new game in hard mode should export it's level properly") {
    val boardData = Board.newGame(settingsHardMode, user, opponent, "user").toData
    boardData.level shouldBe settingsHardMode.level.name
  }

  test("When user is who will start, the new board should contain an user's move and a computer's reply") {
    val boardData = Board.newGame(settingsEasyMode, user, opponent, "user").addMove(Move(0,0)).toData

    boardData.positions(0)(0) shouldBe user.getPiece
    boardData.positions.flatten.count(piece => opponent.getPiece.equals(piece)) shouldBe 1
    boardData.positions.flatten.count(piece => user.getPiece.equals(piece)) shouldBe 1
    boardData.positions.flatten.count(piece => settingsHardMode.emptyPositionValue.equals(piece)) shouldBe 7
  }

  test("When computer is who will start, the new board should contain only a computer's move") {
    val boardData = Board.newGame(settingsEasyMode, user, opponent, "computer").toData

    boardData.positions.flatten.count(piece => opponent.getPiece.equals(piece)) shouldBe 1
    boardData.positions.flatten.count(piece => user.getPiece.equals(piece)) shouldBe 0
    boardData.positions.flatten.count(piece => settingsHardMode.emptyPositionValue.equals(piece)) shouldBe 8
  }

}
