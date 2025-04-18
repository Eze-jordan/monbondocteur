#!/bin/bash

docker compose down app
docker rmi monbondocteur-app:latest
git pull
docker compose up -d app
