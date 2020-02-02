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

import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

public class InputStreamAllocationTest extends Test {
    @Override
    public String getDescription() {
	return "Allocate an input stream instance";
    }

    private byte[] compressedBytes;

    @Override
    public void initialize(final Driver driver, final Config config) throws IOException {
	compressedBytes = driver.getCompressedBytes(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
	System.gc();
    }

    @Override
    public void run(final Driver driver, final byte[] testData, final Config config, final boolean check) throws IOException {
	final InputStream is = driver.allocateInputStream(new ByteArrayInputStream(compressedBytes));
	is.close();
    }

    @Override
    public void tearDown(Driver driver, Config config) {
	System.gc();
    }

    @Override
    public boolean isAllocationTest() {
	return true;
    }
}
