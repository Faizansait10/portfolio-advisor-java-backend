# Project Title: Personalized Investment Portfolio Advisor

## Overview
This project is a console-based backend system that provides personalized investment portfolio recommendations to users. It integrates a Java backend for application logic and data persistence with a Python-based Machine Learning service for predicting user risk tolerance.

## Key Features
* **User Management:** Secure user registration and login.
* **ML-driven Risk Profiling:** Uses a trained Machine Learning model to predict a user's risk tolerance (Conservative, Moderate, Aggressive) based on their profile data (age, income, goals, etc.).
* **Dynamic Portfolio Allocation:** Generates a recommended asset allocation (e.g., percentage in stocks, bonds) based on the ML-predicted risk profile.
* **User History:** Stores and retrieves a history of all user-specific recommendations in a relational database.

## Technical Architecture
The project follows a layered architecture:
* **Presentation Layer:** A console-based Java application for user interaction.
* **Service Layer:** Java classes that contain the business logic, orchestrating database calls and API interactions.
* **Data Access Layer (DAO):** Java classes that handle direct communication with the MySQL database using JDBC.
* **ML Layer:** A Python Flask API that loads a pre-trained `scikit-learn` model to serve risk predictions.

## Tech Stack
* **Backend:** Java (JDK 23), Maven
* **Database:** MySQL, JDBC
* **Machine Learning:** Python (3.12+), Flask, scikit-learn, Pandas, NumPy
* **Version Control:** Git, GitHub

## Getting Started (How to run the project)
1.  **Clone the repositories:**
    ```bash
    git clone [https://github.com/FaizanSait/portfolio-advisor-java-backend.git](https://github.com/FaizanSait/portfolio-advisor-java-backend.git)
    git clone [https://github.com/FaizanSait/risk-profiling-ml-service.git](https://github.com/FaizanSait/risk-profiling-ml-service.git)
    ```
2.  **Database Setup:**
    * Ensure MySQL Server is running.
    * In MySQL Workbench, run the `schema.sql` script to create the necessary tables.
    * Update the `database.properties` file in the `portfolio-advisor-java-backend` project with your credentials.
3.  **Run the Python ML Service:**
    * Navigate to the `risk-profiling-ml-service` directory.
    * Set up and activate your virtual environment.
    * Install Python dependencies (`pip install -r requirements.txt`).
    * Run the data generation and model training scripts: `python generate_data.py` and `python train_model.py`.
    * Start the Flask server: `flask --app app.py run --port 5000`
4.  **Run the Java Backend:**
    * Open the `portfolio-advisor-java-backend` project in IntelliJ IDEA.
    * Run the `PortfolioAdvisorApp` class.

## Key Learnings
* Implemented a **full-stack application combining Java and Python**.
* Designed and implemented a **layered architecture (DAO, Service, Presentation)**.
* Gained hands-on experience in **API integration between different technologies (Java and Python)**.
* Developed a **Machine Learning pipeline** for predictive analytics in a business context.
* Practiced **robust database design and management** using JDBC and MySQL.
