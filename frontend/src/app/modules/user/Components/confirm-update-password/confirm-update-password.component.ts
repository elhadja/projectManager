import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserApiService } from 'src/app/PMApi/user-api.service';
import { RoutingService } from 'src/app/services/routing.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';

@Component({
  selector: 'app-confirm-update-password',
  templateUrl: './confirm-update-password.component.html',
  styleUrls: ['./confirm-update-password.component.css']
})
export class ConfirmUpdatePasswordComponent implements OnInit {
  public message: string;
  public success: boolean;

  constructor(private readonly userApiService: UserApiService,
    private readonly sessionManagerService: sessionManagerService,
    private readonly route: ActivatedRoute,
    private readonly routingService: RoutingService) {
    this.message = '';
    this.success = false;
  }

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    this.userApiService.confirmUpdateEmail(this.sessionManagerService.getUserId(), token ?? '')
      .subscribe(
        () => {
          this.message = 'Your mail has been changed succesfully';
          this.success = true;
        },
        () => {
          this.message = 'An arror occured when trying to change your mail';
          this.success = false;
        });
  }

  public gotoAccountComponent(): void {
    this.routingService.gotoAccountComponent();
  }
}
