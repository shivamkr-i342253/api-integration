package com.teikametrics.apiintegration.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teikametrics.apiintegration.config.OAuthGitHubAppProperties;
import com.teikametrics.apiintegration.constants.Constants;
import com.teikametrics.apiintegration.model.Commit;
import com.teikametrics.apiintegration.model.Event;
import com.teikametrics.apiintegration.service.CommitsProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommitsProcessorImpl implements CommitsProcessor {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OAuthGitHubAppProperties oAuthGitHubAppProperties;

    private static final Logger LOG = LoggerFactory.getLogger(CommitsProcessorImpl.class);


    @Override
    public  void findMostRecentCommits(List<Event> events) {

        // counter for finding the given no of commits

        int count = 0;

        // the list stores the most recent commits PUSHED by the user

        List<Commit> mostRecentCommits = new ArrayList<>();

        // the map stores the timestamps for each event

        Map<String, Integer> timestampMap = new HashMap<>();

        for (int i = 0; i < events.size(); ++i) {

            Event event = events.get(i);

            // only the events with event_type=PUSHED are process

            if (Constants.EVENT_TYPE_VALUE.equals(event.getType())) {

                Map<String, Object> payloadMap = event.getPayload();

                List<Commit> commits = objectMapper.convertValue(payloadMap.get(Constants.COMMITS), new TypeReference<List<Commit>>() {});

                // get the commits performed by the user itself as author and ignore the rest

                List<Commit> commitsByUserEmail = commits
                        .stream()
                        .filter(commit -> oAuthGitHubAppProperties.getUserEmail().equals(commit.getAuthor().get(Constants.EMAIL)))
                        .collect(Collectors.toList());

                int noOfCommitsByUserEmail = commitsByUserEmail.size();

                timestampMap.put(event.getCreated_at(), noOfCommitsByUserEmail);

                if ((count + noOfCommitsByUserEmail) > Constants.NUMBER_MOST_RECENT_COMMITS) {

                    int j = 1;

                    while (j <= (Constants.NUMBER_MOST_RECENT_COMMITS - count)) {

                        mostRecentCommits.add(commitsByUserEmail.get(j-1));

                        ++j;
                    }

                    break;
                }
                else {
                    count += noOfCommitsByUserEmail;

                    mostRecentCommits.addAll(commitsByUserEmail);

                }

            }

        }

        processCommitMessages(mostRecentCommits);

        processCommitTimestamps(timestampMap);
    }

    // to find the most recent words used in the commit

    private void processCommitMessages(List<Commit> mostRecentCommits) {

        Map<String, Integer> wordsMap = new HashMap<>();

        for (int i = 0; i < mostRecentCommits.size(); ++i) {

            Commit commit = mostRecentCommits.get(i);

            if (commit != null) {

                String message = commit.getMessage();

                if (message != null) {

                    String[] words = message.split(" ");

                    for (String word : words) {

                        if (wordsMap.containsKey(word)) {

                            wordsMap.put(word, wordsMap.get(word) + 1);
                        }
                        else {

                            wordsMap.put(word, 1);
                        }
                    }
                }
            }
        }


        System.out.println();
        System.out.println(Constants.NUMBER_MOST_FREQUENTLY_USED_WORDS+" "+Constants.MOST_FREQUENTLY_USED_WORDS+": "+Arrays.asList(getMostFrequentlyUsedWords(wordsMap)));

    }

    public static String[] getMostFrequentlyUsedWords(Map<String, Integer> wordsMap) {

        Map<String, Integer> reverseSortedWordsMap = wordsMap.entrySet()
                .stream()
                .sorted((Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) -> {
                    if (e1.getValue() == e2.getValue()) {

                        return e1.getKey().compareTo(e2.getKey());
                    }

                        return e2.getValue() - e1.getValue();
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        int i = 1;

        Iterator<Map.Entry<String, Integer> > iterator = reverseSortedWordsMap.entrySet().iterator();

        String[] mostUsedWords = new String[Constants.NUMBER_MOST_FREQUENTLY_USED_WORDS];

        while (i <= Constants.NUMBER_MOST_FREQUENTLY_USED_WORDS && iterator.hasNext()) {

            mostUsedWords[i-1] = iterator.next().getKey();

            ++i;

        }

        return mostUsedWords;
    }



    public static String getMostFrequentlyCommittedHour(Map<String, Integer> hourMap) {

        Map<String, Integer> reverseSortedHourMap = hourMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        return reverseSortedHourMap.entrySet().iterator().next().getKey();
    }


    // to find the hour of day with most commits

    private void processCommitTimestamps(Map<String, Integer> timestampMap) {

        Map<String, Integer> hourMap = new HashMap<>();

        Set<Map.Entry<String, Integer>> timestampEntrySet = timestampMap.entrySet();

        for (Map.Entry<String, Integer> timestampEntry : timestampEntrySet) {

            String timestamp = timestampEntry.getKey();

            if (timestamp != null) {

                String[] dateTime = timestamp.split("T");

                String[] time = dateTime[1].split(":");

                if (hourMap.containsKey(time[0])) {

                    hourMap.put(time[0], hourMap.get(time[0]) + timestampEntry.getValue());
                }
                else {

                    hourMap.put(time[0], timestampEntry.getValue());
                }

            }
        }

        System.out.println();
        System.out.println(Constants.MOST_COMMITTED_HOUR+": "+getMostFrequentlyCommittedHour(hourMap));

    }
}
