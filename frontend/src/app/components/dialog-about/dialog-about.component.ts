import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { ProjectManagerOutputDTO } from 'src/app/dto/projectManagerOutput.dto';
import { GenericApi } from 'src/app/PMApi/generic.api';

@Component({
  selector: 'app-dialog-about',
  templateUrl: './dialog-about.component.html'
})
export class DialogAboutComponent implements OnInit {
  public apiInfos: ProjectManagerOutputDTO | undefined;

  constructor(private readonly matDialogRef: MatDialogRef<DialogAboutComponent>, private readonly genericApi: GenericApi) {}

  ngOnInit(): void {
    this.genericApi.getApiInfos().subscribe(apiInfos => {
      this.apiInfos = apiInfos;
    });
  }
}