#!/usr/bin/env bash

read -p "Please provide the project name: " proj_name
read -p "Please enter project description: " proj_descr

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


