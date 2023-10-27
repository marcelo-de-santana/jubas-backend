#!/bin/sh

# Commands to build the Docker image from Dockerfile
env_file=".env"

# Check if the .env file exists
if [ -f "$env_file" ]; then
    # Export environment variables
    while IFS= read -r line; do
        export "$line"
    done < "$env_file"
    echo "Environment variables added successfully."
else
    echo ".env file not found in the directory."
fi

# Compile the project
./mvnw clean install

# Run the application
java -jar ./target/jubasbackend-0.0.1-SNAPSHOT.jar