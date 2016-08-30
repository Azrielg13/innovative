#!/bin/sh
#/usr/local/bin/protoc --proto_path=/Users/eddiemay/Documents/Projects/common/src/conf --proto_path=/usr/local/include --js_out=/Users/eddiemay/Documents/Projects/common/src-gen/js /Users/eddiemay/Documents/Projects/common/src/conf/common_ui.proto
../common/bin/protoc --proto_path=../common/src/conf --proto_path=src/conf --js_out=src-gen/js src/conf/iis_ui.proto
