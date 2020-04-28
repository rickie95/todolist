package com.riccardomalavolti.apps.todolist;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

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

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(mixinStandardHelpOptions = true)
public class TodoList implements Callable<Void>{

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = TodoRepositoryMongoDB.DB_NAME;
	
	@Option(names = { "--collection-name" }, description = "Name of the mongo collection used.")
	private String collectionName = TodoRepositoryMongoDB.COLLECTION_NAME;
	
	@Option(names = {"--verbosity", "-v" }, description = "Specifies verbosity level: [ info | debug ]")
	private String verbosityLevel = "info";
	
	
	private static final Logger LOGGER = LogManager.getLogger(TodoList.class);

	public static void main(String[] args) {
		new CommandLine(new TodoList()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		
		EventQueue.invokeLater(() -> {
			
			MongoClient mongoClient = null;
			
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			Configuration config = context.getConfiguration();
			LoggerConfig rootConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
			rootConfig.setLevel(Level.INFO);
			
			if(verbosityLevel.equals("debug"))
					rootConfig.setLevel(Level.DEBUG);
			
		
			LOGGER.info("The application is starting.");
			
			mongoClient = new MongoClient(new ServerAddress("localhost"));
			
			TagRepository tagMongoRepo = new TagRepositoryMongoDB(mongoClient);
			TodoRepository todoMongoRepo = new TodoRepositoryMongoDB(mongoClient, tagMongoRepo);
			
			TodoManager todoManager = new TodoManager(todoMongoRepo, tagMongoRepo);
			
			
			MainView todoView = new MainView();
			todoView.setVisible(true);
			
			TodoController todoController = new TodoController(todoView, todoManager);
			
			todoView.setController(todoController);
			
			LOGGER.info("Todolist is ready");
		});
		
		return null;
	}

}