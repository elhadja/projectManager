import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PmButtonComponent } from './pm-button/pm-button.component';
import { MatButtonModule } from '@angular/material/button';
import { PmTextAreaInputTextComponent } from './pm-text-area-input-text/pm-text-area-input-text.component';

const MAT_MODULES = [
  MatButtonModule
]


@NgModule({
  declarations: [
    PmButtonComponent,
    PmTextAreaInputTextComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule
  ],
  exports: [
    PmButtonComponent
  ]
})
export class GuiModule { }
