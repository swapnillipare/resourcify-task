import { ChangeDetectionStrategy, Component, EventEmitter, Inject, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Task } from '../task';
import { TaskService } from '../task.service';

/**
 * A form to create tiny tasks.
 */
@Component({
  selector: 'tiny-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.scss'],
  providers: [DatePipe],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskFormComponent {

  @Output() created: EventEmitter<Task> = new EventEmitter();

  taskForm: FormGroup = new FormGroup({
    name: new FormControl('', Validators.required),
    dueDate: new FormControl(null)
  });

  constructor(@Inject('TaskService') private taskService: TaskService,private datePipe: DatePipe) { }

  onSubmit(): void {
    const formValue = this.taskForm.value;
    const formattedDueDate = this.datePipe.transform(formValue.dueDate, "yyyy-MM-dd'T'HH:mm:ss.SSS");

    const taskData = {
      name: formValue.name,
      dueDate: formattedDueDate // Send the formatted date
    };
    this.taskService.create(taskData.name,taskData.dueDate).subscribe(task => {
      this.created.emit(task);
      this.taskForm.reset();
    });
  }
}
