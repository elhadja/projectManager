import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableComponent } from './table/table.component';
import { DialogInfosComponent } from './dialog-infos/dialog-infos.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { DialogConfirmComponent } from './dialog-confirm/dialog-confirm.component';
import { TextLengthPipe } from './pipes/text-length.pipe';
import { ActivityComponent } from './activity/activity.component';
import { MatListModule } from '@angular/material/list';



@NgModule({
  declarations: [
    TableComponent,
    DialogInfosComponent,
    DialogConfirmComponent,
    TextLengthPipe,
    ActivityComponent,
  ],
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatListModule,
    CommonModule
  ],
  exports: [
    DialogInfosComponent,
    DialogConfirmComponent,
    TextLengthPipe,
    ActivityComponent
  ]

})
export class PMSharedModule { }
