#!/bin/bash    

cd flute-3.1
./rand-pts -r $1 | ./flute-net
