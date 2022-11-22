#!/bin/bash

while getopts k:a:o: option
do
 case "${option}"
  in
  k) KEY=${OPTARG};;
  a) APPLICATION=${OPTARG};;
  o) HTTPMETHOD=${OPTARG};;
 esac
done

secs=$(/bin/date +%s)
TOKENEXPIRY=$(/bin/date -u +"%Y-%m-%dT%H:%M:%SZ" --date="@$((secs + 300))")

HMACDATA=$APPLICATION$HTTPMETHOD$TOKENEXPIRY

echo "HMACDATA: $HMACDATA"

HMACTOKEN=$(echo -n $HMACDATA | /usr/bin/openssl sha256 -hmac $KEY)
BEARERTOKEN=${HMACTOKEN/(stdin)=/Bearer}

echo "BEARERTOKEN: $BEARERTOKEN"

echo "TOKENEXPIRY: $TOKENEXPIRY"