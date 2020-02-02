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
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class WriteByteArray512Test extends WriteTest {
    @Override
    public String getDescription() {
	return "Write by write(byte[], int, int) 512 bytes each time";
    }

    @Override
    public void run(final Driver driver, final byte[] testData, final Config config, final boolean check) throws IOException, SanityCheckException {
	final ByteArrayOutputStream baos = new ByteArrayOutputStream(32 * 1024);
	try (final OutputStream os = driver.allocateOutputStream(baos)) {
	    final int chunkSize = 512;
	    for (int i = 0; i < testData.length; i += chunkSize) {
		final int remaining = testData.length - i;
		final int len = remaining < chunkSize ? remaining : chunkSize;
		os.write(testData, i, len);
	    }
	}
	if (check) {
	    check(driver, testData, baos);
	}
    }
}
