# Advent of Code 2022 solutions in Clojure

## Overview

Clojure [AOC](http://adventofcode.com) solutions, using Josh's awesome [aoc-helper](https://github.com/jjcomer/aoc-helper).

## Usage (from aoc-helper readme)

### Requirements

Before you use this framework there is a pre-requisite of [babashka](https://babashka.org/)

On a mac you can install babashka using brew:

```shell
$ brew install babashka
```

For other installation instructions see the [official docs](https://github.com/babashka/babashka#installation)

### Setup your Repo

This repository is already setup for you to start coding. You can clone directly and then delete the `.git` folder and init a new git repo for your own usage.

### Authentication

In order to be able to pull input for your puzzles you will need to provide your AOC session cookie value from your browser.

```TODO Instructions for finding cookie value``` 

Once you have your token, you will need to store the value for the scipts to use. This is done using the `auth` command:

```shell
$ bb auth :year <YEAR> :session <SESSION TOKEN>
```

If you don't specify a year, the script will assume the current year. You can store session tokens for multiple years within the same repo.

Session tokens are stored within the `auth.edn` file at the root of the repo. This file has already been added to the `.gitignore` file to prevent accidental check-ins.

### Generating Solution Files

When you are ready to start solving a problem the framework can generate a starting file for you to use and which will be compatible with the included runners.

To create a new solution file execute the `new` task:

```shell
$ bb new :year <YEAR> :day <DAY>
```

This will create a new file under `src/y<YEAR>/d<DAY>.clj`

### Downloading Input

Your puzzle input can be downloaded directly from AOC. Using the `get` task you can pull any available input. With no parameters the input for today will be pulled.

```shell
$ bb get :year <YEAR> :day <DAY>
```

If the input has already been pulled, the task will not re-pull the input.

### Testing your Solution

A `clojure.test` runner has also been included if you want to write tests against your solution. This can be a helpful way to use the samples which are provided in the puzzles each day.

All tests in the solution's namespace will be executed. To run your tests use the `test` task.

```shell
$ bb test :year <YEAR> :day <DAY>
```

### Running your Solution

When you are ready to run your solution, the framework is ready to help with the `run` task. This task will check for input and automatically download if missing. The input will be passed to the generator fn within your ns. The result of the generator will be passed to both the `part-1` and `part-2` solving functions. The result of both functions will be printed. If no parameters are passed today's date will be used.

```shell
$ bb run :year <YEAR> :day <DAY>
```

### Adding Dependencies

If you solutions need any additional dependencies you can add them to the `:deps` map within the `bb.edn`. They will be pull automatically on the next task execution.

Currently all solutions will run within GraalVM and more specifically the babashka runtime environment. Not all features of clojure are supported. There are a number of libraries pre-included with babashka that you can make use of with no additional configuration. See the [docs](https://book.babashka.org/#libraries) for more information.

### Customizations

If you would like to change the templated solution files, the template is found at `scripts\solution_template.clj`
