import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'pm-button',
  templateUrl: './pm-button.component.html',
  styleUrls: ['./pm-button.component.css']
})
export class PmButtonComponent implements OnInit {

  @Input()
  public label: string;

  @Output()
  public clickEventEmitter: EventEmitter<void>;

  constructor() {
    this.clickEventEmitter = new EventEmitter();
    this.label = "pm-button";
   }

  ngOnInit(): void {
  }

  public emitClickEvent(): void {
    this.clickEventEmitter.emit();
  }

}
