#!/bin/bash

# file gen_signature.sh

SHORT_BOARDNAME=$1


SIGNATURE_FILE_NAME=tunerstudio/generated/signature_${SHORT_BOARDNAME}.txt
echo "Generating signature for ${SHORT_BOARDNAME}"

# generate a unique signature
date=$(date +"%Y.%m.%d")
echo "#define SIGNATURE_DATE $date" > ${SIGNATURE_FILE_NAME}
echo "#define SIGNATURE_BOARD ${SHORT_BOARDNAME}" >> ${SIGNATURE_FILE_NAME}

echo "// SIGNATURE_HASH is a built-in variable generated by ConfigDefinition.jar" >> ${SIGNATURE_FILE_NAME}

echo "#define TS_SIGNATURE \"rusEFI @@SIGNATURE_DATE@@.@@SIGNATURE_BOARD@@.@@SIGNATURE_HASH@@\"" >> ${SIGNATURE_FILE_NAME}

exit 0