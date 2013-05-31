#!/bin/sh

cat seeds_dataset.txt | java -cp bin main/Main $1 $2
