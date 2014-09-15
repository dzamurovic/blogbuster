package com.codingserbia;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.codingserbia.data.CustomerTestDataProvider;
import com.codingserbia.writable.CustomerSessionWritable;

public class CustomerRecordsMapperTest {

    public static String CUSTOMER_CATEGORIES_FILE_PATH = "file://target/test-classes/input/customer_categories.db";

    private MapDriver<LongWritable, Text, LongWritable, CustomerSessionWritable> mapDriver;

    private Configuration config;

    @Before
    public void setUp() {
        config = new Configuration();
        config.set("customer_categories.file.path", CUSTOMER_CATEGORIES_FILE_PATH);

        CustomerRecordsMapper mapper = new CustomerRecordsMapper();
        mapDriver = MapDriver.newMapDriver(mapper);
    }

    // testing style: tell the input, assert the output
    @Test
    public void testMapperWithManualAssertions() throws Exception {
        Context ctx = mapDriver.getContext();
        Mockito.when(ctx.getConfiguration()).thenReturn(config);

        for (int i = 0; i < CustomerTestDataProvider.CUSTOMER_RECORDS_FOR_MAP_INPUT_ONLY.size(); i++) {
            mapDriver.withInput(new LongWritable(i), CustomerTestDataProvider.CUSTOMER_RECORDS_FOR_MAP_INPUT_ONLY.get(i));
        }

        List<Pair<LongWritable, CustomerSessionWritable>> result = mapDriver.run();

        Assertions.assertThat(result).isNotNull().hasSize(4);
    }

}
