# Contributing to Dab Time 🔥

First off, thank you for considering contributing to Dab Time! It's people like you that make Dab Time such a great tool for the community.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Coding Guidelines](#coding-guidelines)
- [Commit Messages](#commit-messages)
- [Pull Request Process](#pull-request-process)
- [Testing Guidelines](#testing-guidelines)

## 🤝 Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to the project maintainers.

### Our Standards

- **Be Respectful**: Treat everyone with respect and kindness
- **Be Collaborative**: Work together and help each other
- **Be Professional**: Keep discussions focused and constructive
- **Be Inclusive**: Welcome newcomers and diverse perspectives

## 🎯 How Can I Contribute?

### Reporting Bugs 🐛

Before creating bug reports, please check the existing issues to avoid duplicates. When you create a bug report, include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps to reproduce the problem**
- **Provide specific examples** (code snippets, screenshots, etc.)
- **Describe the behavior you observed** and what you expected
- **Include device/OS information**:
  - Android version
  - Device model
  - App version

**Bug Report Template:**
```markdown
**Description**
A clear description of the bug

**Steps to Reproduce**
1. Go to '...'
2. Click on '...'
3. Scroll down to '...'
4. See error

**Expected Behavior**
What you expected to happen

**Actual Behavior**
What actually happened

**Screenshots**
If applicable, add screenshots

**Environment**
- Device: [e.g. Pixel 7]
- OS: [e.g. Android 14]
- App Version: [e.g. 1.0.0]
```

### Suggesting Features 💡

Feature suggestions are welcome! Before creating a feature request:

- **Check if the feature has already been suggested**
- **Clearly describe the feature** and its use case
- **Explain why this feature would be useful** to most users

**Feature Request Template:**
```markdown
**Is your feature request related to a problem?**
A clear description of the problem

**Describe the solution you'd like**
What you want to happen

**Describe alternatives you've considered**
Other solutions or features you've considered

**Additional context**
Any other context, screenshots, or mockups
```

### Contributing Code 💻

We love code contributions! Here's how to get started:

1. **Find or Create an Issue**
   - Look for issues labeled `good first issue` or `help wanted`
   - Comment on the issue to let others know you're working on it

2. **Fork the Repository**
   ```bash
   # Click 'Fork' on GitHub, then clone your fork
   git clone https://github.com/YOUR_USERNAME/dabtime.git
   cd dabtime
   ```

3. **Set Up Development Environment**
   - See [Development Setup](#development-setup) below

4. **Create a Branch**
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/bug-description
   ```

5. **Make Your Changes**
   - Write clean, documented code
   - Follow our [Coding Guidelines](#coding-guidelines)
   - Add tests if applicable

6. **Commit Your Changes**
   - See [Commit Messages](#commit-messages) guidelines
   ```bash
   git commit -m "feat: add amazing new feature"
   ```

7. **Push to Your Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

8. **Open a Pull Request**
   - Use our PR template
   - Link related issues
   - Provide clear description

## 🛠️ Development Setup

### Prerequisites

- Android Studio Flamingo or later
- JDK 11+
- Android SDK (API 24+)
- Git

### Initial Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Snapwave333/dabtime.git
   cd dabtime
   ```

2. **Set up Firebase**
   - Create a Firebase project
   - Download `google-services.json`
   - Place in `app/` directory
   - Enable Authentication methods (Anonymous, Google, Facebook, Email)
   - Create Firestore database

3. **Open in Android Studio**
   - File → Open → Select dabtime directory
   - Wait for Gradle sync to complete

4. **Run the app**
   - Select a device/emulator
   - Click Run ▶️

### Building

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test

# Run all checks
./gradlew check
```

## 📝 Coding Guidelines

### Kotlin Style

Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// ✅ Good
class TimerViewModel @Inject constructor(
    private val repository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    fun startTimer() {
        // Implementation
    }
}

// ❌ Bad
class timerViewModel@Inject constructor(private val repo:UserRepository,private val state:SavedStateHandle):ViewModel(){
    private val _state=MutableStateFlow(TimerUiState())
    val state:StateFlow<TimerUiState>=_state.asStateFlow()
    fun start(){
        //code
    }
}
```

### Architecture Guidelines

- **Use MVVM pattern**: ViewModels should handle business logic
- **Repository pattern**: Data access through repositories
- **Dependency Injection**: Use Hilt for DI
- **Reactive programming**: Use Kotlin Flow for data streams
- **State management**: Immutable state with StateFlow

### Code Organization

```kotlin
// File structure
package com.dabtime.app.ui.screens.timer

// 1. Imports (grouped and sorted)
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

// 2. Class declaration
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    // 3. Companion object (if needed)
    companion object {
        private const val KEY_TIME = "time"
    }

    // 4. Properties
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    // 5. Init block
    init {
        loadData()
    }

    // 6. Public methods
    fun startTimer() { }

    // 7. Private methods
    private fun loadData() { }

    // 8. Lifecycle methods
    override fun onCleared() {
        super.onCleared()
    }
}

// 9. Data classes
data class TimerUiState(
    val isRunning: Boolean = false
)
```

### Compose Guidelines

```kotlin
// ✅ Good: Composable function naming
@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    TimerContent(
        uiState = uiState,
        onStartClick = viewModel::startTimer
    )
}

@Composable
private fun TimerContent(
    uiState: TimerUiState,
    onStartClick: () -> Unit
) {
    Column {
        Text(text = uiState.time)
        Button(onClick = onStartClick) {
            Text("Start")
        }
    }
}
```

### Error Handling

```kotlin
// ✅ Good: Proper error handling
suspend fun loadData(): Result<User> {
    return try {
        val user = repository.getUser()
        Result.success(user)
    } catch (e: CancellationException) {
        throw e // Never catch CancellationException
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// ❌ Bad: Swallowing exceptions
suspend fun loadData() {
    try {
        repository.getUser()
    } catch (e: Exception) {
        // Silent failure
    }
}
```

### Documentation

```kotlin
/**
 * Manages the timer state and heat zone calculations.
 *
 * This ViewModel handles:
 * - Timer countdown with millisecond precision
 * - Heat zone transitions (High/Medium/Low)
 * - State persistence across configuration changes
 *
 * @property userRepository Repository for user data operations
 * @property savedStateHandle Handle for state persistence
 */
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Starts the timer countdown from the current time remaining.
     * Emits state updates every 100ms for smooth UI transitions.
     */
    fun startTimer() {
        // Implementation
    }
}
```

## 💬 Commit Messages

We follow [Conventional Commits](https://www.conventionalcommits.org/):

### Format
```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `perf`: Performance improvements
- `test`: Adding/updating tests
- `chore`: Maintenance tasks
- `ci`: CI/CD changes

### Examples

```bash
# Feature
feat(timer): add heat zone transition animations

# Bug fix
fix(auth): resolve Google sign-in crash on Android 14

# Documentation
docs(readme): update installation instructions

# Refactoring
refactor(settings): extract dialog components

# Performance
perf(timer): optimize heat zone calculations

# Breaking change
feat(api)!: change timer state structure

BREAKING CHANGE: TimerState now uses sealed class instead of enum
```

## 🔄 Pull Request Process

### Before Submitting

- [ ] Code follows our style guidelines
- [ ] Self-review of code completed
- [ ] Comments added for complex logic
- [ ] Documentation updated (if needed)
- [ ] No new warnings introduced
- [ ] Tests added/updated (if applicable)
- [ ] All tests pass locally
- [ ] Commits follow our commit message guidelines

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Related Issues
Fixes #(issue number)

## Testing
Describe how you tested your changes

## Screenshots (if applicable)
Add screenshots to show changes

## Checklist
- [ ] My code follows the style guidelines
- [ ] I have performed a self-review
- [ ] I have commented complex code
- [ ] I have updated documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests
- [ ] All tests pass
```

### Review Process

1. **Automated Checks**: CI/CD pipeline runs
2. **Code Review**: Maintainers review your code
3. **Feedback**: Address any requested changes
4. **Approval**: Once approved, a maintainer will merge

### After Merge

- Your PR will be merged to the main branch
- Feature will be included in next release
- You'll be added to contributors list! 🎉

## 🧪 Testing Guidelines

### Unit Tests

```kotlin
@Test
fun `timer should count down correctly`() {
    // Arrange
    val viewModel = TimerViewModel(repository, savedStateHandle)

    // Act
    viewModel.startTimer()
    testScheduler.advanceTimeBy(1000)

    // Assert
    assertEquals(89, viewModel.uiState.value.timeRemaining)
}
```

### UI Tests

```kotlin
@Test
fun timerScreen_displaysCorrectTime() {
    composeTestRule.setContent {
        TimerScreen()
    }

    composeTestRule
        .onNodeWithText("1:30")
        .assertIsDisplayed()
}
```

### Running Tests

```bash
# Unit tests
./gradlew test

# UI tests (requires emulator)
./gradlew connectedAndroidTest

# Specific test class
./gradlew test --tests TimerViewModelTest

# With coverage
./gradlew jacocoTestReport
```

## 🎨 Design Guidelines

- Follow Material 3 Design guidelines
- Use existing theme colors and typography
- Ensure proper contrast ratios (WCAG AA)
- Support both light and dark themes
- Consider accessibility (screen readers, font scaling)

## 📚 Resources

- [Android Developer Docs](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Jetpack Compose Guidelines](https://developer.android.com/jetpack/compose)
- [Material 3 Design](https://m3.material.io/)
- [Firebase Documentation](https://firebase.google.com/docs)

## ❓ Questions?

- Check [existing issues](https://github.com/Snapwave333/dabtime/issues)
- Start a [discussion](https://github.com/Snapwave333/dabtime/discussions)
- Read the [README](README.md)

## 🙏 Thank You!

Your contributions make Dab Time better for everyone. We appreciate your time and effort!

---

**Happy Coding! 🚀**
