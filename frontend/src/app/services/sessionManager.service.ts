import { Injectable, Injector } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DEFAULT_INTERRUPTSOURCES, Idle } from '@ng-idle/core';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { PMConstants } from '../common/PMConstants';
import { DialogInfosComponent } from '../modules/shared/dialog-infos/dialog-infos.component';
import { API } from './Api';
import { RoutingService } from './routing.service';

@Injectable()
export class sessionManagerService {
  // TODO create a wrapper for IDLE
  public readonly IDLE_END = 60 * 15;
  public readonly IDLE_TIMEOUT = 60 * 5;

  public idleCountdown: number;

  public $idleWarningMessageSubject: Subject<string>;

  public userLoggedEmitter: Subject<boolean>;
  public projectSelectedSubject: Subject<void>;
  private readonly invalidId: number;

  constructor(private readonly routingService: RoutingService,
             private readonly idle: Idle,
             private readonly matDialog: MatDialog,
             private readonly injector: Injector) {
    this.userLoggedEmitter = new Subject<boolean>();
    this.projectSelectedSubject = new Subject<void>();
    this.invalidId = -1;
    this.idleCountdown = this.IDLE_TIMEOUT;
    this.$idleWarningMessageSubject = new Subject();

    idle.setIdle(this.IDLE_END);
    idle.setTimeout(this.IDLE_TIMEOUT);

    idle.onIdleStart.subscribe(() => {
      this.idleCountdown = this.IDLE_TIMEOUT;
      matDialog.open(DialogInfosComponent, { data: { message: this.$idleWarningMessageSubject }}).afterClosed().subscribe(() => {
        if (this.isActive()) {
          this.subscribeIdle();
        }
      });
      idle.clearInterrupts();
    });

    idle.onTimeout.subscribe(() => {
      this.closeSession();
      matDialog.closeAll();
      this.routingService.gotoLoginComponent();
    });
    
    idle.onTimeoutWarning.subscribe((countdown) => { 
      const time = countdown < 60 ? countdown + ' secondes' : Math.trunc(countdown/60) + 'min' + countdown%60 + 'secondes';
      this.$idleWarningMessageSubject.next('You will be loged out in ' + time);
    });

  }

  public subscribeIdle(): void {
    this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);
    this.idle.watch();
  }

  public setLanguage(lang: string): void {
    this.injector.get(TranslateService).use(lang);
    this.injector.get(API).setLang(lang);
  }

  public start(token: string, expiresIn: number, userId?: number): void {
    if (userId != null) {
      this.setUserid(userId);
    }
    localStorage.setItem(PMConstants.SESSION_TOKEN_ID_KEY, token);
    localStorage.setItem(PMConstants.SESSION_EXPIRATION, `${expiresIn}`);
    this.subscribeIdle();
  }

  public setUserid(id: number): void {
    localStorage.setItem(PMConstants.SESSION_USER_ID_KEY, `${id}`);
    this.userLoggedEmitter.next(true);// TODO move to login service
  }

  public getUserId(): number {
    const id = localStorage.getItem(PMConstants.SESSION_USER_ID_KEY);
    return id != null ? +id : -1;
  }

  public setProjectId(id: number): void {
    localStorage.setItem(PMConstants.SESSION_PROJECT_ID_KEY, `${id}`);
    this.projectSelectedSubject.next();
  }

  public getProjectId(): number {
    const id = localStorage.getItem(PMConstants.SESSION_PROJECT_ID_KEY);
    return id != null ? +id : -1;
  }

  public getTokenExpiration(): number | null {
    const expiration = localStorage.getItem(PMConstants.SESSION_EXPIRATION);
    if (expiration != null) {
      return +expiration;
    }

    return null;
  }

  public closeSession(): void {
    this.userLoggedEmitter.next(false);
    localStorage.clear();
    this.injector.get(API).clearHeader();
  }

  // TODO should be in login Service ?
  public logout(): void {
    this.matDialog.closeAll();
    this.closeSession();
    this.routingService.gotoLoginComponent();
  }

  public isActive(): boolean {
    // TODO other things than token ?
    return !this.isTokenExpired();
  }

  public hasExpired(): boolean {
    return this.getTokenExpiration() != null && this.isTokenExpired();
  }

  public isTokenExpired(): boolean {
    const token_expiration = localStorage.getItem(PMConstants.SESSION_EXPIRATION);
    if (token_expiration != null) {
      return (new Date()) > new Date(+token_expiration);
    }

    return true;
  }
}