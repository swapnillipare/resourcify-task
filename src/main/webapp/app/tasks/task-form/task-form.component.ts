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
  fileData: string | null = null;
  fileName: string | null = null;
  fileType: string | null = null;

  constructor(@Inject('TaskService') private taskService: TaskService,private datePipe: DatePipe) { }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];
     
      this.fileName = file.name; // Capture file name
      this.fileType = file.type; // Capture file type
      
      const reader = new FileReader();
      reader.onload = () => {
        const result = reader.result as string;
        this.fileData = result.split(',')[1]; // Remove the `data:*/*;base64,` prefix
      };

      reader.readAsDataURL(file); // Read file as DataURL (base64)
    }
  }

  onSubmit(): void {
    const formValue = this.taskForm.value;
    const formattedDueDate = this.datePipe.transform(formValue.dueDate, "yyyy-MM-dd'T'HH:mm:ss.SSS");

    const taskData = {
      name: formValue.name,
      dueDate: formattedDueDate, // Send the formatted date
      fileData: this.fileData, // Send base64 encoded file with the form data
      fileName: this.fileName,     // Send file name
      fileType: this.fileType      // Send file type
    };
   
    this.taskService.create(taskData.name,taskData.dueDate, taskData.fileData, taskData.fileName, taskData.fileType).subscribe(task => {
      this.created.emit(task);
      this.taskForm.reset();
    });
  }
}
