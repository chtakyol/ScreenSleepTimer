# Architectural Rules

## Use Clean Architecture with three main layers:
Data Layer: Handles repositories, data sources (API, database, etc.).
Domain Layer: Contains use cases and business logic.
UI Layer: Includes ViewModels and Jetpack Compose UI components.

## Organize files into three main package groups:
data → API, database, repositories, and data sources.
domain → Use cases, models, and interfaces.
ui → Screens, components, and ViewModels.