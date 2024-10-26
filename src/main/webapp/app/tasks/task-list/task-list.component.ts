import { ChangeDetectionStrategy, Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Task } from '../task';
import { TaskService } from '../task.service';

/**
 * A list of tiny tasks.
 */
@Component({
  selector: 'tiny-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskListComponent {

  @Input() tasks: Task[];
  imagePreviews: { [taskId: string]: SafeUrl } = {};

  @Output() deleted: EventEmitter<Task> = new EventEmitter();

  constructor(@Inject('TaskService') private taskService: TaskService, private sanitizer: DomSanitizer) { }

  onDownload(taskId: string): void {
    this.taskService.downloadFile(taskId);
  }

  loadImagePreview(taskId: string): void {
    this.taskService.generatePreviewUrl(taskId).subscribe(previewUrl => {
      // Store the sanitized URL for safe display in the template
      this.imagePreviews[taskId] = this.sanitizer.bypassSecurityTrustUrl(previewUrl);
    });
  }

  delete(task: Task): void {
    this.taskService.delete(task.id).subscribe(() => {
      this.deleted.emit(task);
    });
  }
}
