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

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class Skip512Test extends ReadTest {
    @Override
    public String getDescription() {
	return "Skip by skip(long) 512 bytes each time";
    }

    private static final int chunkSize = 512;

    @Override
    public void run(final Driver driver, final byte[] testData, final Config config, final boolean check) throws IOException, SanityCheckException {
	try (final InputStream is = driver.allocateInputStream(new ByteArrayInputStream(compressedTestData))) {
	    long n = testData.length < chunkSize ? testData.length : chunkSize;
	    long skippedBytes;
	    long totalSkippedBytes = 0;
	    while ((skippedBytes = is.skip(n)) != 0) {
		totalSkippedBytes += skippedBytes;
		long remainingBytes = testData.length - totalSkippedBytes;
		n = remainingBytes < chunkSize ? remainingBytes : chunkSize;
	    }
	    if (check) {
		if (totalSkippedBytes != testData.length) {
		    throw new SanityCheckException();
		}
	    }

	    int byteRead;
	    int cursor = 0;
	    while ((byteRead = is.read()) != -1) {
		if (check) {
		    if (testData[cursor] != (byte)byteRead) {
			throw new SanityCheckException();
		    }
		    cursor++;
		}
	    }
	}
    }
}
