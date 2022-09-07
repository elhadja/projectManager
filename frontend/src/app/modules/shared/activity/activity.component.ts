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
  @Input()
  public manageVisibility: boolean;
  @Input()
  public showActivities: boolean;

  constructor() {
    this.showActivities = false;
    this.manageVisibility = true;

    this.activities = [];
  }

  public get componentName(): string {
    return 'ActivityComponent';
  }
}
