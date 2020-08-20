#!/usr/bin/env bash -e

source ./creds.sh

mvn clean package

if [ -e ./langramming.pid ]
then
  kill "$(cat ./langramming.pid)" && rm ./langramming.pid
fi

mvn flyway:migrate -Dflyway.user="${DB_USER}" -Dflyway.password="${DB_PASSWORD}"

java -jar webapp/target/langramming-webapp-*.jar &
echo $! > ./langramming.pid
