# WebtoonVault

**WebtoonVault** is an Android application designed to display and manage webtoons. Users can browse webtoons, view details, mark their favorites, and rate webtoons. The app uses Firebase for authentication, Firestore for database management, and Firebase Storage for image handling. It's built with Jetpack Compose for UI and follows modern Android development practices like MVVM architecture, StateFlow, and Kotlin coroutines.

### Features
* Google Sign-In for user authentication.
* Browse Webtoons: View a list of webtoons from the Firestore database.
* Details Screen: View details about each webtoon, including rating and content.
* Favorite Webtoons: Users can mark webtoons as favorites and view them in a dedicated favorites screen.
* Rating: Users can rate webtoons and see the average rating.
* Responsive UI using Jetpack Compose.

### Screenshots
![WhatsApp Image 2024-10-18 at 01 23 05_2abd92cb](https://github.com/user-attachments/assets/bf2b1914-303a-49d7-bb26-a6dc20d89bd9)
![Home2](https://github.com/user-attachments/assets/2f9a0d97-de66-4a23-a9ec-1ba2ad673ce1)
![Details](https://github.com/user-attachments/assets/bdf608f8-e900-49a9-a294-7a7cfd234889)
![Rating](https://github.com/user-attachments/assets/74cdccca-4b07-4a72-9e26-a9b8ad69ea47)
![Favorites](https://github.com/user-attachments/assets/ffde3105-ff2e-40f1-8cc0-8c5b363f80f5)

### Technologies Used
* Kotlin: Programming language.
* Jetpack Compose: Modern UI toolkit for Android.
* Firebase Firestore: NoSQL cloud database for storing webtoon data and user favorites.
* Firebase Authentication: Google Sign-In for user authentication.
* Firebase Storage: For loading webtoon images.
* StateFlow: State management using Kotlin coroutines.
* Coil: Image loading library for loading webtoon images from Firebase Storage.

### Usage
* Sign in: Use the Google Sign-In button to log in.
* Browse webtoons: View a list of available webtoons.
* View Details: Click on a webtoon to view its details, rating, and description.
* Mark Favorite: Toggle the favorite icon to mark/unmark a webtoon as a favorite.
* Rate Webtoons: Submit your rating for the webtoon.

### Project Structure
* The project follows the MVVM (Model-View-ViewModel) architecture, using StateFlow for state management and Coroutines for asynchronous tasks.
* ViewModels: Manage UI-related data and business logic.
* Repositories: Handle data fetching and interaction with Firebase services.
* UI (Jetpack Compose): Composable functions for building UI components
