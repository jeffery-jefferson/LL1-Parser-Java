#!/bin/bash

set -e  # exit immediately if a command fails

TEST_FLAG=false

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

OUT_DIR="./out"

echo ""

if [ "$TEST_FLAG" = true ]; then
    echo "Running Tests"
    java -cp "$OUT_DIR:libs/*" Impl/Program true
else
    echo "Running Program"
    java -cp "$OUT_DIR:libs/*" Impl/Program
fi

echo ""
