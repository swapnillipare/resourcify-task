# TinyTasks Nr. 1

## Task: A user can set a due date

### Summary

As a user, I want to **set a due date** for my tasks so that I can start working on my urgent tasks first.

### Acceptance criteria

For this issue to be seen as **Done**, it must satisfy the following:

- the user can set a due date for a task
- the task list is sorted by due date
  - tasks with a due date go to the top of the list
  - tasks without a due date go to the bottom of the list

### Tech Approach

Please keep these things in mind:

- Backend: TaskService and TaskController already exists, can use this as a starting point
- Frontend: TaskFormComponent is used to enter task data, TaskListComponent is used to show all tasks
- Documentation for material datepicker: https://v11.material.angular.io/components/datepicker/overview

### Out of Scope

This ticket should not solve:

- to be discussed
