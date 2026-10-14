# Agent Guidance

This is a single-module Android application built with Kotlin and Jetpack Compose.
The project requirements are in `README.md`.

The current project is a starter: application wiring, manual dependency injection,
generic networking infrastructure, and an empty home screen. Implement assignment
behavior only when the user asks for it. Do not add employer branding, recruitment
details, or implementation notes to the project.

## Project structure

- Package and application ID: `sk.lukasvyletel.scratchcard`.
- `core/api`: reusable Retrofit and OkHttp configuration.
- `core/dependency_injection`: `Injector`, `RealInjector`, and `GlobalModule`.
- `features/home/view`: the empty starting screen.
- Add feature code under `features/<feature>/` and reusable UI under `shared/ui/`
  when needed. Do not create layers or modules without a concrete need.
- JVM tests belong in `app/src/test/kotlin`; instrumented tests belong in
  `app/src/androidTest/kotlin`.

## Quality gate

- Use the repository's Gradle wrapper and JDK 17. Install Android SDK platform 37.
- After code changes, run:

  ```sh
  ./gradlew :app:assembleDebug :app:detekt :app:lintDebug :app:test :app:koverHtmlReportDebug
  ```

- If instrumented tests have been added, also run
  `./gradlew :app:connectedDebugAndroidTest` on a device or emulator.
- AGP 9 enables debug unit tests by default. Release Robolectric tests are not
  enabled: their Compose test activity must be configured separately.
- Cover critical behavior with unit tests. Prefer deterministic coroutine tests
  using `kotlinx-coroutines-test` and controlled dispatchers instead of real waits.
- Kover is prepared for debug coverage reporting. Introduce meaningful verification
  bounds as critical behavior is implemented; do not copy a coverage threshold
  from another application or exclude behavior solely to improve the percentage.
- Fix compile warnings and lint findings. Lint warnings are errors; there is no
  inherited lint baseline. Check official compatibility guidance before changing
  dependency or plugin versions.
- Understand Detekt findings and fix the code when the rule is a reasonable fit.
  Do not change rule settings, add baselines, or suppress findings merely to pass
  the gate. If a rule does not fit, report its name, location, and the reason.
- The original toolchain has two narrowly scoped upstream deprecation exceptions:
  `The Configuration.setVisible(boolean) method has been deprecated.` from AGP,
  and `The ReportingExtension.file(String) method has been deprecated.` from the
  Detekt Gradle plugin. Ignore only those exact messages from those plugins.
  Recheck the exceptions after upgrading the relevant plugin. Investigate all
  other warnings and deprecation summaries.
- Report checks that could not run and why. Do not claim a successful build or
  test run from static inspection.

## Testing rules

- Prefer Compose tests on the JVM with Robolectric. Use instrumented tests for
  Android-specific integration rather than duplicating JVM coverage.
- Locate Compose elements with stable test tags using `compose.onNodeWithTag(...)`,
  then assert their properties. Assert localized text from resources rather than
  hardcoded strings.
- Name JVM tests with descriptive backtick-quoted `GIVEN ... WHEN ... THEN ...`
  names. `GIVEN` describes a precondition, `WHEN` an action, and `THEN` the outcome.
  Omit a section when it would be trivial; always include the asserted outcome.
- Instrumented test names use lower camel case with `given..._when..._then...`
  sections. Do not use backtick names: the app supports devices below API 30.
- Use MockK for collaborators and MockWebServer for HTTP integration tests. The
  production application exposes `createInjector()` for test overrides, and
  `LocalInjector` can be overridden in Compose tests.

## Coding rules

- This is a single-module codebase, so do not use the `internal` keyword.
- Keep composables focused on rendering and user events. Use constructor injection
  for collaborators; resolve dependencies at the application or screen boundary.
- Keep generic networking independent of feature services and endpoint addresses.
- Keep coroutine ownership and cancellation explicit, and propagate cancellation
  instead of swallowing it as a generic error.
- Use string resources for user-facing text.
- AGP provides built-in Kotlin support. Do not add `org.jetbrains.kotlin.android`.
  Keep Compose and serialization plugin versions aligned in the version catalog.
