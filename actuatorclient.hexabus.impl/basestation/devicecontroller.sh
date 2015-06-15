#!bin/bash

if [ $2 = "on" ] 
then
   echo on
   hexaswitch -i $1 -c on
elif [ $2 = "off" ]
then
   echo off
   hexaswitch -i $1 -c off 
fi
