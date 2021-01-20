#!/bin/bash
# $1 == GH_TOKEN
# $2 == GORM Version

grails_version="$2"
echo -n "Updating Grails version to: $grails_version"

if [ -z "$GIT_USER_EMAIL" ]; then
    GIT_USER_EMAIL="${GITHUB_ACTOR}@users.noreply.github.com"
fi

if [ -z "$GIT_USER_NAME" ]; then
   GIT_USER_NAME="${GITHUB_ACTOR}"
fi

echo "Configuring git"
git config --global user.email "$GIT_USER_EMAIL"
git config --global user.name "$GIT_USER_NAME"

git checkout $TARGET_BRANCH
git pull origin $TARGET_BRANCH

echo "Setting release version in gradle.properties"
sed -i "s/^grailsVersion.*$/grailsVersion\=${grails_version}/" gradle.properties
cat gradle.properties

echo "Pushing release version and recreating v${grails_version} tag"
git add gradle.properties
git commit -m "Release v${grails_version} docs"
git push origin $TARGET_BRANCH
git push origin :refs/tags/v${grails_version}
git tag -fa v${grails_version} -m "Release v${grails_version} docs"
git push origin $TARGET_BRANCH --tags
