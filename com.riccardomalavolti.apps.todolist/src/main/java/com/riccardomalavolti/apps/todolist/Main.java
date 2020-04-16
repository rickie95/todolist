package com.riccardomalavolti.apps.todolist;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;


import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.controller.TodoManager;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepositoryMongoDB;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepository;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepositoryMongoDB;
import com.riccardomalavolti.apps.todolist.view.MainView;

public class Main {

	private static final Logger LOGGER = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		
		MongoClient mongoClient = null;
		
		if(args.length > 0 && args[0].equals("debug")) {
				LoggerContext context = (LoggerContext) LogManager.getContext(false);
				Configuration config = context.getConfiguration();
				LoggerConfig rootConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
				rootConfig.setLevel(Level.DEBUG);
			}
		
		LOGGER.info("The application is starting.");
		
		mongoClient = new MongoClient(new ServerAddress("localhost"));
		
		TagRepository tagMongoRepo = new TagRepositoryMongoDB(mongoClient);
		TodoRepository todoMongoRepo = new TodoRepositoryMongoDB(mongoClient, tagMongoRepo);
		
		TodoManager todoManager = new TodoManager(todoMongoRepo, tagMongoRepo);
		
		
		MainView todoView = new MainView();
		todoView.setVisible(true);
		
		TodoController todoController = new TodoController(todoView, todoManager);
		
		todoView.setController(todoController);
		
	}

}
