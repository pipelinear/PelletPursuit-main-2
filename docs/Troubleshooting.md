# Troubleshooting

## Common problems

| Symptom | Fix |
|---------|-----|
| `Could not find or load main class` | IntelliJ didn't load the Maven project — open the Maven tool window (**View → Tool Windows → Maven**) and click the refresh icon |
| `JDK 21 required` or module not specified | **File → Project Structure → SDK** — set it to JDK 24 |
| Window opens but game is blank | Press ENTER to start |
| `./mvnw: Permission denied` | Run `chmod +x mvnw` in the terminal, then retry |
| `WARNING: sun.misc.Unsafe` in the console | Harmless — ignore it. Comes from the graphics library, not your code |

## Classroom setup notes

- Run `./mvnw javafx:run` on one machine before class to warm up the Maven
  cache, then copy `~/.m2` to other machines if the school network blocks
  Maven Central.
- Do **not** click **Build → Rebuild Project** in IntelliJ — Maven handles the build.
