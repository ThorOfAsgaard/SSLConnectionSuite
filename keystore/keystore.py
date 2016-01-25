###KeyStore Generator###
from __future__ import print_function
import ssl, os

from socket import socket

import urllib.request, sys
import subprocess

BOUNCYCASTLEURL="https://www.bouncycastle.org/download/bcprov-jdk15on-154.jar"
JAVA_HOME= os.environ['JAVA_HOME']
FILENAME=""
PASSWORD=""



def downloadBouncyCastle():
    print("Downloading Bouncey Castle from:" + BOUNCYCASTLEURL)
    urllib.request.urlretrieve(BOUNCYCASTLEURL, "bounceycastle.jar")

def createKeyStore():
    print("Creating Keystore:"+FILENAME)
    for i in os.listdir(os.getcwd() + "/certs"):
        print("Adding:" + i)
        command = "" + JAVA_HOME+"/bin/keytool -import -trustcacerts -alias '" +i+ "' -file certs/" + i + " -keystore " + FILENAME + " -storetype BKS -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath bounceycastle.jar -storepass " + PASSWORD
        print(command)
        subprocess.call(command)

    pass

def grabCerts():

    s = socket()
    print(len(sys.argv))
    for x in range(3, len(sys.argv)):
            url = sys.argv[x]
            ##Grab the cert
            print("Grabbing Cert for:" +url)
            cert = ssl.get_server_certificate((url, 443))
            f = open('certs/'+url+'.pem', 'w')
            f.write(cert)
            f.close()
            print(cert)

def tidyUp():
    global FILENAME
    print("All done, now just copy " + FILENAME + " to your /res/raw directory and you should be good to do.")

def main():
    global FILENAME
    global PASSWORD
    if len(sys.argv) < 3:
        print( "Syntax: python keystore.py filename.bks password [urls, to, grab, certs, for]")
    else:
        FILENAME = sys.argv[1]
        PASSWORD = sys.argv[2]
        downloadBouncyCastle()
        grabCerts()
        createKeyStore()
        tidyUp()


if __name__ == "__main__":
    main()




