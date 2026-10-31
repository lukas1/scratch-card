# Scratch Card

[![Quality Gate](https://github.com/lukas1/scratch-card/actions/workflows/quality-gate.yml/badge.svg?branch=main)](https://github.com/lukas1/scratch-card/actions/workflows/quality-gate.yml)

## Assignment

Create an Android application in Kotlin that models a scratch card and its usage.
The minimum Android version is at your discretion. Libraries are allowed, and
framework choices are up to you.

The scratch card has three states: unscratched, scratched (with its code revealed),
and activated.

### Main screen

Always display:

- The current state of the scratch card.
- A button that navigates to the scratch screen.
- A button that navigates to the activation screen.

### Scratch screen

Provide a button that changes the card's state to scratched. Scratching reveals
a code generated as a random UUID. Simulate a heavy operation that takes two
seconds.

If the user closes the screen with the back button before the operation has
successfully completed, cancel the operation.

### Activation screen

Provide a button that activates the scratch card by sending its revealed code to
the supplied activation service.

Service definition:

- Endpoint: `https://api.o2.sk/version`.
- Base URL: `https://api.o2.sk/`.
- Path: `/version`.
- Method: `GET`.
- Query parameter: `code`.
- Authentication: none.
- Example response:

  ```json
  { "android": "287028" }
  ```

If the numeric value of `android` is greater than `277028`, change the card's state
to activated. Otherwise, display an error modal.

If the user closes the screen while activation is in progress, do not cancel the
operation.

### Quality

Cover critical parts of the application with unit tests. Focus on proper software
engineering.

## Solution notes

Strictly speaking the architecture wiring around the assignment is larger than necessary for the
given assignment. The goal was not to simply fulfill the assignment, but to showcase the engineering
approach when working on a much larger project.

The `production` variant calls the supplied activation service at
`https://api.o2.sk/version`. Its Retrofit base URL is `https://api.o2.sk/`,
and the request includes the `code` query parameter. The `mock` variant starts
an in-process MockWebServer and randomly serves version responses or HTTP 500
errors, so the app can be run and its activation success and error flows
explored without a backend. The mock app is named `M-Scratch Card` on the
device and uses the application ID `sk.lukasvyletel.scratchcard.mock`, so it
can be installed alongside the production app (`Scratch Card`, application
ID `sk.lukasvyletel.scratchcard`).

CI pipeline runs on main on purpose. This is a single person project, checks
are also run locally before pushes and therefore pull requests would serve
no purpose in this repo. With team collaboration it'd be setup on PRs with and
passing the CI verification would be required for a PR to be mergeable.

### Dependency injection

The handwritten dependency injection setup is deliberately more elaborate than
this small demo needs. It demonstrates a way to add feature modules without
making injector construction allocate a dependency factory or `by lazy` holder
for every declared module. `AppBindings` provides production recipes, while
tests use fail-closed bindings and feature-specific helpers to provide only
the dependencies they need.

`SharedInjector` creates modules on request and retains them in a thread-safe
sparse cache. This keeps stateful dependencies, such as the card repository,
alive across visits to a feature while allocating module instances only for
features that are used. The previous design's constructor inputs and `by lazy`
properties would add per-module startup work as the graph grew. This design
avoids that particular cost; it does not make startup independent of code size,
class loading, or modules actually requested at startup.
