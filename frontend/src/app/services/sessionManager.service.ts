import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DEFAULT_INTERRUPTSOURCES, Idle } from '@ng-idle/core';
import { Subject } from 'rxjs';
import { PMConstants } from '../common/PMConstants';
import { DialogConfirmComponent } from '../modules/shared/dialog-confirm/dialog-confirm.component';
import { API } from './Api';
import { RoutingService } from './routing.service';

@Injectable()
export class sessionManagerService {
  // TODO create a wrapper for IDLE
  public readonly IDLE_END = 10;
  public readonly IDLE_TIMEOUT = 5;

  public idleMessage = 'Vous allez être deconnecté dans ';
  public idleCountdown: number;

  public userLoggedEmitter: Subject<boolean>;
  public projectSelectedSubject: Subject<void>;
  private readonly invalidId: number;

  constructor(private readonly api: API,
             private readonly routingService: RoutingService,
             private readonly idle: Idle,
             private readonly matDialog: MatDialog) {
    this.userLoggedEmitter = new Subject<boolean>();
    this.projectSelectedSubject = new Subject<void>();
    this.invalidId = -1;
    this.idleCountdown = this.IDLE_TIMEOUT;

    idle.setIdle(this.IDLE_END);
    idle.setTimeout(this.IDLE_TIMEOUT);

    idle.onIdleStart.subscribe(() => {
      matDialog.open(DialogConfirmComponent, { data: { message: this.idleMessage + ' ' + this.idleCountdown }}).afterClosed().subscribe(() => {
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
      this.idleCountdown = countdown;
    });
  }

  public subscribeIdle(): void {
    this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);
    this.idle.watch();
  }

  public start(token: string, userId?: number): void {
    if (userId != null) {
      this.setUserid(userId);
    }
    localStorage.setItem(PMConstants.SESSION_TOKEN_ID_KEY, token);
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

  public closeSession(): void {
    this.userLoggedEmitter.next(false);
    localStorage.clear();
    this.api.clearHeader();
  }

  public isActive(): boolean {
    return this.getUserId() != this.invalidId;
  }
}