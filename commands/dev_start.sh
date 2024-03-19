#!/bin/sh

# Check if the container already exists
if docker ps -a --format '{{.Names}}' | grep -q "jubas-backend"; then
    echo "The jubas-backend container already exists."
else
    echo "The jubas-backend container does not exist. Creating..."

    # Commands to parse environment variables
    env_file=".env"

    # Check if the .env file exists
    if [ -f "$env_file" ]; then
        # Export environment variables from .env file
        while IFS= read -r line; do
            export $line
        done < "$env_file"
        echo "Environment variables added successfully."
    else
        echo ".env file not found in the directory."
    fi

    # Compile the project (skipping tests)
    ./mvnw clean install \
      -DskipTests=true \
      -Dmaven.test.skip=true \
      -Dmaven.test.dependency.skip=true

    # Run the application
    java -jar ./target/jubasbackend-0.0.1-SNAPSHOT.jar
fi
