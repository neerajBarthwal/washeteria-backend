package com.iiith.washeteria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiith.washeteria.dao.TopicDAO;
import com.iiith.washeteria.dataentities.Topic;

@Service
public class TopicService {

	@Autowired
	private TopicDAO topicDAO;
	
	public void addTopic(Topic topic) {
		topicDAO.add(topic);
		
	}

	public Topic getTopic(String id) {
		// TODO Auto-generated method stub
		
		return topicDAO.getById(id);
	}

}
