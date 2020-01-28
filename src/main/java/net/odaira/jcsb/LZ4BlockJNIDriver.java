package net.odaira.jcsb;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.OutputStream;
import java.io.InputStream;
import java.util.zip.Checksum;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4FastDecompressor;
import net.jpountz.xxhash.XXHashFactory;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4BlockInputStream;

public class LZ4BlockJNIDriver extends Driver {
    private static final int DEFAULT_SEED = 0x9747b28c;
    private final LZ4Compressor compressor = LZ4Factory.nativeInstance().fastCompressor();
    private final Checksum outputChecksum = XXHashFactory.nativeInstance().newStreamingHash32(DEFAULT_SEED).asChecksum();
    private final LZ4FastDecompressor decompressor = LZ4Factory.nativeInstance().fastDecompressor();
    private final Checksum inputChecksum = XXHashFactory.nativeInstance().newStreamingHash32(DEFAULT_SEED).asChecksum();

    @Override
    public OutputStream allocateOutputStream(final OutputStream out) {
	return new LZ4BlockOutputStream(out, 1 << 16, compressor, outputChecksum, false);
    }

    @Override
    public InputStream allocateInputStream(final InputStream in) {
	return new LZ4BlockInputStream(in, decompressor, inputChecksum, true);
    }
}
