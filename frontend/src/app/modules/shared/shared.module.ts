import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from './table/table.component';
import { DialogInfosComponent } from './dialog-infos/dialog-infos.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { DialogConfirmComponent } from './dialog-confirm/dialog-confirm.component';



@NgModule({
  declarations: [
    TableComponent,
    DialogInfosComponent,
    DialogConfirmComponent,
  ],
  imports: [
    MatDialogModule,
    MatButtonModule,
    CommonModule
  ],
  exports: [
    DialogInfosComponent
  ]

})
export class PMSharedModule { }
