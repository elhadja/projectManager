import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { RoutingService } from './services/routing.service';
import { sessionManagerService } from './services/sessionManager.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';
  constructor(private translate: TranslateService,
              private readonly sessionManagerService: sessionManagerService,
              private readonly routingService: RoutingService) {
    translate.setDefaultLang('fr');

    if (sessionManagerService.isActive()) {
      sessionManagerService.subscribeIdle();
    }
  }
}
