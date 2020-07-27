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
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;

public class BufferedPassThroughDriver extends Driver {
    @Override
    public String getDescription() {
	return "BufferedOutputStream and BufferedInputStream";
    }

    @Override
    public OutputStream allocateOutputStream(final OutputStream out) throws IOException {
	return new BufferedOutputStream(out, 64 * 1024);
    }

    @Override
    public InputStream allocateInputStream(final InputStream in) throws IOException {
	return new BufferedInputStream(in, 64 * 1024);
    }
}
