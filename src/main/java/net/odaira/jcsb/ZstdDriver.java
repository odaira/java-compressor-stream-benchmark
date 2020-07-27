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

import com.github.luben.zstd.ZstdOutputStream;
import com.github.luben.zstd.ZstdInputStream;

public class ZstdDriver extends Driver {
    @Override
    public String getDescription() {
	return "ZstdOutputStream and ZstdInputStream";
    }

    @Override
    public OutputStream allocateOutputStream(final OutputStream out) throws IOException {
	ZstdOutputStream zos = new ZstdOutputStream(out);
	zos.setChecksum(useChecksum);
	return zos;
    }

    @Override
    public InputStream allocateInputStream(final InputStream in) throws IOException {
	return new ZstdInputStream(in);
    }
}
