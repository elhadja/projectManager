import { Injectable } from '@angular/core';
import { DEFAULT_INTERRUPTSOURCES, Idle } from '@ng-idle/core';
import { Subject } from 'rxjs';
import { PMConstants } from '../common/PMConstants';
import { API } from './Api';
import { RoutingService } from './routing.service';

@Injectable()
export class sessionManagerService {
  public userLoggedEmitter: Subject<boolean>;
  public projectSelectedSubject: Subject<void>;
  private readonly invalidId: number;

  constructor(private readonly api: API,
             private readonly routingService: RoutingService,
             private readonly idle: Idle) {
    this.userLoggedEmitter = new Subject<boolean>();
    this.projectSelectedSubject = new Subject<void>();
    this.invalidId = -1;

    idle.setIdle(5);
    idle.setTimeout(5);
    idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);

    idle.onTimeout.subscribe(() => {
      this.closeSession();
      this.routingService.gotoLoginComponent();
    });

    //idle.onTimeoutWarning.subscribe((countdown) => { console.log('waring: ', countdown); });
  }

  public subscribeIdle(): void {
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