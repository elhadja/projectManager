import { AfterViewInit, Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { PMConstants } from './common/PMConstants';
import { API } from './services/Api';
import { RoutingService } from './services/routing.service';
import { sessionManagerService } from './services/sessionManager.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit {
  title = 'frontend';
  constructor(private translate: TranslateService,
              private readonly sessionManagerService: sessionManagerService,
              private readonly api: API) {
    translate.setDefaultLang(PMConstants.DEFAULT_LANG);
   
    if (sessionManagerService.isActive()) {
      sessionManagerService.subscribeIdle();
    }
  }

  ngAfterViewInit(): void {
    this.translate.use(this.sessionManagerService.getLanguage());
    this.sessionManagerService.setLanguage(this.translate.currentLang);
    this.api.setLang(this.translate.currentLang);
  }
}
