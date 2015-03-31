#!/bin/bash
set -e

git config --global user.name "$GIT_NAME"
git config --global user.email "$GIT_EMAIL"
git config --global credential.helper "store --file=~/.git-credentials"
echo "https://$GH_TOKEN:@github.com" > ~/.git-credentials

export GRADLE_OPTS="-Xmx2048m -Xms256m -XX:MaxPermSize=512m -XX:+CMSClassUnloadingEnabled -XX:+HeapDumpOnOutOfMemoryError"

./gradlew assemble --info --stacktrace

if [[ $TRAVIS_PULL_REQUEST == 'false' ]]; then

			git rm -rf latest/
			mkdir -p latest
			cp -r ../build/docs/. ./latest/
			git add latest/*

fi
