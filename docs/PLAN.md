# Notey - Kotlin Multiplatform Note Taker App

> **Learning Purpose**: This project is designed to help learn Android and Kotlin Multiplatform (KMP) development. Implementation will include detailed explanations of concepts, patterns, and why certain decisions are made.

## Working Agreement

- [ ] Make one atomic change at a time
- [ ] Explain why the change is needed before applying it
- [ ] Run one relevant check after the change
- [ ] Stop for review before moving to the next set of changes

## Features Breakdown

### P0 - Core Features (Must Have)
- [x] Create Note (title + content text fields)
- [x] View Notes List (scrollable list with title preview)
- [x] Edit Note (tap to open, modify, save)
- [x] Delete Note (swipe-to-delete or button)

### P1 - Enhanced Features
- [ ] Search Notes (filter by title/content)
- [ ] Note Timestamps (created & modified dates)
- [ ] Sort Options (by date, alphabetical, pinned first)

### P2 - Sharing Features
- [ ] Share to App (receive shared text from other apps → create note)

### P3 - Export Features
- [ ] Markdown Export (export single note or all notes as .md file)

---

## Implementation Tasks

### Phase 0: Toolchain Alignment (Blocking)
- [x] 0.1 Verify Kotlin, AGP, and KSP compatibility
- [x] 0.2 Lock KSP plugin version compatible with Kotlin 2.3.20
- [x] 0.3 Verify plugin resolution and basic module build

Acceptance criteria:
- [x] Build reaches Kotlin compilation for `:composeApp` without plugin resolution failures

Learning checkpoint:
- [x] Understand why Kotlin compiler plugins (KSP) must match the Kotlin toolchain

### Phase 1: Setup & Database (P0)
- [x] 1.1 Add Room KMP dependencies to libs.versions.toml
- [x] 1.2 Configure KSP for Room compiler in build.gradle.kts
- [x] 1.3 Choose SQLite driver strategy (`BundledSQLiteDriver` for KMP consistency)
- [x] 1.4 Create Note entity in commonMain
- [x] 1.5 Create NoteDao with CRUD operations
- [x] 1.6 Create AppDatabase with expect/actual platform builders
- [x] 1.7 Create NoteRepository wrapping database operations

Acceptance criteria:
- [x] Shared Room database compiles for Android, iOS, and JVM targets
- [x] `schemas/` directory is configured for Room schema output

Learning checkpoint:
- [x] Understand `commonMain` vs `androidMain/iosMain/jvmMain` responsibilities
- [x] Understand `expect/actual` usage for platform database path/builder setup

### Phase 2: UI & State Management (P0)
- [x] 2.1 Build ViewModel with state management
- [x] 2.2 Implement NoteListScreen UI
- [x] 2.3 Implement NoteDetailScreen (create/edit)
- [x] 2.4 Implement delete functionality
- [x] 2.5 Add i18n support for multi-language UI

Acceptance criteria:
- [x] User can create, edit, and delete notes from UI
- [x] Notes persist after app restart

Learning checkpoint:
- [x] Understand state flow from Repository -> ViewModel -> Compose UI

### Phase 3: Enhanced Features (P1)
- [ ] 3.1 Add search/filter notes feature
- [ ] 3.2 Add note timestamps (created/modified)
- [ ] 3.3 Add pin/favorite notes feature
- [ ] 3.4 Add sort options feature

Acceptance criteria:
- [ ] Search and sort combinations return expected note lists

Learning checkpoint:
- [ ] Understand query design tradeoffs (DAO query vs in-memory filtering)

### Phase 4: Sharing (P2)
- [ ] 4.1 Implement share-to-app intent (Android)
- [ ] 4.2 (Future: iOS Share Extension if needed)

Acceptance criteria:
- [ ] Shared plain text from another app creates a note draft or saved note

Learning checkpoint:
- [ ] Understand Android intent filters and `ACTION_SEND` flows

### Phase 5: Export (P3)
- [ ] 5.1 Implement markdown export feature

### Phase 6: Post-MVP Cleanup
- [ ] 6.1 Refactor `getDatabaseBuilder` API to avoid `Any` context and use a safer platform-specific factory contract
- [ ] 6.2 Revisit Room KMP Native constructor wiring (`@ConstructedBy` + `RoomDatabaseConstructor`) after core app is complete
- [ ] 6.3 Consider migrating from manual DI container to Koin for better dependency management

Acceptance criteria:
- [ ] Single-note and all-notes markdown export are supported

Learning checkpoint:
- [ ] Understand serialization format choices and export UX tradeoffs

---

## Tech Stack

- **UI Framework**: Jetpack Compose (already in project)
- **Database**: Room 2.8.x (KMP support)
- **Architecture**: MVVM with ViewModel
- **Language**: Kotlin 2.3.20
- **Compose Multiplatform**: 1.10.3
- **Material**: Material3

---

## Project Structure

```
composeApp/src/commonMain/kotlin/com/iam2kabhishek/notey/
├── data/
│   ├── notes/
│   │   ├── NoteEntity.kt          # Room entity
│   │   ├── NoteDao.kt             # Room DAO
│   │   └── NoteRepository.kt      # Repository
│   └── AppDatabase.kt            # Room database
├── ui/
│   ├── NotesViewModel.kt         # ViewModel
│   ├── NotesUiState.kt         # UI state
│   ├── screens/
│   │   ├── NoteListScreen.kt    # List screen
│   │   └── NoteDetailScreen.kt  # Detail/edit screen
│   └── components/
│       └── NoteCard.kt         # Reusable card component
├── data/local/                  # Platform-specific DB builders
│   ├── DatabaseFactory.android.kt
│   ├── DatabaseFactory.ios.kt
│   └── DatabaseFactory.jvm.kt
└── App.kt                      # Main composable with navigation
```

---

## Notes

- iOS share handling will be addressed when we reach Phase 4
- Markdown export details to be determined as we implement
- Current Room setup uses a simplified constructor path for cross-target compile stability; revisit official `@ConstructedBy` native wiring in Phase 6
- DI handled inline (no Koin/Hilt for simplicity)
- Package structure: data/notes (entity, dao, repo), ui/ (screens, viewmodel, state, components)

---

## Learning Objectives

This project teaches the following concepts:

### Kotlin Multiplatform (KMP)
- **commonMain vs platform-specific source sets**: Understanding how shared code lives in `commonMain` while platform-specific code goes in `androidMain`, `iosMain`, `jvmMain`
- **expect/actual pattern**: How to provide platform-specific implementations for common code (e.g., database builders)
- **KSP (Kotlin Symbol Processing)**: How annotation processors work in KMP context

### Room Database (KMP)
- **Entity definition**: How Room maps Kotlin data classes to database tables
- **DAO (Data Access Object)**: Writing type-safe SQL queries as Kotlin interfaces
- **SQLiteDriver**: The new KMP-native way to connect to SQLite (replacing Android's SupportSQLite)
- **DatabaseBuilder**: Platform-specific database instantiation

### MVVM Architecture
- **ViewModel**: Managing UI state and surviving configuration changes
- **State management**: Using `StateFlow` or `MutableState` in Compose
- **Repository pattern**: Abstracting data sources from ViewModels

### Jetpack Compose
- **@Composable functions**: Building UI declaratively
- **State hoisting**: Moving state up to make components stateless
- **Navigation**: Moving between screens
- **Material3**: Using Material Design components

### Android-Specific (Phase 4+)
- **Intent handling**: Receiving shared content from other apps
- **ShareSheet**: Exporting content to other apps

---

## Testing Strategy

### Test Layers

| Layer | Target | What to Test | When to Add |
|-------|--------|--------------|-------------|
| **Unit Tests** | `commonTest` | Pure logic (mappers, validation, repository rules, markdown formatting) | Start now |
| **JVM Integration** | `jvmTest` | Database logic (DAO queries, schema behavior on JVM) | After Phase 1.7 |
| **Android Instrumented** | `androidInstrumentedTest` | Real Room behavior on Android (schema, DAOs, migrations) | After Phase 1.7 |
| **UI Tests** | Compose UI tests | Screen flows (create/edit/delete happy paths) | After Phase 2.4 |

### Test Recommendations

**Phase 1 (now):**
- [ ] Add `commonTest` source set dependencies (kotlin-test, kotlin-test-junit)

**After Phase 1.7 (DAO + Repository done):**
- [ ] Test: `NoteRepository.getAllNotes()` returns notes ordered by `updatedAt DESC`
- [ ] Test: `NoteRepository.insertNote()` creates a note with timestamps
- [ ] Test: `NoteRepository.updateNote()` updates `updatedAt`
- [ ] Test: `NoteRepository.deleteNote()` removes note

**After Phase 2.4 (UI done):**
- [ ] Test: Create note → appears in list
- [ ] Test: Edit note → saves and shows updated content
- [ ] Test: Delete note → removed from list

### Notes
- `commonTest` runs on all targets (JVM, Android, iOS) — fastest feedback
- JVM integration tests are faster than Android instrumented tests
- Android instrumented tests give highest confidence for actual Android behavior
- Run `./gradlew :composeApp:checkAll` to verify common/JVM/iOS compile plus Android assemble and lint
