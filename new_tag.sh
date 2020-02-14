#!/usr/bin/env bash

NEW_TAG=$1
LAST_TAG=$(git tag --sort=creatordate | tail -1 | sed 's/v//')

find . \( -name '*.md' -o -name 'pom.xml' \)  -exec sed -i '' s/$LAST_TAG/$NEW_TAG/g  {} \; -print
git add .
git commit -m ":bookmark: release v$NEW_TAG"
git tag v$NEW_TAG
