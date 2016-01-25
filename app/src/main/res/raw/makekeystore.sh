#!/bin/bash


BCJAR=bcprov-jdk15on-154.jar

#openssl s_client -showcerts -connect tx-tss.caltesting.org:443 </dev/null 2>/dev/null|openssl x509 -outform PEM > tx-tss.pem


TRUSTSTORE=$1
TRUSTPASS=$2
#CURLFROM=$2

if [ "$#" -ne 1]
then
	echo "USAGE:..."
	exit 1
fi

#curl https://mkcert.org/generate/ > ./certs/mkcert.crt


for FILE in certs/*.*
do
    echo $FILE
    CACERT=$FILE
    TRUSTSTORE=$1
    ALIAS=`openssl x509 -inform PEM -subject_hash -noout -in $CACERT`
    echo "ALIAS $ALIAS"
   # if [ -f $TRUSTSTORE ]; then
   #     rm $TRUSTSTORE || exit 1
   # fi
    echo "Adding $FILE to $TRUSTSTORE..."
    keytool -import -v -trustcacerts -alias $ALIAS \
          -file $CACERT \
          -keystore $TRUSTSTORE -storetype BKS \
          -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider \
          -providerpath $BCJAR \
          -storepass $2
    echo ""
    echo "Added '$CACERT' with alias '$ALIAS' to $TRUSTSTORE..."
done
