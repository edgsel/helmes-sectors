#!/bin/bash
trap 'kill 0' EXIT

export $(xargs < .env)

./gradlew :sectors-api:bootRun &
./gradlew :sectors-ui:start &

wait
