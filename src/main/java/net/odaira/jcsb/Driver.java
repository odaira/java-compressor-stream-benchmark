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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

public abstract class Driver {
    public abstract String getDescription();

    public abstract InputStream allocateInputStream(InputStream in) throws IOException;

    public abstract OutputStream allocateOutputStream(OutputStream out) throws IOException;

    public final byte[] getCompressedBytes(final byte[] data) throws IOException {
	final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	try (final OutputStream os = allocateOutputStream(baos)) {
	    os.write(data);
	}
	return baos.toByteArray();
    }

    public void tearDown(final Config config) throws IOException {
    }
}
