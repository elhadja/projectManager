import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { PMConstants } from 'src/app/common/PMConstants';
import { RoutingService } from 'src/app/services/routing.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';

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

  constructor(private router: Router,
             private sessionService: sessionManagerService,
             private routingService: RoutingService) { 
    this.items = [];
    this.userItems = [];
    this.languageItems = [];
    this.isVisible = sessionService.getUserId() > 0;
    this.currentLanguage = '';

    this.sessionService.userLoggedEmitter.subscribe((isUserLogged) => {
      this.isVisible = isUserLogged;
      this.initializeMenu();
    });

    this.sessionService.projectSelectedSubject.subscribe(() => {
      this.initializeMenu();
    });
  }

  ngOnInit(): void {
    this.sessionService.getCurrentLanguage().subscribe(language => {
      this.currentLanguage = language;
    });
    this.initializeMenu();
  }

  private initializeMenu(): void {
    this.items = [
      {
        label: 'Project',
        command: () => { this.routingService.gotoProjectComponent(); },
      },
      {
        label: 'User Stories',
        command: () => { this.routingService.gotoBacklogComponent( +this.sessionService.getProjectId()); },
        visible: this.isProjectSelected()
      },
      {
        label: 'Tasks',
        command: () => { this.routingService.gotoTaskComponent( +this.sessionService.getProjectId()); },
        visible: this.isProjectSelected()
      },
    ];
    this.userItems = [
      {
        label: 'Quitter',
        command: () => { 
          this.sessionService.closeSession();
          this.routingService.gotoLoginComponent();
        }
      },
      {
        label:'Mon compte',
        command: () => { this.routingService.gotoAccountComponent(); }
      }
    ];

    PMConstants.AVAILABLE_LANG.forEach(language => {
      this.languageItems.push({
        label: language.label,
        command: () => {
          this.sessionService.setLanguage(language.value);
        }
      });
    });

    this.languageItems = [...new Set(this.languageItems)];
  }

  private isProjectSelected(): boolean {
    return this.sessionService.getProjectId() > 0;
  }
}
