import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { MenuItem } from 'primeng/api';
import { PMConstants } from 'src/app/common/PMConstants';
import { RoutingService } from 'src/app/services/routing.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';
import { DialogAboutComponent } from '../dialog-about/dialog-about.component';

@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrls: ['./menu-bar.component.css']
})
export class MenuBarComponent implements OnInit {
  public items: MenuItem[];
  public userItems: MenuItem[];
  public languageItems: MenuItem[];
  public isVisible: boolean;
  public currentLanguage: string;

  constructor(private sessionService: sessionManagerService,
             private routingService: RoutingService,
             private readonly matDialog: MatDialog,
             private readonly tranlateService: TranslateService) { 
    this.items = [];
    this.userItems = [];
    this.languageItems = [];
    this.isVisible = sessionService.getUserId() > 0;
    this.currentLanguage = '';

  }

  ngOnInit(): void {
    this.sessionService.userLoggedEmitter.subscribe((isUserLogged) => {
      this.isVisible = isUserLogged;
      this.initializeMenu();
    });

    this.sessionService.projectSelectedSubject.subscribe(() => {
      this.initializeMenu();
    });

    this.tranlateService.get('init').subscribe(() => {
      this.initializeMenu();
    });

    this.sessionService.getCurrentLanguage().subscribe(language => {
      this.currentLanguage = language;
      this.initializeMenu(); // TODO All menu should not be build after each language change
    });

    this.initializeMenu();

  }

  private initializeMenu(): void {
    this.items = [
      {
        label: this.tranlateService.instant(this.componentName + '.projects'),
        command: () => { this.routingService.gotoProjectComponent(); },
      },
      {
        label: this.tranlateService.instant(this.componentName + '.backlog'),
        command: () => { this.routingService.gotoBacklogComponent( +this.sessionService.getProjectId()); },
        visible: this.isProjectSelected()
      },
      {
        label: this.tranlateService.instant(this.componentName + '.tasks'),
        command: () => { this.routingService.gotoTaskComponent( +this.sessionService.getProjectId()); },
        visible: this.isProjectSelected()
      },
    ];
    this.userItems = [
      {
        label: this.tranlateService.instant(this.componentName + '.account'),
        command: () => { this.routingService.gotoAccountComponent(); }
      },
      {
        label: this.tranlateService.instant(this.componentName + '.exit'),
        command: () => { 
          this.sessionService.closeSession();
          this.routingService.gotoLoginComponent();
        }
      },
      {
        label: this.tranlateService.instant(this.componentName + '.about'),
        command: () => { this.matDialog.open(DialogAboutComponent); }
      }
    ];

    this.languageItems = [];
    PMConstants.AVAILABLE_LANG.forEach(language => {
      this.languageItems.push({
        label: this.tranlateService.instant(this.componentName + '.' + language.label),
        command: () => {
          this.sessionService.setLanguage(language.value);
        }
      });
    });
    this.languageItems = [...this.languageItems];
  }

  private isProjectSelected(): boolean {
    return this.sessionService.getProjectId() > 0;
  }

  get componentName(): string {
    return 'MenuBarComponent';
  }
}
