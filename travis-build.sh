#!/bin/bash
set -e

git config --global user.name "$GIT_NAME"
git config --global user.email "$GIT_EMAIL"
git config --global credential.helper "store --file=~/.git-credentials"
echo "https://$GH_TOKEN:@github.com" > ~/.git-credentials

export GRADLE_OPTS="-Xmx2048m -Xms256m -XX:MaxPermSize=512m -XX:+CMSClassUnloadingEnabled -XX:+HeapDumpOnOutOfMemoryError"
EXIT_STATUS=0

if [[ $TRAVIS_BRANCH == 'master' ]]; then
  echo "Don't publish docs because branch is master"
  exit $EXIT_STATUS
fi

if [[ $TRAVIS_PULL_REQUEST == 'false' ]]; then

  # If there is a tag present then this becomes the latest
  if [[ -n $TRAVIS_TAG ]]; then

    ./gradlew assemble --info --stacktrace

    git clone https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git -b gh-pages gh-pages --single-branch > /dev/null
    cd gh-pages

    version="$TRAVIS_TAG"
    version=${version:1}
    zipName="grails-docs-$version"
    export RELEASE_FILE="${zipName}.zip"

    publishLatest=true
    if $publishLatest ; then
      git rm -rf latest/
      mkdir -p latest
      cp -r ../build/docs/. ./latest/
      git add latest/*
    fi

    majorVersion=${version:0:4}
    majorVersion="${majorVersion}x"

    mkdir -p "$version"
    cp -r ../build/docs/. "./$version/"
    git add "$version/*"

    mkdir -p "$majorVersion"
    cp -r ../build/docs/. "./$majorVersion/"
    git add "$majorVersion/*"
    git commit -a -m "Updating docs for Travis build: https://travis-ci.org/$TRAVIS_REPO_SLUG/builds/$TRAVIS_BUILD_ID"
    git push origin HEAD
    cd ..
    rm -rf gh-pages
  else
 
    ./gradlew assemble -x apiDocs --info --stacktrace

    git clone https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git -b gh-pages gh-pages --single-branch > /dev/null
    cd gh-pages

    git rm -rf snapshot/
    mkdir -p snapshot
    cp -r ../build/docs/. ./snapshot
    git add snapshot/*

   git commit -a -m "Updating snapshot docs for Travis build: https://travis-ci.org/$TRAVIS_REPO_SLUG/builds/$TRAVIS_BUILD_ID"
   git push origin HEAD
 
  fi
fi
