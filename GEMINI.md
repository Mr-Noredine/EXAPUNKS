# EXAPUNKS Clone - Project Overview

This project is a Java-based clone of the hacking simulation game **Exapunks**, developed as a 2nd-year university project at Sorbonne Paris Nord. It features a graphical interface where players can write assembly-like instructions to control robots (EXAs) to solve puzzles.

## Project Structure

The project follows the **Model-View-Controller (MVC)** architectural pattern:

- **`src/model/`**: Contains the core game logic, including:
    - `Grille.java`: Manages the game world (grid, robots, files, and doors).
    - `Robot.java`: Represents the EXAs that execute instructions.
    - `Fichier.java`: Represents files that can be grabbed, dropped, or modified.
    - `AnalyseurSyntaxique.java` & `Instruction.java`: Handle command parsing and representation.
    - `Niveau.java` & `Niveau[1-3].java`: Define level-specific goals and initial states.
- **`src/view/`**: Contains the Swing-based GUI components:
    - `GameWindow.java`: The main application frame.
    - `GamePanel.java`: Responsible for rendering the grid and game entities.
    - `TextZone.java`: The area where the user inputs code.
- **`src/controller/`**: Orchestrates the interaction between the model and view:
    - `Main.java`: The application's entry point.
    - `GameController.java`: Executes instructions in a separate thread.
- **`src/assets/`**: Contains graphical assets (images) used by the UI.

## Supported Instructions

The game supports a subset of the original Exapunks instruction set:

| Command | Arguments | Description |
| :--- | :--- | :--- |
| `LINK` | `direction/id` | Moves the robot in a direction (left, right, up, down) or through a door. |
| `COPY` | `source`, `dest` | Copies a value from a source (X, T, M, F, or literal) to a destination. |
| `GRAB` | `file_id` | Picks up a file with the specified ID. |
| `DROP` | - | Drops the currently held file. |
| `ADDI` | `v1`, `v2`, `dest` | Adds `v1` and `v2`, storing the result in `dest`. |
| `SUBI` | `v1`, `v2`, `dest` | Subtracts `v2` from `v1`, storing the result in `dest`. |
| `MULI` | `v1`, `v2`, `dest` | Multiplies `v1` and `v2`, storing the result in `dest`. |
| `DIVI` | `v1`, `v2`, `dest` | Divides `v1` by `v2`, storing the result in `dest`. |
| `MODI` | `v1`, `v2`, `dest` | Stores the remainder of `v1 / v2` in `dest`. |
| `TEST` | `condition` | Performs a test (e.g., `X != 0`) and sets the `T` register. |
| `JUMP` | `line_index` | Unconditionally jumps to a specific instruction line. |
| `FJUMP` | `line_index` | Jumps to a line if the last `TEST` result was false. |
| `NOOP` | - | No operation; waits for a short period. |
| `HALT` | - | Stops the robot and removes it from the grid. |

## Registers & Buffers
- **`X`**: General-purpose register.
- **`T`**: Test register (used for conditional jumps).
- **`M`**: Message register for communication (basic implementation).
- **`F`**: File buffer (reads/writes to the held file).

## Building and Running

### Prerequisites
- Java Development Kit (JDK) 17 or higher.

### Compilation
From the root directory:
```bash
mkdir -p bin
javac -d bin -sourcepath src src/controller/Main.java
```

### Running the Application
```bash
java -cp bin controller.Main
```

### Running the Textual Test
```bash
java -cp bin main.TestPartieTextuelle
```

## Development Conventions
- **Language**: Source code uses French for many identifiers (e.g., `Grille`, `Fichier`, `Instruction`).
- **Assets**: Images are loaded from `src/assets/images/`.
- **Threading**: Instruction execution is handled in separate threads to maintain GUI responsiveness.
- **Levels**: New levels should extend the `Niveau` class and implement `testVectoire()` and `Initialiser...` methods.
