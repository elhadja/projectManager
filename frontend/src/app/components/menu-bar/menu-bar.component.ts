import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { RoutingService } from 'src/app/services/routing.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';

@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrls: ['./menu-bar.component.css']
})
export class MenuBarComponent implements OnInit {
  public items: MenuItem[];
  public isVisible: boolean;

  constructor(private router: Router,
             private sessionService: sessionManagerService,
             private routingService: RoutingService) { 
    this.items = [];
    this.isVisible = sessionService.getUserId() > 0;

    this.sessionService.userLoggedEmitter.subscribe((isUserLogged) => {
      this.isVisible = isUserLogged;
      this.initializeMenu();
    });

    this.sessionService.projectSelectedSubject.subscribe(() => {
      this.initializeMenu();
    });
  }

  ngOnInit(): void {
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
        visible: this.isProjectSelected()
      },
      {
        label: 'user name',
        items: [
          {
            label: 'quitter',
            command: () => { 
              this.sessionService.closeSession();
              this.routingService.gotoLoginComponent();
            }
          }
        ]
      }
    ]
  }

  private isProjectSelected(): boolean {
    return this.sessionService.getProjectId() > 0;
  }
}
