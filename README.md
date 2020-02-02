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
 -d         disable checksum if possible
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
net.odaira.jcsb.LZ4BlockJNIDriver
  LZ4BlockOutputStream and LZ4BlockInputStream (checksum cannot be disabled)
net.odaira.jcsb.LZ4FrameJNIDriver
  LZ4FrameOutputStream and LZ4FrameInputStream
net.odaira.jcsb.SnappyDriver
  SnappyOutputStream and SnappyInputStream
net.odaira.jcsb.SnappyFramedDriver
  SnappyFramedOutputStream and SnappyFramedInputStream
net.odaira.jcsb.ZstdDriver
  ZstdOutputStream and ZstdInputStream
net.odaira.jcsb.GZIPDriver
  GZIPOutputStream and GZIPInputStream (checksum cannot be disabled)
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
LZ4BlockJNIDriver	InputStreamAllocationTest	InstanceAllocation	21119229.1	ops
LZ4BlockJNIDriver	OutputStreamAllocationTest	InstanceAllocation	148709.4	ops
LZ4BlockJNIDriver	WriteByteArrayTest	testdata/calgary/bib	2656.3	ops
LZ4BlockJNIDriver	WriteByteArrayTest	testdata/calgary/book1	308.0	ops
LZ4BlockJNIDriver	WriteByteArrayTest	testdata/calgary/book2	452.9	ops
LZ4BlockJNIDriver	WriteByteArrayTest	testdata/calgary/geo	4270.3	ops
LZ4BlockJNIDriver	WriteByteTest	testdata/calgary/bib	1711.6	ops
...
```
Edit cfg/tests.yml to run only specific drivers, tests, or test data.
