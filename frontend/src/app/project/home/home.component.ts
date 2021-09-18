import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PMConstants } from 'src/app/common/PMConstants';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  public onLogout(): void {
    localStorage.removeItem('token');
    this.router.navigateByUrl(PMConstants.AUTHENTICATION_MODULE_BASE_URI);
  }
}
