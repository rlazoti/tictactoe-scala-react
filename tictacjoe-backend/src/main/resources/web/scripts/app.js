var GameSettingsFirstStep = React.createClass({

  clickHandler : function(level) {
    ReactDOM.render(
      <GameSettingsSecondStep level={level} />,
      document.getElementById("game-container")
    );
  },

  render : function() {
    return (
      <div className="row">
        <div className="row row-title">
          <div className="col-xs-12 text-center">
            <h2>Choose the Difficulty</h2>
          </div>
        </div>
        <div className="row">
          <div className="col-xs-12 text-center">
            <div className="btn-group btn-group-lg" role="group" aria-label="...">
              <button type="button" className="btn btn-default" onClick={this.clickHandler.bind(this, "easy")}>
                Piece of Cake
              </button>
              <button type="button" className="btn btn-warning" onClick={this.clickHandler.bind(this, "normal")}>
                Normal
              </button>
              <button type="button" className="btn btn-danger" onClick={this.clickHandler.bind(this, "hard")}>
                Impossible
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

});

var GameSettingsSecondStep = React.createClass({

  clickHandler : function(mark) {
    ReactDOM.render(
      <GameSettingsThirdStep level={this.props.level} playerMark={mark} />,
      document.getElementById("game-container")
    );
  },

  render : function() {
    return (
      <div className="row">
        <div className="row row-title">
          <div className="col-xs-12 text-center">
            <h2>OK! Now choose your Piece</h2>
          </div>
        </div>
        <div className="row">
          <div className="col-xs-12 text-center">
            <div className="btn-group btn-group-lg" role="group" aria-label="...">
              <button type="button" className="btn btn-success" onClick={this.clickHandler.bind(this, "O")}>
                &nbsp;&nbsp;O&nbsp;&nbsp;
              </button>
              <button type="button" className="btn btn-primary" onClick={this.clickHandler.bind(this, "X")}>
                &nbsp;&nbsp;X&nbsp;&nbsp;
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

});

var GameSettingsThirdStep = React.createClass({

  clickHandler : function(whoStarts) {
    var onSuccess = function(result) {
      ReactDOM.render(
        <NewGame gameData={result} />,
        document.getElementById("game-container")
      );
    };

    var onError = function(jqXHR, textStatus, errorThrown) {
      console.log(jqXHR);
    };

    var formdata = {
      "level" : this.props.level,
      "playerMark" : this.props.playerMark,
      "whoStarts" : whoStarts
    };

    $.ajax({
      type: 'POST',
      url: '/game/new',
      dataType: 'json',
      contentType: "application/json",
      data: JSON.stringify(formdata),
      beforeSend: function() {}
    })
      .done(onSuccess)
      .fail(onError);
  },

  render : function() {
    return (
      <div className="row">
        <div className="row row-title">
          <div className="col-xs-12 text-center">
            <h2>Great! Who will Start?</h2>
          </div>
        </div>
        <div className="row">
          <div className="col-xs-12 text-center">
            <div className="btn-group btn-group-lg" role="group" aria-label="...">
              <button type="button" className="btn btn-success" onClick={this.clickHandler.bind(this, "Computer")}>
                Computer.
              </button>
              <button type="button" className="btn btn-primary" onClick={this.clickHandler.bind(this, "User")}>
                I'll Start!
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

});

var GameStatus = React.createClass({

  statusHandler : function(status) {
    if (status === "user-won") return "You WIN!";
    else if (status === "computer-won") return "You LOSE! :(";
    else if (status === "draw") return "It's a draw! :/";
    else return "It's your turn...";
  },

  render : function() {
    return (
      <div className="row row-title">
        <div className="col-xs-12 text-center">
        <h3>{this.statusHandler(this.props.status)}</h3>
        </div>
      </div>
    );
  }

});

var NewGame = React.createClass({

  isPositionAvailable : function(row, col) {
    return this.props.gameData.userMark !== this.props.gameData.positions[row][col] &&
           this.props.gameData.computerMark !== this.props.gameData.positions[row][col];
  },

  classHandler : function(row, col) {
    if (this.props.gameData.positions[row][col] === this.props.gameData.computerMark)
      return "piece piece-computer";

    else if (this.props.gameData.positions[row][col] === this.props.gameData.userMark)
      return "piece piece-user";

    else if (this.props.gameData.status === "active")
      return "piece piece-free";

    else return "piece";
  },

  hoverHandler : function(row, col) {
    if (this.isPositionAvailable(row, col)) {
      $("#r" + row + "-c" + col).text(this.props.gameData.userMark);
    }
  },

  clickHandler : function(row, col) {
    if (!this.isPositionAvailable(row, col)) return false;
    else if (this.props.gameData.status !== "active") return false;

    var onSuccess = function(result) {
      ReactDOM.render(
        <NewGame gameData={result} />,
        document.getElementById("game-container")
      );
    };

    var onError = function(jqXHR, textStatus, errorThrown) {
      alert(textStatus);
    };

    var formdata = {
      "row":row,
      "col":col,
      "board": {
        "level": this.props.gameData.level,
        "userMark": this.props.gameData.userMark,
        "computerMark": this.props.gameData.computerMark,
        "positions": this.props.gameData.positions,
        "status": this.props.gameData.status
      }
    };

    $.ajax({
      type: 'POST',
      url: '/game/move',
      dataType: 'json',
      contentType: "application/json",
      data: JSON.stringify(formdata),
      beforeSend: function() {}
    })
      .done(onSuccess)
      .fail(onError);
  },

  render : function() {
    var generateColumn = function(that, positions, row) {
      return positions.map(function(value, col) {
        var id = "r" + row + "-c" + col;

        return <td key={"r" + id} className="text-center">
                 <div id={id} key={"d" + id}
                   onClick={that.clickHandler.bind(that, row, col)}
                   onMouseOver={that.hoverHandler.bind(that, row, col)}
                   className={that.classHandler(row, col)}>
                   {value}
                 </div>
               </td>;
      });
    };

    var generateRows = function(that, positions) {
      return positions.map(function(value, row) {
        return <tr key={row}>{generateColumn(that, value, row)}</tr>;
      });
    };

    return (
      <div className="row">
        <div className="row">
          <div className="col-xs-10 col-xs-offset-1 col-md-4 col-md-offset-4">
            <table id="board">
              <tbody>
                {generateRows(this, this.props.gameData.positions)}
              </tbody>
            </table>
          </div>
        </div>
        <GameStatus status={this.props.gameData.status} />
      </div>
    );
  }

});

$(function() {
  ReactDOM.render(
      <GameSettingsFirstStep />,
    document.getElementById("game-container")
  );
});
