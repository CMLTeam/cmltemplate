#!/usr/bin/env bash

set -e

LATEST_ZIP="https://github.com/CMLTeam/cmltemplate/archive/master.zip?$(date +%T)"

exec 3<>/dev/tty
read_cmd="read -u 3"

declare proj_name
declare proj_descr

# TODO add validation to proj_name - should be alnum + underscore
$read_cmd -p "Please provide the project name (alnum + underscore only): " proj_name
$read_cmd -p "Please enter project description: " proj_descr

fail() {
    >&2 echo $1
    exit 1
}

proj_path="$(pwd)/$proj_name"
if [[ -e "$proj_path" ]]
then
    fail "Path $proj_path exists already."
fi

download() {
    local tmp_zip=/tmp/cmltemplate.zip

    echo
    echo "Downloading..."
    echo

    curl -sSL -o "$tmp_zip" "$LATEST_ZIP"

    local tmp_dir=/tmp/cmltemplate_out
    mkdir "$tmp_dir"

    echo
    echo "Extracting..."
    echo

    unzip -qq "$tmp_zip" -d "$tmp_dir"
    mv "$tmp_dir/cmltemplate-master" "$proj_path"

    rm -f "$tmp_zip"
    rmdir "$tmp_dir"
}

prepare() {
    echo "Processing..."

    local TPL=cmltemplate
    local FILES="
    create_db.sh
    deploy.sh
    pom.xml
    src/main/resources/application.yml
    src/main/resources/application-test.yml
    src/main/resources/application-prod.yml
    "

    cd "$proj_path"

    rm init.sh

    for f in $FILES
    do
        echo "Processing $f ..."
        sed -i "s/$TPL/$proj_name/g" $f
        if [[ "$f" == 'pom.xml' ]]
        then
            sed -i "s#<description>CML Spring Boot Template</description>#<description>$proj_descr</description>#g" $f
        fi
    done

    echo "Processing README.md ..."
    echo "# $proj_name

#### Description
$proj_descr
" > README.md

    echo "Processing $TPL.conf ..."
    mv $TPL.conf $proj_name.conf

    echo "Processing sources..."
    mv src/main/java/com/cmlteam/$TPL src/main/java/com/cmlteam/$proj_name
    for f in $(find src/ -type f -name '*.java')
    do
        echo "Processing $f ..."
        sed -i "s/com.cmlteam.$TPL/com.cmlteam.$proj_name/g" $f
    done

    echo
    echo "Congrats! Your project is ready to work."
    echo
}

do_work() {
    download
    prepare
}

# defense against partial download
do_work