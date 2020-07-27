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
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class WriteByteArrayTest extends WriteTest {
    @Override
    public String getDescription() {
	return "Write by single write(byte[])";
    }

    @Override
    public void run(final Driver driver, final byte[] testData, final Config config, final boolean check) throws IOException, SanityCheckException {
	final ByteArrayOutputStream baos = new ByteArrayOutputStream(32 * 1024);
	try (final OutputStream os = driver.allocateOutputStream(baos)) {
	    os.write(testData);
	}
	if (check) {
	    check(driver, testData, baos);
	}
    }
}
