# TinyTasks

Welcome to _TinyTasks_, the most basic task management app in the whole wide world - no fancy UI and a
very limited set of features. Fortunately, you are here to save the day and improve parts of _TinyTasks_.
Feel free to focus on a single feature or aspect of the application. You can find a list of open issues in
the [issue section](https://github.com/mindsmash/tiny-tasks/issues) of this repository.

## Dependencies
- jdk 17 or later
  - Use e.g. [https://sdkman.io/install](https://sdkman.io/install) and run

    ```bash
    sdk install java 17-open
    ```

- The right version of node (lts/erbium) and yarn
  - Use e.g. [https://github.com/nvm-sh/nvm](https://github.com/nvm-sh/nvm) and run

    ```bash
    nvm install lts/erbium
    nvm use lts/erbium
    npm install -g yarn
    yarn --version
    
    # If you're on mac, also run
    xcode-select --install
    ```

 ## IDE

Option 1. GitHub Codespaces is available out of the box with a properly configured devcontainer.

Option 2. Use the IDE of your choice
  - Wwhatever you feel comfortable coding an Angular + Spring Boot project.
  - Make sure you have the jdk 17+ and Gradle 8+ installed above available in your IDE

## Development

The application consists of a frontend and a backend. Both can be started separately. The frontend is
[Angular](https://angular.io/) based and the backend is based on [Spring Boot](https://spring.io/projects/spring-boot).

Before you can start developing you need to set up your development environment. You can find good and clear
instructions on the Angular website in the [Quickstart](https://angular.io/guide/quickstart) guide.
The backend uses H2 in-memory database, therefore it is not necessary to install any database server or run it in Docker

### Frontend

The fronted was generated with [Angular CLI](https://github.com/angular/angular-cli). Run `yarn` to install the
dependencies for the app. You can also add new dependencies via `yarn add`. Run `yarn start` for a dev server.
Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

To run the dev server without starting the backend use `yarn start-local`. The application will then store its
data in the browser's local storage instead of sending the data to the backend.

Run `yarn lint` to lint your application and `yarn test` to execute the unit tests via [Karma](https://karma-runner.github.io).

The application is tested using [Cypress](https://www.cypress.io). To execute the end-to-end tests run `yarn e2e`
or `yarn e2e-local` respectively.

### Backend

Set GRADLE_USER_HOME environment variable to point to <tinytask repository root>/.gradle in order to make use of the pre-cached dependencies.

The backend was generated with [Spring Initializr](https://start.spring.io/).
Run `./gradlew bootRun` for a dev server. The server is available under `http://localhost:8080/`.

Run `./gradlew test` to execute the tests.

# Fallback: Start the development environment in Gitpod
In case the application can not be started, we can use Gitpod for the interview.

Gitpod is a container-based development platform that puts developer experience first. Gitpod provisions ready-to-code developer environments in the cloud accessible through your browser (on your computer or tablet) and your local IDE.

Gitpod allows you to start developing this project without the need to configure anything on your local machine. You can either develop in your browser or within your Local VS Code IDE.

## Open a fresh workspace
Just click here to get started:
[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://gitlab.com/resourcify/tiny-tasks/-/tree/master/)

This will create a fresh workspace and install all necessary dependencies.

1. Start frontend: Go to VS Code "Run and Debug" and selected "Launch Frontend"
2. Start backend: Go to VS Code "Run and Debug" and selected "Launch Backend"

## How the candidate access and be able to modify the code

1. Conductor is responsible for setting up the GitHub Codespaces for this project. Most of it is automated (see the next bullet point), but it takes time. Make sure you do it before the interview.
When the Development Environment created in Codespaces it installs npm dependencies automatically.

2. The Conductor may need to Import the java project as it is not recognized automatically. It enables Java code navigation and intellisense in VS Code.
2.1 open VSCode and wait for it to setup everything.
2.2 Open the Java Project view (under the Explorer view on the left) and click Import Project. It will download JDK and Spring sources and javadoc. 

2.3 Conductor may run `./gradlew clean build` to make sure everything is working and dependencies are downloaded.

3. VS Code is installed with Live Share extension: Candidate can be invited with read/write access as guest to the workspace.
3.1 Open the Live Share View on the left side of the Editor: Invite participants -> invite link is created and available on the Clipboard -> send it to the Candidate.
3.2 When the candidate opens VS Code you can get a notification in which the Conductor chooses to give read-write access (bottom right corner)
