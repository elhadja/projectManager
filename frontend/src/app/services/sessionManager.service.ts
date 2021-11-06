import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import { PMConstants } from "../common/PMConstants";

@Injectable()
export class sessionManagerService {
    private userId: number
    private projectId: number;
    public userLoggedEmitter: Subject<boolean>;
    public projectSelectedSubject: Subject<void>;

    constructor() {
        this.userId = -1;
        this.projectId = -1;
        this.userLoggedEmitter = new Subject<boolean>();
        this.projectSelectedSubject = new Subject<void>();
    }

    public setUserid(id: number): void {
        sessionStorage.setItem(PMConstants.SESSION_USER_ID_KEY, `${id}`);
        this.userId = id;
        localStorage.setItem(PMConstants.SESSION_USER_ID_KEY, `${id}`);
        this.userLoggedEmitter.next(true);
    }

    public getUserId(): number {
        if (this.userId === -1) {
            const id = localStorage.getItem(PMConstants.SESSION_USER_ID_KEY);
            if (id != null) {
                this.userId = +id;
            }
        }

        return this.userId;
    }

    public setProjectId(id: number): void {
        this.projectId = id;
        localStorage.setItem(PMConstants.SESSION_PROJECT_ID_KEY, `${id}`);
        this.projectSelectedSubject.next();
    }

    public getProjectId(): number {
        if (this.projectId === -1) {
            const id = localStorage.getItem(PMConstants.SESSION_PROJECT_ID_KEY);
            if (id != null) {
                this.projectId = +id;
            }
        }

        return this.projectId;
    }

    public closeSession(): void {
        this.userLoggedEmitter.next(false);
        localStorage.clear();
    }
}