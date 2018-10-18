#!/usr/bin/env bash

set -e

LATEST_ZIP=https://github.com/CMLTeam/cmltemplate/archive/master.zip

exec 3<>/dev/tty
read_cmd="read -u 3"

declare proj_name
declare proj_descr

# TODO add validation to proj_name - should be alnum + underscore
$read_cmd -p "Please provide the project name: " proj_name
$read_cmd -p "Please enter project description: " proj_descr

proj_path="$(pwd)/$proj_name"
if [ -e "$proj_path" ]
then
    fail "Path $proj_path exists already."
fi

fail() {
    >&2 echo $1
    exit 1
}

download() {
    local tmp_zip=/tmp/cmltemplate.zip
    curl -o "$tmp_zip" -SL "$LATEST_ZIP"

    local tmp_dir=/tmp/cmltemplate_out
    mkdir "$tmp_dir"
    unzip "$tmp_zip" -d "$tmp_dir"
    mv "$tmp_dir/cmltemplate-master" "$proj_path"

    rm -f "$tmp_zip"
    rmdir "$tmp_dir"
}

prepare_project() {
    echo "Processing..."

    TPL=cmltemplate
    FILES="
    create_db.sh
    deploy.sh
    pom.xml
    src/main/resources/application.properties
    "

    DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
    cd $DIR

    for f in $FILES
    do
        echo "Processing $f ..."
        sed -i "s/$TPL/$proj_name/g" $f
        if [ "$f" == 'pom.xml' ]
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
    git mv $TPL.conf $proj_name.conf

    echo "Processing sources..."
    git mv src/main/java/com/cmlteam/$TPL src/main/java/com/cmlteam/$proj_name
    for f in $(find src/ -type f -name '*.java')
    do
        echo "Processing $f ..."
        sed -i "s/com.cmlteam.$TPL/com.cmlteam.$proj_name/g" $f
    done

    echo
    echo "DONE! Please check the change carefully before committing!"
    echo "Also it's advised to remove this script ($0) if all's good."
    echo
}

do_work() {
    download
}

# defense against partial download
do_work