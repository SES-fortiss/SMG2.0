#!/usr/bin/env python
# encoding: utf-8
# @author Sebastian Wilzbach <sebi@wilzbach.me>

import hmac
import random
import time
import urllib.parse
import base64
import sys
import hashlib

def gen_smg_url(urlInput,accessKey, secretKey):
    """Generates a valid smg REST url

    :url: URL to sign
    :akey: your access key
    :skey: your secret key
    :returns: a valid URL with signature

    """
    if "?" in urlInput:
        url = urlInput + "&accesskey=" + accessKey;
    else:
        url = urlInput + "?accesskey=" + accessKey;

    url = url + "&timestamp=" + str(int(time.time())) + "&authversion=1" + "&nonce=" +str(random.randint(0,100000000000));

    sig = gen_sig(secretKey, url)
    return url + "&signature=" +urllib.parse.quote(sig);

def gen_sig(key, data):
    """ Generates a HMAC RFC-2104 signature

    :key: your key
    :data: string to sign
    """
    signature = hmac.new(key.encode('utf-8'), data.encode('utf-8'), hashlib.sha1)

    sig =   signature.digest()
    # base64 encode
    b64 = base64.b64encode( sig)
    # url encode
    return b64


print( gen_smg_url(sys.argv[1],"68a45057-5734-4dad-9f86-ab9e32c4506e", "jg9e65dui5272c45uds3qrf3b8gc71crjq4raq43") )
