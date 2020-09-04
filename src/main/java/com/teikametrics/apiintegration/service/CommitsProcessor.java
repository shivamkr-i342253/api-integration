package com.teikametrics.apiintegration.service;

import com.teikametrics.apiintegration.model.Event;

import java.util.List;

public interface CommitsProcessor {

    void findMostRecentCommits(List<Event> events);

}
