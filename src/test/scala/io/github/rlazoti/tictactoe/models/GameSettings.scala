package io.github.rlazoti.tictactoe.models

import org.scalatest._

class GameSettingsSpec extends FunSuite with Matchers {

  private val settings = GameSettings(Easy("easy"))
  private val emptyRow = Array(settings.emptyPositionValue, settings.emptyPositionValue, settings.emptyPositionValue)
  private val opponent = Computer(Nought())
  private val user = User(Cross())
  private val initialBoardState = InitialBoardState(settings, user, opponent)

  test("New game's settings should accept 'hard' as a valid diffculty") {
    val levelName = "hard"
    val newGame = NewGame(levelName, "X", "user")
    newGame.getLevel() shouldBe Hard(levelName)
  }

  test("New game's settings should accept 'normal' as a valid diffculty") {
    val levelName = "normal"
    val newGame = NewGame(levelName, "X", "user")
    newGame.getLevel() shouldBe Normal(levelName)
  }

  test("New game's settings should accept as Easy difficulty everything else different from 'normal' and 'hard'") {
    val levelName = "easy"

    NewGame("asdwe23", "X", "user").getLevel() shouldBe Easy(levelName)
    NewGame("", "X", "user").getLevel() shouldBe Easy(levelName)
    NewGame("  ", "X", "user").getLevel() shouldBe Easy(levelName)
    NewGame("Nor mal", "X", "user").getLevel() shouldBe Easy(levelName)
    NewGame("Very Hard", "X", "user").getLevel() shouldBe Easy(levelName)
    NewGame("super easy", "X", "user").getLevel() shouldBe Easy(levelName)
  }

}
