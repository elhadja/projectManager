import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { DialogCreateTaskComponent } from '../dialog/dialog-create-task/dialog-create-task.component';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {
  public projectId: number;

  constructor(private matDialog: MatDialog,
              private route: ActivatedRoute) {
    this.projectId = 0;
    const routeParameter = this.route.snapshot.paramMap.get('project-id');
    if (routeParameter != null) {
      this.projectId = +(routeParameter);
    } else {
      const id = localStorage.getItem('projectId');
      this.projectId = id != null ? +id : this.projectId;
    }

  }

  ngOnInit(): void {
  }

  public onCreateTask(): void {
    this.matDialog.open(DialogCreateTaskComponent, { data: {projectId: this.projectId } });
  }
}
