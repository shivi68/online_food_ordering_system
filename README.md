Online Food Ordering System

Overview

This is a simple web application for online food ordering, built using Java and containerized using Docker. The application is defined as a multi-container application using Docker Compose, with a database service and a web application service.

Requirements

- Docker 20.10 or later
- Docker Compose 1.29 or later
- Linux command-line knowledge
- GitHub account

Setup and Deployment

1. Install Docker and Docker Compose
    - Install Docker and Docker Compose on your machine
2. Verify Installation
    - Run a test container to verify the installation
3. Create and Build Docker Image
    - Create a Dockerfile to containerize the application
    - Build the Docker image using sudo docker build -t online_food_ordering .
4. Run Container
    - Run a container from the Docker image using docker run
5. Docker Commands
    - List, start, stop, and remove containers using Docker commands
    - Inspect running containers and view logs
6. Docker Compose
    - Create a docker-compose.yml file to define the multi-container application
    - Bring up the application using docker-compose up -d
    - Verify that all services are running correctly using docker-compose ps and docker-compose logs
7. Private Docker Registry
    - Create a private Docker registry or use Docker Hub
    - Push Docker images to the registry using docker push
    - Pull Docker images from the registry using docker pull
    - Run the pulled Docker image using docker run

Running the Application

1. Build and Start Containers
    - Build and start the containers using docker-compose up --build
2. Verify APIs
    - Check the APIs using Postman

GitHub Repository

This project is hosted on GitHub at 
https://github.com/shivi68/online_food_ordering_system.git
