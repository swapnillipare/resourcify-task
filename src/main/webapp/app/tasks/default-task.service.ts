import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BASE_URL } from '../app.tokens';
import { Task } from './task';
import { TaskService } from './task.service';

@Injectable()
export class DefaultTaskService implements TaskService {

  constructor(private http: HttpClient, @Inject(BASE_URL) private baseUrl: string) {
  }

  create(name: string, dueDate:string, fileData:string, fileName:string, fileType:string): Observable<Task> {
    return this.http.post<Task>(this.baseUrl + '/tasks', {name,dueDate,fileData,fileName,fileType} as Task);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(this.baseUrl + '/tasks/' + id);
  }

  getAll(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl + '/tasks');
  }

  // Generate image preview URL if the file is an image
  generatePreviewUrl(taskId: string): Observable<string> {
    return this.http.get(`${this.baseUrl}/tasks/files/stream/${taskId}`, { responseType: 'blob' })
      .pipe(
        map((blob: Blob) => {
          // Create a URL for the image blob
          return URL.createObjectURL(blob);
        })
      );
  }
  
  downloadFile(taskId: string): void {
    // Directly open the download URL in a new tab
    const downloadUrl = `${this.baseUrl}/tasks/files/stream/${taskId}`;
    window.open(downloadUrl, '_blank');
  }
}
