#!/bin/bash

SCRIPTDIR=$(dirname $0)
echo "SCRIPTDIR: $SCRIPTDIR"

PROJECTDIR="$SCRIPTDIR/../"
echo "PROJECTDIR: $PROJECTDIR"

BUNDLENAME="$1"

BUNDLENAMEL=`echo "$BUNDLENAME" | tr '[:upper:]' '[:lower:]'`
echo "Building Projects for Bundle: $BUNDLENAME / $BUNDLENAMEL"
echo

if [[ -z "$BUNDLENAME" ]]; then
	echo "No Bundlename given."	
	exit 4;
fi

if [[ -d "$PROJECTDIR/$BUNDLENAMEL.api" || -d  "$PROJECTDIR/$BUNDLENAMEL.impl" || -d "$PROJECTDIR/$BUNDLENAMEL.test"   ]]; then
	echo -e "Bundle aleady exists.\nAborting ..."	
	exit 5;
fi


# init dirs

mkdir -p "$PROJECTDIR/$BUNDLENAMEL".api/src/main/java/org/fortiss/smg/"$BUNDLENAMEL"/api
mkdir -p "$PROJECTDIR/$BUNDLENAMEL".impl/src/main/java/org/fortiss/smg/"$BUNDLENAMEL"/impl
mkdir -p "$PROJECTDIR/$BUNDLENAMEL".test/src/test/java/org/fortiss/smg/"$BUNDLENAMEL"/test


NAME="$PROJECTDIR"/"$BUNDLENAMEL"
# copy templates
cp "$SCRIPTDIR"/templates/api/pom.xml "$NAME".api/
cp "$SCRIPTDIR"/templates/impl/pom.xml "$NAME".impl/
cp "$SCRIPTDIR"/templates/test/pom.xml "$NAME".test/

# Api
cp "$SCRIPTDIR"/templates/api/MyService.java $NAME.api/src/main/java/org/fortiss/smg/"$BUNDLENAMEL"/api/"$BUNDLENAME"Interface.java
cp "$SCRIPTDIR"/templates/api/QueueNames.java $NAME.api/src/main/java/org/fortiss/smg/"$BUNDLENAMEL"/api/"$BUNDLENAME"QueueNames.java

# Impl
cp "$SCRIPTDIR"/templates/impl/Activator.java $NAME.impl/src/main/java/org/fortiss/smg/"$BUNDLENAMEL"/impl/"$BUNDLENAME"Activator.java
cp "$SCRIPTDIR"/templates/impl/Impl.java $NAME.impl/src/main/java/org/fortiss/smg/"$BUNDLENAMEL"/impl/"$BUNDLENAME"Impl.java

#Test
cp "$SCRIPTDIR"/templates/test/TestCase.java $NAME.test/src/test/java/org/fortiss/smg/"$BUNDLENAMEL"/test/Test"$BUNDLENAME"Simple.java

cp "$SCRIPTDIR"/templates/packageinfo $NAME.api/src/main/java/org/fortiss/smg/"$BUNDLENAMEL"/api/
cp "$SCRIPTDIR"/templates/packageinfo $NAME.impl/src/main/java/org/fortiss/smg/"$BUNDLENAMEL"/impl/
cp "$SCRIPTDIR"/templates/packageinfo $NAME.test/src/test/java/org/fortiss/smg/"$BUNDLENAMEL"/test/

# arrage bundle variables
find "$NAME".* -type f -print0 | xargs -0 sed -i "s/#Bundle#/$BUNDLENAME/g"
find "$NAME".* -type f -print0 | xargs -0 sed -i "s/#bundle#/$BUNDLENAMEL/g"

# add bundles to the pom
while true; do
    read -p "Do you wish to add your bundle to the global pom.xml? [yn]" yn
    case $yn in
        [Yy]* ) 
			sed -i.bak  "s|.*DYNAMIC-INSERT-POINT.*|\t\t<module>$BUNDLENAMEL.api</module>\n\t\t<module>$BUNDLENAMEL.impl</module>\n\t\t<module>$BUNDLENAMEL.test</module>\n\t\t<!-- DYNAMIC-INSERT-POINT DO NOT REMOVE -->|"  "$PROJECTDIR/pom.xml" 
			rm "$PROJECTDIR/pom.xml.bak"
        	
        	break;;
        [Nn]* ) 
        	echo "Add your bundles manually."
        	exit;;
        * ) echo "Please answer [y]es or [n]o.";;
    esac
done

echo "Init of bundle successful"
echo "Don't forget to run 'mvn install' before you import your bundles to eclipse"
