# Developer Guide for Reddot

This developer guide provides technical instructions for contributors or maintainers looking to understand the codebase
and technical infrastructure of Reddot.

## 1. Web Version (ReactJS + Vite Frontend, Spring Boot + MySQL Backend)

### Frontend (ReactJS + Vite)

#### Project Setup

1. Clone the repository:

      ```bash
      git clone https://github.com/trongtoannguyen/Reddot-Client.git
      cd Reddot-Client
      ```

2. Install dependencies:

      ```bash
      npm install 
      ```

3. Start the development server:

      ```bash
      npm run dev 
      ```

4. The application should now be running at `http://localhost:3000`.

#### Component Breakdown

- **QuestionFeed**: Displays a list of questions posted by users.
- **UserProfile**: Allows users to view and edit their profile information.
- **VotingMechanism**: Implements up voting and down voting functionality for both questions and answers.

#### Routing

Reddot uses React Router for client-side navigation. Routes are configured in `src/routes.js`, mapping URLs to different
components.

#### State Management

State is managed using the Context API. Global state is stored in `AppContext.js`, which provides access to data like
user information, questions, and answers across components.

### Backend (Spring Boot + MySQL)

#### API Documentation

Reddot follows RESTful principles for its backend API, using the following key endpoints:

- **Authentication API**:
    - GET /auth/confirm-account: Confirms a user's account.
    - POST /auth/login: Authenticates a user and returns a JWT token.
    - POST /auth/register: Registers a new user.

- **Recovery API**:
    - GET /settings/email/resend-confirm: Resends a confirmation email.
    - GET /settings/email/confirm: Confirms a user's email address.
    - POST /settings/reset-password: Sends a password reset email.
    - POST /settings/reset-password/confirm: Resets a user's password.
    - PUT /settings/email/edit/{id}: Updates a user's email address.

- **User Management API**:
    - GET /users : Fetches user profile by username.
    - GET /users/{id}: Fetches user profile by user id.
    - POST /users/delete: Deletes a user account.
    - PUT /users/edit/{id}: Updates a user's profile.

- **Question API**:
    - GET /questions: Gets all the questions on the site.
    - GET /questions/{ids}: Gets questions in a list of ids.
        - POST /questions/add: Submit a new question.
        - POST /questions/{id}/comments/add: Add a comment to a question.
        - PUT /questions/{id}/update: Updates a question.
        - DELETE /questions/{id}/delete: Deletes the question identified by ID.

- **Comment API**:
    - GET /comments: Get all comments on the site.
    - GET /comments/{ids}: Get the comments identified by a set of IDs.

Authentication is handled via JWT tokens passed in the `Authorization` header.

#### Database Schema

Reddot uses MySQL as its database. The key tables include:

- **Users**: Stores user information (e.g., ID, name, email, hashed password).

- **Questions**: Contains question details (e.g., ID, title, description, user_id).

- **Answers**: Stores answers associated with questions (e.g., ID, content, user_id, question_id).

- **Votes**: Contains vote details of a subject (e.g., ID, user_id, question_id, comment_id, vote_type_id).

A visual diagram of the schema can be found in the [database-scheme](database-schema.png) file.

#### Authentication

Reddot uses JWT (JSON Web Tokens) for authentication. Users authenticate via the `/auth/login` endpoint, which returns a
JWT token. This token is stored in localStorage on the client-side and passed with each request in the `Authorization`
header for protected routes.

OAuth2 can also be implemented for third-party login options (e.g., Google or GitHub).

#### Error Handling

- Custom exceptions are thrown in the backend for known issues (
  e.g., `EmailFoundException`, `ResourceNotFoundException`, `BadRequestException`).

- Logging: Use Slf4j for logging errors and key actions. Log error details for debugging and operational monitoring
  purposes.

- Global Exception Handling: A @ControllerAdvice class catches exceptions and provides consistent error responses to the
  frontend.

## 2. Mobile Version (Flutter)

### Flutter Setup

1. Clone the Flutter project:

    ```bash
    git clone https://github.com/your-repo/reddot-mobile.git 
    cd reddot-flutter
    ```

2. Set up Android Studio (Recommended), IntelliJ or VSCode for development.

3. Install dependencies:

    ```bash
    flutter pub get 
    ```

4. Run the app on a connected device or emulator:

    ```bash
    flutter run 
    ```

### Navigation

Reddot mobile uses `Navigator 2.0` for handling in-app navigation. Routes are defined in `lib/routes.dart`, mapping URLs
to screens like `HomeScreen` and `QuestionScreen`. Use `push()` and `pop()` methods to navigate between pages.

Example

```dart
Navigator.push(
  context,
  MaterialPageRoute(builder: (context) => QuestionDetailScreen(id: questionId)),
);
```

### Platform-Specific Code

Flutter’s platform channels are used to access native APIs for platform-specific functionality. For example,
using `MethodChannel` to invoke camera or location services in Android/iOS:

```dart
const platform = MethodChannel('com.example.reddot/native'); 

try { 

  final result = await platform.invokeMethod('getBatteryLevel'); 

} on PlatformException catch (e) { 

  print("Failed to get battery level: '${e.message}'."); 

} 
```

### Testing

- **Unit Testing**: For testing individual functions, such as validating input or calculating votes.

Example:

```dart

test('Increment reputation by 10', () { 

  var reputation = 50; 

  reputation += 10; 

  expect(reputation, 60); 

}); 

```

- **Widget Testing**: Tests for UI components to ensure proper rendering and user interactions.

Example:

```dart

testWidgets('Question card displays correctly', (WidgetTester tester) async { 

  await tester.pumpWidget(QuestionCard(question: sampleQuestion)); 

  expect(find.text('Sample Question'), findsOneWidget); 

}); 

```

To run unit and widget tests:

```bash
flutter test 
```

For integration tests:

```bash
flutter drive --target=test_driver/app.dart 
```

Include tests for critical functions like submitting a question, voting, and logging in.
