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

import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import org.apache.commons.cli.*;
import org.yaml.snakeyaml.Yaml;

public class App {
    private Config config;
    private HashMap<String, byte[]> testDataBytesMap;
    private SanityChecker sanityChecker;
    private int warmupSeconds;
    private int runSeconds;

    private Config loadConfig(final String configYAML) {
	final Yaml yaml = new Yaml();
	Config config = null;
	try (FileInputStream fis = new FileInputStream(configYAML)) {
	    config = yaml.loadAs(fis, Config.class);
	} catch (IOException ex) {
	    System.err.println(ex);
	    System.exit(1);
	}
	return config;
    }

    private void checkConfig(final Config config) {
	HashSet<String> duplicateChecker = new HashSet<String>();
	for (final String testDataName : config.getTestdata()) {
	    if (!duplicateChecker.add(testDataName)) {
		System.err.println(testDataName + " appears twice in the config");
		System.exit(1);
	    }
	}
    }

    private byte[] readTestData(String testDataName) {
	byte[] buffer = new byte[4096];
	try (FileInputStream fis = new FileInputStream(testDataName); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	    int bytesRead;
	    while ((bytesRead = fis.read(buffer)) != -1) {
		baos.write(buffer, 0, bytesRead);
	    }

	    return baos.toByteArray();
	} catch (IOException ex) {
	    System.err.println("Cannot read " + testDataName);
	    System.err.println(ex);
	    System.exit(1);
	}

	return null;
    }

    private HashMap<String, byte[]> readTestData(Config config) {
	HashMap<String, byte[]> testDataBytesMap = new HashMap<String, byte[]>();

	for (final String testDataName : config.getTestdata()) {
	    testDataBytesMap.put(testDataName, readTestData(testDataName));
	}

	return testDataBytesMap;
    }

    private App(final String configYAML) {
	config = loadConfig(configYAML);
	checkConfig(config);
	testDataBytesMap = readTestData(config);
	sanityChecker = new SanityChecker(1000);

	warmupSeconds = 10;
	runSeconds = 30;
    }

    private Driver instantiateDriver(final String driverClassName) {
	try{
	    return Driver.class.cast(Class.forName(driverClassName).newInstance());
	} catch(InstantiationException
		| IllegalAccessException
		| ClassNotFoundException
		| LinkageError ex){
	    throw new IllegalStateException(ex);
	}
    }

    private Test instantiateTest(final String testClassName) {
	try{
	    return Test.class.cast(Class.forName(testClassName).newInstance());
	} catch(InstantiationException
		| IllegalAccessException
		| ClassNotFoundException
		| LinkageError ex){
	    throw new IllegalStateException(ex);
	}
    }

    private void runTestData(final String driverClassName, final Driver driver, final String testClassName, final Test test, final String testDataName) {
	System.out.print(driverClassName.substring(driverClassName.lastIndexOf('.') + 1) + '\t');
	System.out.print(testClassName.substring(testClassName.lastIndexOf('.') + 1) + '\t');
	System.out.print(testDataName + '\t');

	byte[] testDataBytes = testDataBytesMap.get(testDataName);

	try {
	    test.initializeTestData(driver, testDataBytes, config);
	} catch (IOException ex) {
	    System.err.println("Test initializeTestData error for " + driverClassName + "/" + testClassName + "/" + testDataName);
	    System.err.println(ex);
	    return;
	}

	try {
	    System.gc();
	    sanityChecker.reset();

	    final long warmupStartTime = System.currentTimeMillis();
	    long currentTime = warmupStartTime;
	    do {
		try {
		    test.run(driver, testDataBytes, config, sanityChecker.check());
		} catch (IOException ex) {
		    System.err.println("Test warmup error for " + driverClassName + "/" + testClassName + "/" + testDataName);
		    System.err.println(ex);
		    return;
		} catch (Test.SanityCheckException ex) {
		    System.err.println("Sanity check error for " + driverClassName + "/" + testClassName + "/" + testDataName);
		    return;
		}
		currentTime = System.currentTimeMillis();
	    } while ((currentTime - warmupStartTime) < warmupSeconds * 1000);

	    System.gc();

	    final long runStartTime = System.currentTimeMillis();
	    long numOperations = 0;
	    do {
		try {
		    test.run(driver, testDataBytes, config, sanityChecker.check());
		} catch (IOException ex) {
		    System.err.println("Test run error for " + driverClassName + "/" + testClassName + "/" + testDataName);
		    System.err.println(ex);
		    return;
		} catch (Test.SanityCheckException ex) {
		    System.err.println("Sanity check error for " + driverClassName + "/" + testClassName + "/" + testDataName);
		    return;
		}
		numOperations++;
		currentTime = System.currentTimeMillis();
	    } while ((currentTime - runStartTime) < runSeconds * 1000);

	    final double ops = 1000.0 * numOperations / (currentTime - runStartTime);
	    System.out.printf("%.1f\tops\n", ops);
	} finally {
	    try {
		test.tearDownTestData(driver, testDataBytes, config);
	    } catch (IOException ex) {
		System.err.println("Test tearDownTestData error for " + driverClassName + "/" + testClassName + "/" + testDataName);
		System.err.println(ex);
	    }
	}
    }

    private void run() {
	for (final String driverClassName : config.getDrivers()) {
	    final Driver driver = instantiateDriver(driverClassName);

	    for (final String testClassName : config.getTests()) {
		final Test test = instantiateTest(testClassName);

		try {
		    test.initialize(driver, config);
		} catch (IOException ex) {
		    System.err.println("Test initialization error for" + driverClassName + "/" + testClassName);
		    System.err.println(ex);
		    continue;
		}

		List<String> testDataNames;
		if (test.isAllocationTest()) {
		    testDataNames = new ArrayList<String>();
		    testDataNames.add("InstanceAllocation");
		} else {
		    testDataNames = config.getTestdata();
		}
		for (final String testDataName : testDataNames) {
		    runTestData(driverClassName, driver, testClassName, test, testDataName);
		}
		try {
		    test.tearDown(driver, config);
		} catch (IOException ex) {
		    System.err.println("Test tearDown error for " + driverClassName + "/" + testClassName);
		    System.err.println(ex);
		}
	    }
	    try {
		driver.tearDown(config);
	    } catch (IOException ex) {
		System.err.println("Driver tearDown error for " + driverClassName);
		System.err.println(ex);
	    }
	}
    }

    private static void printHelp(final HelpFormatter formatter, final Options options) {
	formatter.printHelp("java -jar java-compressor-stream-benchmark.jar [options] <config>", options);
    }

    public static void main(final String[] args){
	final Options options = new Options();
	options.addOption(new Option("h", false, "print help"));
	options.addOption(new Option("w", true, "specify warmup time in seconds (default 10 seconds)"));
	options.addOption(new Option("r", true, "specify run time in seconds (default 30 seconds)"));

	final CommandLineParser parser = new DefaultParser();
	final HelpFormatter formatter = new HelpFormatter();
	CommandLine cmd = null;

	try {
	    cmd = parser.parse(options, args);
	} catch (ParseException ex) {
	    System.err.println(ex.getMessage());
	    printHelp(formatter, options);
	    System.exit(1);
	}
	if (cmd.hasOption("h")) {
	    printHelp(formatter, options);
	    System.exit(0);
	}

	final String[] leftOverArgs = cmd.getArgs();
	if (leftOverArgs.length != 1) {
	    printHelp(formatter, options);
	    System.exit(0);
	}

	final App app = new App(leftOverArgs[0]);

	String wOptionValue = cmd.getOptionValue("w");
	if (wOptionValue != null) {
	    try {
		app.warmupSeconds = Integer.parseInt(wOptionValue);
	    } catch (NumberFormatException ex) {
		System.err.println(wOptionValue + " is not an integer number");
		printHelp(formatter, options);
		System.exit(1);
	    }
	}
	String rOptionValue = cmd.getOptionValue("r");
	if (rOptionValue != null) {
	    try {
		app.runSeconds = Integer.parseInt(rOptionValue);
	    } catch (NumberFormatException ex) {
		System.err.println(rOptionValue + " is not an integer number");
		printHelp(formatter, options);
		System.exit(1);
	    }
	}

	app.run();
    }

    private class SanityChecker {
	private long counter =  0;
	private final int interval;

	SanityChecker(int interval) {
	    this.interval = interval;
	}

	boolean check() {
	    if (counter++ % interval == 0) {
		return true;
	    } else {
		return false;
	    }
	}

	void reset() {
	    counter = 0;
	}
    }
}
