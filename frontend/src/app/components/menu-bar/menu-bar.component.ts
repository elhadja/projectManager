import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Subscriber, Subscription } from 'rxjs';
import { SubjectSubscriber } from 'rxjs/internal/Subject';
import { PMConstants } from 'src/app/common/PMConstants';
import { sessionManagerService } from 'src/app/services/sessionManager.service';

@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrls: ['./menu-bar.component.css']
})
export class MenuBarComponent implements OnInit {
  public items: MenuItem[];
  public isVisible: boolean;
  public isProjectSelected: boolean;

  constructor(private router: Router,
             private sessionService: sessionManagerService) { 
    this.items = [];
    this.isVisible = sessionService.getUserId != null;
    this.isProjectSelected = false;

    this.sessionService.userLoggedEmitter.subscribe((isUserLogged) => {
      this.isVisible = isUserLogged;
    });

    this.sessionService.projectSelectedSubject.subscribe(() => {
      this.isProjectSelected = true;
      this.items = [...this.items];
    });
  }

  ngOnInit(): void {
    this.items = [
      {
        label: 'Project',
        command: () => {this.router.navigate(["/" + PMConstants.PROJECT_MODULE_BASE_URI])},
      },
      {
        label: 'User Stories',
      },
      {
        label: 'Tasks',
      },
      {
        label: 'user name',
        items: [
          {
            label: 'quitter',
            command: () => { 
              this.sessionService.closeSession();
              this.router.navigateByUrl(PMConstants.AUTHENTICATION_MODULE_BASE_URI) ;
            }
          }
        ]
      }
    ]
  }
}
