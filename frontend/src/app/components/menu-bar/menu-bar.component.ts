import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { PMConstants } from 'src/app/common/PMConstants';

@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrls: ['./menu-bar.component.css']
})
export class MenuBarComponent implements OnInit {
  public items: MenuItem[];

  constructor(private router: Router) { 
    this.items = [];
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
        label: 'Tasks'
      },
      {
        label: 'user name',
        items: [
          {
            label: 'quitter',
            command: () => { 
              localStorage.clear;
              this.router.navigateByUrl(PMConstants.AUTHENTICATION_MODULE_BASE_URI) ;
            }
          }
        ]
      }
    ]
  }

}
