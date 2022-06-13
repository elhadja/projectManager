import { Component, Input } from '@angular/core';
import { CustomRevisionEntityDTO } from 'src/app/dto/custom-revision-entity.dto';

@Component({
  selector: 'pm-activity',
  templateUrl: './activity.component.html',
  styleUrls: []
})
export class ActivityComponent {
  @Input()
  public activities: CustomRevisionEntityDTO[];
  public showActivities: boolean;

  constructor() {
    this.showActivities = false;
    this.activities = [];
  }
}
