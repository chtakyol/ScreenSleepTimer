# Jetpack Compose Rules

# Previews:
Every screen and reusable component must have at least one preview.
Use @Preview with different screen sizes (phone, tablet, dark mode).

# State Management:
UI state should be handled inside ViewModel.
Use StateFlow instead of LiveData in ViewModels.
UI elements should be stateless and receive state via StateFlow.

# Navigation:
Use Jetpack Navigation Compose.
Define routes in a sealed class to prevent string-based navigation errors.
Pass arguments safely using NavBackStackEntry.arguments.