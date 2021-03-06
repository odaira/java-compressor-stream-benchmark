package net.odaira.jcsb;

/*
 * Copyright 2020 Rei Odaira
 *
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
import java.io.IOException;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4SafeDecompressor;
import net.jpountz.xxhash.XXHashFactory;
import net.jpountz.xxhash.XXHash32;
import net.jpountz.lz4.LZ4FrameOutputStream;
import net.jpountz.lz4.LZ4FrameInputStream;

public class LZ4FrameJNIDriver extends Driver {
    @Override
    public String getDescription() {
	return "LZ4FrameOutputStream and LZ4FrameInputStream";
    }

    private final LZ4Compressor compressor = LZ4Factory.nativeInstance().fastCompressor();
    private final XXHash32 outputChecksum = XXHashFactory.nativeInstance().hash32();
    private final LZ4SafeDecompressor decompressor = LZ4Factory.nativeInstance().safeDecompressor();
    private final XXHash32 inputChecksum = XXHashFactory.nativeInstance().hash32();

    @Override
    public OutputStream allocateOutputStream(final OutputStream out) throws IOException {
        LZ4FrameOutputStream.FLG.Bits[] checksumFlags = getChecksumFlags();
        LZ4FrameOutputStream.FLG.Bits[] flags;
        if (checksumFlags != null) {
            flags = new LZ4FrameOutputStream.FLG.Bits[checksumFlags.length + 1];
            flags[0] = LZ4FrameOutputStream.FLG.Bits.BLOCK_INDEPENDENCE;
            System.arraycopy(checksumFlags, 0, flags, 1, checksumFlags.length);
        } else {
            flags = new LZ4FrameOutputStream.FLG.Bits[] { LZ4FrameOutputStream.FLG.Bits.BLOCK_INDEPENDENCE };
        }
        return new LZ4FrameOutputStream(out, LZ4FrameOutputStream.BLOCKSIZE.SIZE_64KB, -1L, compressor, outputChecksum, flags);
    }

    protected LZ4FrameOutputStream.FLG.Bits[] getChecksumFlags() {
        return null;
    }

    @Override
    public InputStream allocateInputStream(final InputStream in) throws IOException {
	return new LZ4FrameInputStream(in, decompressor, inputChecksum);
    }
}
