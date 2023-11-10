```markdown
# ARKBankBE - Backend for ARK-Bank

ARKBankBE is the backend for the ARK-Bank website project, developed using Spring Boot.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Running the Backend](#running-the-backend)
- [ARKBankFE - Frontend](#arkbankfe-frontend)
- [Accessing the Application](#accessing-the-application)
- [Additional Configuration](#additional-configuration)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Prerequisites

Make sure you have the following installed:

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html)

## Running the Backend

1. Navigate to the `ARKBankBE` directory:

   ```bash
   cd ARKBankBE
   ```

2. Build and run the Java application:

   ```bash
   ./mvn spring-boot:run
   ```

   or if you are on Windows:

   ```bash
   mvn spring-boot:run
   ```

   This will start the backend server at `http://localhost:8080`.

## ARKBankFE - Frontend

The frontend for ARK-Bank is developed using Angular.

### Prerequisites

Make sure you have the following installed:

- [Node.js and npm](https://nodejs.org/)
- [Angular CLI](https://cli.angular.io/)

### Running the Frontend

1. Navigate to the `ARKBankFE` directory:

   ```bash
   cd ARKBankFE
   ```

2. Install the required dependencies:

   ```bash
   npm install
   ```

3. Start the Angular development server:

   ```bash
   ng serve
   ```

   The frontend will be accessible at `http://localhost:4200`.

## Accessing the Application

Open your web browser and go to `http://localhost:4200` to access the ARK-Bank application.

## Additional Configuration

If your backend server or frontend development server uses different ports or if you have additional configuration options, mention them here.

## Troubleshooting

If you encounter any issues during setup or running the application, refer to the troubleshooting section in the documentation.

## Contributing

If you would like to contribute to the ARK-Bank project, please follow our [contribution guidelines](CONTRIBUTING.md).

## License

This project is licensed under the [MIT License](LICENSE).
