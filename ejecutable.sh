#!/bin/sh
BASEDIR=`dirname $0`
exec java \
     -XstartOnFirstThread \
     -jar netclip_macOsx_64.jar