import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from './table/table.component';
import { DialogConfirmComponent } from './dialog-confirm/dialog-confirm.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';



@NgModule({
  declarations: [
    TableComponent,
    DialogConfirmComponent,
  ],
  imports: [
    MatDialogModule,
    MatButtonModule,
    CommonModule
  ],
  exports: [
    DialogConfirmComponent
  ]

})
export class PMSharedModule { }
