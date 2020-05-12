Feature: Todo MainView Frame
  
  Scenario: Initial state
    Given the db contains a todo with ID "2" and text "Buy milk"
    And the db contains a todo with ID "5" and text "Call Greg"
    When MainView is shown
    Then the todo list contains a todo with text "Buy milk"
    And the todo list contains a todo with text "Call Greg"
