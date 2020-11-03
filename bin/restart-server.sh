#!/usr/bin/env bash
set -e

source "$(dirname $0)/creds.sh"

set -x
mvn clean package install

if [ -e ./langramming.pid ]
then
  kill "$(cat ./langramming.pid)" && rm ./langramming.pid
fi

mvn flyway:migrate -Dflyway.user="${DB_USER}" -Dflyway.password="${DB_PASSWORD}"

java -Dfile.encoding=UTF-8 -jar webapp/target/langramming-webapp-*.jar 2>>../error.log >>../access.log &
echo $! > ./langramming.pid

echo "Server started successfully!"
