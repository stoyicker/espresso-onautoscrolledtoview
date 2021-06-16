#!/bin/bash
set -e

VERSION=""
REVISION="origin/HEAD"

usage() {
  echo "Usage: $0 -v <version> [-r <revision=${REVISION}]" >&2
  exit 1
}

while getopts ":v:r" opt; do
  case $opt in
  v)
    VERSION=$OPTARG
    ;;
  r)
    REVISION=$OPTARG
    ;;
  \?)
    echo "Invalid option: -$OPTARG" >&2
    exit 1
    ;;
  *)
    usage
    ;;
  esac
done

if [ -z "$VERSION" ]; then
  usage
fi

git fetch &&
  git tag ${VERSION} ${REVISION} -m "${VERSION}" &&
  git push --tags &&
  echo "New release ${VERSION} for revision $(git rev-parse --short "${REVISION}") requested successfully"
