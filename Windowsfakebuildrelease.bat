export RELEASE_IDENTIFIER=pre-4.0
export INPUT_BUILD_NUMBER=TEST
export BUILD_NUMBER=NNN

cd D:\temp\martus
rem sh martus-build/buildrelease.sh (Linux way)
echo INPUT_BUILD_NUMBER=$INPUT_BUILD_NUMBER
call buildr --trace -f martus-build/buildfile test=no clean martus-client-nsis-single:build martus-client-nsis-pieces:package martus-client-nsis-upgrade:build

