#!/bin/sh

./prd_build.sh
docker run -p 8080:8080 -it jubas-backend:latest
