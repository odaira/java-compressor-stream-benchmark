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

public abstract class Test {

    public abstract String getDescription();

    public void initialize(final Driver driver, final Config config) throws IOException {
    }

    public void initializeTestData(final Driver driver, final byte[] testData, final Config config) throws IOException {
    }

    public abstract void run(Driver driver, byte[] testData, Config config, boolean check) throws IOException, SanityCheckException;

    public void tearDownTestData(final Driver driver, final byte[] testData, final Config config) throws IOException {
    }

    public void tearDown(final Driver driver, final Config config) throws IOException {
    }

    public boolean isAllocationTest() {
	return false;
    }

    public boolean isSizeTest() {
	return false;
    }

    public int getCompressedSize() {
	return -1;
    }

    public class SanityCheckException extends Exception {
    }
}
