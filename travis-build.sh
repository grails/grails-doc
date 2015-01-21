#!/bin/bash
set -e

git config --global user.name "$GIT_NAME"
git config --global user.email "$GIT_EMAIL"
git config --global credential.helper "store --file=~/.git-credentials"
echo "https://$GH_TOKEN:@github.com" > ~/.git-credentials


./gradlew assemble 

if [[ $TRAVIS_PULL_REQUEST == 'false' ]]; then

	git clone https://${GH_TOKEN}@github.com/grails/grails-doc.git -b gh-pages gh-pages --single-branch > /dev/null
	cd gh-pages

	# If this is the master branch then update the snapshot
	if [[ $TRAVIS_BRANCH == 'master' ]]; then
		mkdir -p snapshot
		cp -r ../build/docs/. ./snapshot/

		git add snapshot/*

		# If there is a tag present then this becomes the latest
		if [[ -n $TRAVIS_TAG ]]; then
			git rm -rf latest/
			mkdir -p latest
			cp -r ../build/docs/. ./latest/
			git add latest/*

			version="$TRAVIS_TAG"
			version=${version:1}
			majorVersion=${version:0:4}
			majorVersion="${majorVersion}x"

			mkdir -p "$version"
			cp -r ../build/docs/. "./$version/"
			git add "$version/*"			

			mkdir -p "$majorVersion"
			cp -r ../build/docs/. "./$majorVersion/"
			git add "$majorVersion/*"						

		fi		

		git commit -a -m "Updating docs for Travis build: https://travis-ci.org/$TRAVIS_REPO_SLUG/builds/$TRAVIS_BUILD_ID"
		git push origin HEAD
		cd ..
		rm -rf gh-pages

	fi
fi
