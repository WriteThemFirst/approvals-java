#!/usr/bin/env bash

NEW_TAG=${1:-$(gum input --placeholder v1.2.3)}
LAST_TAG=$(git tag --sort=creatordate | tail -1 | sed 's/v//')

find . \( -name '*.md' -o -name 'pom.xml' \)  -exec sed -i '' s/$LAST_TAG/$NEW_TAG/g  {} \; -print
git add .
git commit -m ":bookmark: release $NEW_TAG"
git tag $NEW_TAG
