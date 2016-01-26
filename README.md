# SSLConnection Suite


This repo is an ongoing, evolving tutorial on SSL URL Connections via different means within Android.
The purpose of this tutorial is specifically to instruct on how to control and manipulate both an android application and a custom key store in order to increase/decrease security for an application which requries accessing a website.

This repo is intended for Educational Use - and falls under GPL.  Any copying or reproduction of code must be attributed to me - thor@asgaardianworkshop / Thor Tallmon.

## Cloning
```
https://github.com/ThorOfAsgaard/SSLConnectionTutorial.git
```

## Overview

The basic idea behind this is to lock down (or enhance) an applications ability to connect to a site using SSL.  There are Three scenarios that you might encounter which might make this useful:

1. You want to be able to connect to a site/server using a self-signed SSL Certificate
1. You want to do the above, but you don't want to add the self-signed cert into Androids main keystore
1. You want to limit the servers that the android application can connect to (and thus limiting the chance for exploits, suchs as 'Man in the Middle' attacks

## Usage

1. Use keystore/keystore.py to generate your KeyStore BKS file e.g.:

```
python keystore.py keystore.bks KeyStorePassword https://list.of, https://comma.delimited, https://urls.to, https://grab.certs.for
```

2. Copy the keystore over to you res/raw folder

3. Import this into your project as a library

4. Invoke in a similar manner:

```
   URL u = new URL("https://google.com");
   CustomKeyStoreSSLConnection cks = new CustomKeyStoreSSLConnection(getApplicationContext());
   httpsURLConnection = cks.returnConnection(u);
```


### NOTES/CAVEATS

1. **Use a new thread when doing network commands**  See MainActivity.java for an example.  Android prefers to keep the main/ui thread free of Asynchronous stuff.  You can use new Thread(new Runnable({})) to get around this.
1. Change the default **password/keystore location** if you don't use whats in the examples
1. If you want to add a whole slew of certs, you can curl the generated certs from mkcert:
    curl https://mkcert.org/generate/ > ./certs/mkcert.crt

