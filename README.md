# ⚔️ Pokémon ShootDown

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/downloads/#java21)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

<div align="center">
  <img width="600" height="450" alt="Pokémon ShootDown Banner" src="https://github.com/user-attachments/assets/8ef8493b-641d-4086-877d-a7b1e543c357" />
  <p><em>A fast-paced turn-based Pokémon battle simulator built with Java 21, JavaFX, and Maven.</em></p>
</div>

---

## 🎮 Overview

**Pokémon ShootDown** is a desktop battle simulator inspired by competitive Pokémon showdown formats. It features a complete combat loop, active team management, dynamic move effectiveness calculation, status conditions, passive & consumable items, and an interactive JavaFX graphical user interface with audio feedback.

---

## ✨ Features

- **🏆 Team Builder:** Select up to 6 Pokémon, customize held items and inspect stats.
- **⚡ Turn-Based Battle Engine:**
  - Complete damage formula considering base attack, defense, move power, and type chart multipliers.
  - Critical hits and speed-based turn order resolution.
- **🔥 Status Ailments:** Burn (reduces attack & periodic damage), Paralysis (speed drop & full para chance), Poison.
- **🛡️ Items & Consumables:**
  - **Held Items:** *Leftovers* (turn-end heal), *Focus Sash* (KO prevention), *Assault Vest* (defense boost), *Air Balloon*.
  - **Usable Items:** *Potion*, *Antidote*, *Berry Juice*.
- **🎨 Interactive JavaFX UI:**
  - Health bar animations with status badges.
  - Battle log with dynamic combat text.
  - Sprite rendering for all included Pokémon.
- **🔊 Audio Engine:** Immersive background music and custom sound effects for moves, hits, victory and defeat.

---

## 🛠️ Architecture

```
Pokemon-ShootDown/
├── simulator/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/pokemon/
│       │   ├── controllers/      # JavaFX UI Controllers (Battle, TeamBuilder, Start)
│       │   ├── core/             # BattleEngine, DamageCalculator, AudioManager, DataLoaders
│       │   ├── effect/           # Attack on-hit and status effects (LifeSteal, Damocles...)
│       │   ├── items/            # Held and usable items logic
│       │   ├── models/           # Domain models: Pokemon, Move, Team, Type
│       │   ├── status/           # Burn, Paralysis, Poison state handlers
│       │   └── utils/            # Scene navigation and helper utilities
│       └── resources/com/pokemon/
│           ├── assets/           # Sounds and Pokemon sprites
│           ├── css/              # Custom stylesheets
│           ├── data/             # CSV pokedex datasets (moves, pokemons)
│           └── views/            # FXML layout definitions
```

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 21+**
- **Apache Maven 3.8+**

### Installation & Run

```bash
# Clone the repository
git clone https://github.com/FamilyTsuki/Pokemon-ShootDown.git
cd Pokemon-ShootDown/simulator

# Compile and run with Maven JavaFX plugin
mvn clean javafx:run
```

---

## 👥 Authors

- **Alban ELIE** ([@FamilyTsuki](https://github.com/FamilyTsuki))
- **Raphaël CERF** ([@raphaelecerf](https://github.com/raphaelecerf))

---

## 📄 License

This project is licensed under the MIT License.
