# Agent Guidance

This repository contains a working, single-module Android scratch-card application written in Kotlin and Jetpack Compose. The assignment requirements and service-variant notes are in `README.md`. Keep changes within that scope unless the user asks for more.

## Project structure

- Package and application ID: `sk.lukasvyletel.scratchcard`.
- `app/src/main/kotlin/.../core`: shared API, coroutine, navigation, annotation, and dependency-injection code.
- `app/src/main/kotlin/.../features/card`: card state, repository, code generation, activation API, and feature dependency wiring.
- `app/src/main/kotlin/.../features/home`, `scratch`, and `activate`: the three user-facing flows.
- `app/src/main/kotlin/.../shared/ui`: reusable Compose UI.
- `app/src/mock` and `app/src/production`: service base-URL configuration; the mock flavor also hosts its in-process mock service.
- JVM tests belong in `app/src/test/kotlin`; Android integration tests belong in `app/src/androidTest/kotlin`.
- Keep the existing single application module and feature structure. Add layers or modules only for a concrete need.

## Build and quality checks

- Use the Gradle wrapper, JDK 17 (also set by `gradle/gradle-daemon-jvm.properties`), and Android SDK platform 37.
- After code changes, run the repository quality gate when the environment supports it:

  ```sh
  ./gradlew qualityGate
  ```

- The root `qualityGate` task assembles and lints `mockDebug` and `productionDebug`, runs their JVM unit tests, generates both flavor coverage reports, verifies the combined package coverage rule, and runs connected Android tests for `mockDebug`.
- Kover verifies a 100% line-coverage minimum across `features`, `core`, and `shared`; preview code is excluded by annotation and preview class name.
- Fix compile warnings and lint findings. Lint warnings are errors. Understand Detekt findings and fix them when the rule is a reasonable fit; do not alter rule settings, add baselines, or suppress findings solely to pass the gate. If a rule does not fit, report its name, location, and reason.
- Check official compatibility guidance before changing dependency or plugin versions. AGP supplies Kotlin support; do not add `org.jetbrains.kotlin.android`. Keep Compose and serialization plugin versions aligned in the version catalog.
- Report checks that could not run and why. Do not claim successful builds or tests based on static inspection.

## Testing

- Cover critical behavior with unit tests. Prefer deterministic coroutine tests using `kotlinx-coroutines-test` and controlled dispatchers over real waits.
- Prefer JVM Compose tests with Robolectric. Use instrumented tests for Android-specific integration rather than duplicating JVM coverage.
- Find Compose elements by stable test tags with `compose.onNodeWithTag(...)`; assert localized text through resources rather than hardcoded strings.
- Name JVM tests with descriptive backtick-quoted `GIVEN ... WHEN ... THEN ...` names. `GIVEN` describes a precondition, `WHEN` an action, and `THEN` the asserted outcome. Omit a section when trivial; always include the outcome.
- Name instrumented tests in lower camel case with `given..._when..._then...` sections; do not use backtick names because the app supports devices below API 30.
- Use MockK for collaborators and MockWebServer for HTTP integration tests. The production `ScratchCardApplication` provides the overridable `createInjector()` boundary; Compose tests can override `LocalInjector`.

## Coding rules

- This is a single-module codebase, so do not use the `internal` keyword.
- Exclude every Compose preview from coverage with `@ExcludeFromCoverage` from `core.annotations`.
- When a preview needs an injector, provide `PreviewInjector`; do not use `RealInjector` in previews. Do not add preview-specific conditional logic to production composables.
- Keep composables focused on rendering and user events. Use constructor injection for collaborators and resolve dependencies at the application or screen boundary.
- Keep generic networking independent of feature services and endpoint addresses.
- Make coroutine ownership and cancellation explicit. Propagate cancellation instead of swallowing it as a generic error.
- Use string resources for user-facing text.
