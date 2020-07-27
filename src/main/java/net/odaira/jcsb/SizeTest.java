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

public class SizeTest extends Test {
    @Override
    public String getDescription() {
	return "Return the size of the compressed test data";
    }

    private int compressedSize;

    @Override
    public void initializeTestData(final Driver driver, final byte[] testData, final Config config) throws IOException {
	compressedSize = driver.getCompressedBytes(testData).length;
    }

    @Override
    public void run(Driver driver, byte[] testData, Config config, boolean check) {
	throw new IllegalStateException("run() must not be executed for SiztTest");
    }

    @Override
    public boolean isSizeTest() {
	return true;
    }

    @Override
    public int getCompressedSize() {
	return compressedSize;
    }
}
