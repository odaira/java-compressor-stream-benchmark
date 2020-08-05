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

import net.jpountz.lz4.LZ4FrameOutputStream;

public class LZ4FrameBlockContentChecksumJNIDriver extends LZ4FrameJNIDriver {
    @Override
    public String getDescription() {
	return "LZ4FrameOutputStream and LZ4FrameInputStream with block and content checksum";
    }

    protected LZ4FrameOutputStream.FLG.Bits[] getChecksumFlags() {
        return new LZ4FrameOutputStream.FLG.Bits[] { LZ4FrameOutputStream.FLG.Bits.BLOCK_CHECKSUM, LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM };
    }
}
