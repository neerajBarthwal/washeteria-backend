package com.iiith.washeteria.dao;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iiith.washeteria.dataentities.Topic;

@Transactional
@Repository
public class TopicDAO {
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	public void add(Topic topic) {	
		
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		session.save(topic);
		
	}

	public Topic getById(String id) {
		Topic topic = null;
		
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		topic = session.get(Topic.class, id);
		return topic;
	}

}
