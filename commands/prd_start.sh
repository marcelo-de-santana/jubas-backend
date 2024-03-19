#!/bin/sh

# Commands to parse environment variables
env_file="$PWD/.env"

# Check if the .env file exists
if [ -f "$env_file" ]; then
  # Export environment variables from .env file
  while IFS= read -r line; do
      export $line
  done < "$env_file"
  echo "Environment variables added successfully."

  # Run the program
  java -jar ./jubasbackend.jar

else
    echo ".env file not found in the directory."
fi


