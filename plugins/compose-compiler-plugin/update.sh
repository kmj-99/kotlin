#!/bin/bash

#
# Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
# Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
#

set -e # Exit if one of commands exit with non-zero exit code
set -u # Treat unset variables and parameters other than the special parameters ‘@’ or ‘*’ as an error

if [ $# -ne 1 ]; then
    echo "Usage update /path/to/androidx"
    exit 1
fi

kotlinRepoDir=$(realpath "$(pwd)/../../")
androidxRepoDir=$(realpath "$1")

echo "Updating compose compiler plugin from $androidxRepoDir to $kotlinRepoDir"

androidxRepoComposeDir="$androidxRepoDir/compose"
kotlinComposeDir="$kotlinRepoDir/plugins/compose-compiler-plugin"

rm -rf $kotlinComposeDir/compiler-daemon/integration-tests/src
cp -R $androidxRepoComposeDir/compiler/compiler-daemon/integration-tests/src $kotlinComposeDir/compiler-daemon/integration-tests

rm -rf $kotlinComposeDir/compiler-daemon/src
cp -R $androidxRepoComposeDir/compiler/compiler-daemon/src $kotlinComposeDir/compiler-daemon

rm -rf $kotlinComposeDir/compiler-hosted/src
cp -R $androidxRepoComposeDir/compiler/compiler-hosted/src $kotlinComposeDir/compiler-hosted