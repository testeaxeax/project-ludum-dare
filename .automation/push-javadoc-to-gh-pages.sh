#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "testeaxeax/project-ludum-dare" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

echo -e "Publishing javadoc...\n"

  cp -R ./core/build/docs/javadoc $HOME/docs

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet --branch=gh-pages https://${GITHUB_TOKEN}@github.com/testeaxeax/project-ludum-dare gh-pages > /dev/null

  cd gh-pages
  git rm -rf ./docs
  cp -Rf $HOME/docs ./
  git add -f .
  git commit -m "Latest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published Javadoc to gh-pages.\n"
fi
