#!/usr/bin/env bash
set -e

source ./creds.sh

echo "Building server..."
mvn clean package

if [ -e ./langramming.pid ]
then
  echo "Killing existing server instance..."
  kill "$(cat ./langramming.pid)" && rm ./langramming.pid
fi

echo "Migrating database..."
mvn flyway:migrate -Dflyway.user="${DB_USER}" -Dflyway.password="${DB_PASSWORD}"

echo "Starting server..."
java -jar webapp/target/langramming-webapp-*.jar &
echo $! > ./langramming.pid

echo "Successfully started up server"