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

import org.xerial.snappy.SnappyFramedOutputStream;
import org.xerial.snappy.SnappyFramedInputStream;

public class SnappyFramedDriver extends Driver {
    @Override
    public String getDescription() {
	return "SnappyFramedOutputStream and SnappyFramedInputStream";
    }

    @Override
    public OutputStream allocateOutputStream(final OutputStream out) throws IOException {
	return new SnappyFramedOutputStream(out, 64 * 1024, SnappyFramedOutputStream.DEFAULT_MIN_COMPRESSION_RATIO);
    }

    @Override
    public InputStream allocateInputStream(final InputStream in) throws IOException {
	return new SnappyFramedInputStream(in, useChecksum());
    }

    protected boolean useChecksum() {
        return false;
    }
}
