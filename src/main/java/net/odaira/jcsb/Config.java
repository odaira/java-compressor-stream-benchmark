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

import java.util.List;

public class Config {
    private String title;
    private List<String> drivers;
    private List<String> tests;
    private List<String> testdata;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getDrivers() { return drivers; }
    public void setDrivers(List<String> drivers) { this.drivers = drivers; }

    public List<String> getTests() { return tests; }
    public void setTests(List<String> tests) { this.tests = tests; }

    public List<String> getTestdata() { return testdata; }
    public void setTestdata(List<String> testdata) { this.testdata= testdata; }

    @Override
    public String toString() {
	return "Config { drivers=" + drivers + ", tests=" + tests + ", testdata=" + testdata + " }";
    }
}
