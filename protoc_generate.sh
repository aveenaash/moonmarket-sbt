###########################################################################
##### https://developers.google.com/protocol-buffers/docs/javatutorial#####
###########################################################################

SRC_DIR="src/main/resources"
DST_DIR="src/main/scala/code/model"

echo "Generating Java classes to $DST_DIR"
echo "..................................."
protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/moonmarket.proto
#protoc -I=src/main/resources --java_out=src/main/scala/code/model src/main/resources/moonmarket.proto
echo "Generating Java classes to $DST_DIR completed."
