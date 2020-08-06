# Overview
This project provides a performance benchmark suite for stream-based APIs of compression algorithms in Java. A benchmark suite for block-based APIs is provided by [jvm-compressor-benchmark](https://github.com/ning/jvm-compressor-benchmark).

# How to build
```
$ mvn package
```

# How to run
Help:
```
$ ./run.sh -h
usage: run.sh [options] <config>
 -e         explain the drivers and tests and then exit
 -h         print help
 -r <arg>   specify run time in seconds (default 30 seconds)
 -s         skip sanity check
 -w <arg>   specify warmup time in seconds (default 10 seconds)
```
Explain:
```
$ ./run.sh -e cfg/tests.yml
---------------
net.odaira.jcsb.LZ4BlockChecksumJNIDriver
  LZ4BlockOutputStream and LZ4BlockInputStream with checksum (checksum cannot be disabled)
net.odaira.jcsb.LZ4FrameJNIDriver
  LZ4FrameOutputStream and LZ4FrameInputStream
net.odaira.jcsb.LZ4FrameBlockChecksumJNIDriver
  LZ4FrameOutputStream and LZ4FrameInputStream with block checksum
net.odaira.jcsb.LZ4FrameContentChecksumJNIDriver
  LZ4FrameOutputStream and LZ4FrameInputStream with content checksum
net.odaira.jcsb.LZ4FrameBlockContentChecksumJNIDriver
  LZ4FrameOutputStream and LZ4FrameInputStream with block and content checksum
net.odaira.jcsb.SnappyDriver
  SnappyOutputStream and SnappyInputStream (checksum is not supported)
net.odaira.jcsb.SnappyFramedDriver
  SnappyFramedOutputStream and SnappyFramedInputStream
net.odaira.jcsb.SnappyFramedChecksumDriver
  SnappyFramedOutputStream and SnappyFramedInputStream with checksum
net.odaira.jcsb.ZstdDriver
  ZstdOutputStream and ZstdInputStream
net.odaira.jcsb.ZstdChecksumDriver
  ZstdOutputStream and ZstdInputStream with checksum
net.odaira.jcsb.GZIPChecksumDriver
  GZIPOutputStream and GZIPInputStream with checksum (checksum cannot be disabled)
---------------
net.odaira.jcsb.InputStreamAllocationTest
  Allocate an input stream instance
net.odaira.jcsb.OutputStreamAllocationTest
  Allocate an output stream instance
net.odaira.jcsb.WriteByteArrayTest
  Write by single write(byte[])
net.odaira.jcsb.WriteByteTest
  Write by write(int)
net.odaira.jcsb.WriteByteArray512Test
  Write by write(byte[], int, int) 512 bytes each time
net.odaira.jcsb.ReadByteArray4096Test
  Read by read(byte[], int, int) 4096 bytes each time
net.odaira.jcsb.ReadByteTest
  Read by read()
net.odaira.jcsb.Skip512Test
  Skip by skip(long) 512 bytes each time
net.odaira.jcsb.SizeTest
  Return the size of the compressed test data
```
Run:
```
$ ./run.sh cfg/tests.yml
LZ4BlockChecksumJNIDriver	InputStreamAllocationTest	InstanceAllocation	21119229.1	ops
LZ4BlockChecksumJNIDriver	OutputStreamAllocationTest	InstanceAllocation	148709.4	ops
LZ4BlockChecksumJNIDriver	WriteByteArrayTest	testdata/calgary/bib	2656.3	ops
LZ4BlockChecksumJNIDriver	WriteByteArrayTest	testdata/calgary/book1	308.0	ops
LZ4BlockChecksumJNIDriver	WriteByteArrayTest	testdata/calgary/book2	452.9	ops
LZ4BlockChecksumJNIDriver	WriteByteArrayTest	testdata/calgary/geo	4270.3	ops
LZ4BlockChecksumJNIDriver	WriteByteTest	testdata/calgary/bib	1711.6	ops
...
```
Edit cfg/tests.yml to run only specific drivers, tests, or test data.

The run.sh script recognizes the JAVA_HOME and JAVA_OPTS envorinment variables. It is recommended to specify a suffciently large size of the Java heap.
```
$ env JAVA_OPTS="-Xms300m -Xmx300m" ./run.sh cfg/tests.yml
```
