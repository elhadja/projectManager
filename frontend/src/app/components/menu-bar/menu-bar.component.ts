import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { PMConstants } from 'src/app/common/PMConstants';
import { DialogConfirmComponent } from 'src/app/modules/shared/dialog-confirm/dialog-confirm.component';
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
             private readonly matDialog: MatDialog) { 
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

    PMConstants.AVAILABLE_LANG.forEach(language => {
      this.languageItems.push({
        label: language.label,
        command: () => {
          this.sessionService.setLanguage(language.value);
        }
      });
    });
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
      },
      {
        label:'About', // TODO i18n
        command: () => { this.matDialog.open(DialogAboutComponent); }
      }
    ];
  }

  private isProjectSelected(): boolean {
    return this.sessionService.getProjectId() > 0;
  }
}
