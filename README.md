# Scratch Card

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

The `production` variant calls the supplied activation service at
`https://api.o2.sk/version`. Its Retrofit base URL is `https://api.o2.sk/`,
and the request includes the `code` query parameter. The `mock` variant starts
an in-process MockWebServer and randomly serves version responses or HTTP 500
errors, so the app can be run and its activation success and error flows
explored without a backend. The mock app is named `M-Scratch Card` on the
device and uses the application ID `sk.lukasvyletel.scratchcard.mock`, so it
can be installed alongside the production app (`Scratch Card`, application
ID `sk.lukasvyletel.scratchcard`).
