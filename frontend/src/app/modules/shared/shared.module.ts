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
import { DialogActivityComponent } from './dialog-activity/dialog-activity.component';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { HttpLoaderFactory } from 'src/app/app.module';



@NgModule({
  declarations: [
    TableComponent,
    DialogInfosComponent,
    DialogConfirmComponent,
    TextLengthPipe,
    ActivityComponent,
    DialogActivityComponent,
  ],
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatListModule,
    CommonModule,
    TranslateModule.forChild({
      loader: {provide: TranslateLoader, useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  exports: [
    DialogInfosComponent,
    DialogConfirmComponent,
    TextLengthPipe,
    ActivityComponent
  ]

})
export class PMSharedModule { }
