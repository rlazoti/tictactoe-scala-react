[![][travis img]][travis]
[![][coverage img]][coverage]
[![][quality img]][quality]
[![][license img]][license]

[travis]:https://travis-ci.org/rlazoti/finagle-metrics
[travis img]:https://travis-ci.org/rlazoti/finagle-metrics.svg?branch=master

[coverage]:https://coveralls.io/github/rlazoti/finagle-metrics?branch=master
[coverage img]:https://coveralls.io/repos/github/rlazoti/finagle-metrics/badge.svg?branch=master

[quality]:https://www.codacy.com/app/rodrigolazoti/finagle-metrics
[quality img]:https://api.codacy.com/project/badge/Grade/812e2e73d83b4944aee308a58eb84ded

[license]:LICENSE
[license img]:https://img.shields.io/dub/l/vibe-d.svg


A Tic Tac Toe game in Scala and React
========================================

Easy way to send [Finagle](https://github.com/twitter/finagle) metrics to Codahale Metrics library.

## Overview

*finagle-metrics* enables your finagle based application to send its metrics to [Codahale Metrics library](https://github.com/dropwizard/metrics) instead of the default metrics ([finagle-stats](https://github.com/twitter/finagle/tree/master/finagle-stats)).

### Run

```sh
$ git clone https://github.com/rlazoti/tictactoe-scala-react.git
$ cd tictactoe-scala-react
$ sbt run
```

### Test

```sh
$ sbt test
```

Author
======

Rodrigo Lazoti - rodrigolazoti@gmail.com
