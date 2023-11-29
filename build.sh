#!/bin/bash

./gradlew clean publish
rm build/bundle/io/github/pluslake/pipe/maven*
(cd build/bundle; tar -zcvf bundle.tar.gz io)
