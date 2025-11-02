#!/bin/bash

set -e  # exit immediately if a command fails

TEST_FLAG=false

# parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --test|-t)
            TEST_FLAG=true
            shift
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done

TMP_DIR=$(mktemp -d)
trap "rm -rf $TMP_DIR" EXIT # temp directory deleted on script exit

echo "Compiling Java files into $TMP_DIR..."
javac -d "$TMP_DIR" -cp "libs/*" $(find . -name "*.java")

echo ""

if [ "$TEST_FLAG" = true ]; then
    echo "Running Tests"
    java -cp "$TMP_DIR:libs/*" Program true
else
    echo "Running Program"
    java -cp "$TMP_DIR:libs/*" Program
fi

echo ""