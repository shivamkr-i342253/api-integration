package com.teikametrics.apiintegration;

import com.teikametrics.apiintegration.service.impl.CommitsProcessorImpl;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class CommitsProcessorImplTest {

    @Test
    void testFrequentlyUsedWords() {
        Map<String, Integer> map = new HashMap<>();
        map.put("hello", 1);
        map.put("fix", 7);
        map.put("changes", 3);
        map.put("replaced", 2);
        map.put("logic", 5);

        String[] sorted = new String[] {"fix", "logic", "changes", "replaced", "hello"};

        assertEquals(sorted, CommitsProcessorImpl.getMostFrequentlyUsedWords(map));
    }

    @Test
    void testMostCommittedHour() {
        Map<String, Integer> map = new HashMap<>();
        map.put("10", 1);
        map.put("22", 4);
        map.put("03", 3);
        map.put("19", 2);
        map.put("11", 5);

        assertEquals("11", CommitsProcessorImpl.getMostFrequentlyCommittedHour(map));
    }
}
