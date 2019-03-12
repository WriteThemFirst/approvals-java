#!/bin/bash

# Changelog.sh
#
# Script displaying a human-readable changelog from the current Git repository by parsing the Git logs.
#
# That script relies on Git commands, so `git` should be available in the PATH.
#
# It will display on the output the changelog that can then be used on GitHub for example.
#
# Gather the latest tag in the repository, along with the previous tag

latest_tag=$(git describe --tags --abbrev=0)
previous_tag=$(git describe --tags --abbrev=0 ${latest_tag}^)

# Display the changelog between the two tags

git log ${previous_tag}...${latest_tag} \
  --pretty=format:"* [%s](https://github.com/WriteThemFirst/approvals-java/commit/%H)" \
  --reverse \
  | grep ":sparkles:\|:bug:\|:boom:"
