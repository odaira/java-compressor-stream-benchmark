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

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class ReadByteArray4096Test extends ReadTest {
    private static final int chunkSize = 4096;
    private final byte[] buffer = new byte[chunkSize];

    @Override
    public void run(final Driver driver, final byte[] testData, final Config config, final boolean check) throws IOException, SanityCheckException {
	try (final InputStream is = driver.allocateInputStream(new ByteArrayInputStream(compressedTestData))) {
	    int off = 0;
	    int len = chunkSize;
	    int bytesRead;
	    int cursor = 0;
	    while ((bytesRead = is.read(buffer, off, len)) != -1) {
		off += bytesRead;
		len -= bytesRead;
		if (len == 0) {
		    off = 0;
		    len = chunkSize;
		    if (check) {
			for (int i = 0; i < chunkSize; i++, cursor++) {
			    if (testData[cursor] != buffer[i]) {
				throw new SanityCheckException();
			    }
			}
		    }
		}
	    }
	    if (check) {
		for (int i = 0; i < off; i++, cursor++) {
		    if (testData[cursor] != buffer[i]) {
			throw new SanityCheckException();
		    }
		}
	    }
	}
    }
}
