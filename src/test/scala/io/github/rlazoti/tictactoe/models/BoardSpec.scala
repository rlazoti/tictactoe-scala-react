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
    val boardData = Board.newGame(settingsNormalMode, user, opponent, "computer").toData

    boardData.positions.flatten.count(piece => opponent.getPiece.equals(piece)) shouldBe 1
    boardData.positions.flatten.count(piece => user.getPiece.equals(piece)) shouldBe 0
    boardData.positions.flatten.count(piece => settingsHardMode.emptyPositionValue.equals(piece)) shouldBe 8
  }

  test("Board's ended game should not accept new moves") {
    val positions = Array( Array("X", "-", "X"), Array("O", "-", "0"), Array("-", "O", "-"))
    val board = Board.buildGame(settingsHardMode, user, opponent, positions).addMove(Move(0,1))
    val newBoard = board.addMove(Move(2,2))

    board.currentStatus() shouldBe "user-won"
    board shouldBe newBoard
  }

  test("Board's game should expose its status when the game is not over yet") {
    val positions = Array( Array("X", "-", "X"), Array("O", "-", "O"), Array("X", "O", "-"))
    val board = Board.buildGame(settingsEasyMode, user, opponent, positions)

    board.isEnded() shouldBe false
    board.currentStatus() shouldBe "active"
  }

  test("Board's ended game should expose its status when the user win the game") {
    val positions = Array( Array("X", "-", "X"), Array("O", "-", "O"), Array("-", "O", "-"))
    val board = Board.buildGame(settingsNormalMode, user, opponent, positions).addMove(Move(0,1))

    board.userWon() shouldBe true
    board.currentStatus() shouldBe "user-won"
  }

  test("Board's ended game should expose its status when the computer win the game") {
    val positions = Array( Array("X", "-", "X"), Array("O", "O", "O"), Array("X", "O", "-"))
    val board = Board.buildGame(settingsHardMode, user, opponent, positions)

    board.computerWon() shouldBe true
    board.currentStatus() shouldBe "computer-won"
  }

  test("Board's draw game should expose its status") {
    val positions = Array( Array("X", "O", "X"), Array("O", "X", "O"), Array("O", "X", "O"))
    val board = Board.buildGame(settingsEasyMode, user, opponent, positions)

    board.isEnded() shouldBe true
    board.currentStatus() shouldBe "draw"
  }

  test("Board's game should expose its available positions properly") {
    val positions = Array( Array("X", "-", "X"), Array("O", "-", "O"), Array("X", "O", "-"))
    val board = Board.buildGame(settingsNormalMode, user, opponent, positions)
    val expectedPositions = Seq(Move(0,1), Move(1,1), Move(2,2))

    board.availablePositions() shouldBe expectedPositions
  }

  test("Board's game should indentify its user's player properly") {
    val positions = Array( Array("X", "-", "X"), Array("O", "-", "O"), Array("X", "O", "-"))
    val board = Board.buildGame(settingsHardMode, user, opponent, positions)

    board.userPlayer shouldBe user
    board.computerPlayer shouldBe opponent
  }

  test("Board's game should indentify its computer's player properly") {
    val initialBoardState = InitialBoardState(GameSettings(Easy("easy")), opponent, user)
    val board = Board(GameSettings(Easy("easy")), user, initialBoardState)

    board.userPlayer shouldBe user
    board.computerPlayer shouldBe opponent
  }

}
