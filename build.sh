#!/bin/sh

mkdir -p bin
javac -d bin $(find src/* | grep "\.java")
