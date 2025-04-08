![image](https://github.com/user-attachments/assets/d26128b6-680d-4a0a-bb58-d8277cf1cbc7)# Minesweeper Game - Java Swing Implementation

![image](https://github.com/user-attachments/assets/4451589d-337f-4596-8250-ce140767a730)
*A classic Minesweeper game built with Java Swing.*

---

## 📌 Overview
Minesweeper is a classic single-player puzzle game where the player's goal is to uncover all cells except those containing mines. This project is a Java Swing implementation of the game, featuring:
- Customizable board sizes and mine counts.
- Flagging mechanics to mark suspected mines.
- Recursive cell uncovering for empty areas.
- Win/loss conditions with visual feedback.

---

## 🎮 Game Mechanics

### Objective
- Uncover all non-mine cells without triggering any mines.
- Use numbered clues to deduce mine locations.

### Rules
- **Left-click**: Reveal a cell.
  - If the cell is empty, all adjacent empty cells are automatically revealed.
  - If the cell contains a mine, the game ends.
- **Right-click**: Toggle a flag on a cell (to mark suspected mines).
- **Winning**: All non-mine cells are uncovered.
- **Losing**: A mine is uncovered.

---

## 🛠️ Implementation Details

### Technologies Used
- **Java Swing**: For GUI components (`JFrame`, `JPanel`, `JLabel`).
- **Event-Driven Programming**: Handles mouse clicks and game state updates.
- **Object-Oriented Design**: Modular architecture for maintainability.

### Key Classes
| Class          | Responsibility                                                                 |
|----------------|--------------------------------------------------------------------------------|
| `Board`        | Manages game state, mine generation, cell uncovering, and rendering.          |
| `MinesAdapter` | Listens for mouse events (clicks, right-clicks) and triggers game actions.    |
| `Minesweeper`  | Sets up the game window (title, icon, layout) and initializes the `Board`.    |
| `Main`         | Entry point to launch the game.                                               |

### Features
- **Safe First Click**: Mines are generated after the first click to ensure the player never loses immediately.
- **Recursive Uncovering**: Empty cells and their neighbors are revealed automatically.
- **Dynamic Rendering**: Cells update visually to reflect their state (covered, flagged, revealed).

---

## 🖼️ Screenshots
| Board Size      | Mines | Gameplay State       |
|----------------|-------|----------------------|
| 16x16          | 100   | ![image](https://github.com/user-attachments/assets/ac45f591-9307-4b4d-a3d8-98892bdcc7fc) |
| 32x32          | 300   | ![image](https://github.com/user-attachments/assets/7a4cb503-ae78-4f47-9d1b-c328ec6a72ac) |
| 45x45          | 500   | ![image](https://github.com/user-attachments/assets/57466006-fcf0-4796-b46a-cfa475466b9f) |

---

## 🚀 How to Run
1. **Prerequisites**: Java JDK installed.
2. **Clone the repository**:
   ```bash
   git clone https://github.com/andreypUP/minesweeper-java.git
   
   cd
   javac Main.java
   java Main

