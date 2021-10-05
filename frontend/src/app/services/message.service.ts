import { Injectable } from "@angular/core";
import { Subject } from "rxjs";

@Injectable()
export class MessageService {
    public messageSubject: Subject<string>;
    private messageType: number;

    constructor() {
        this.messageSubject = new Subject<string>();
        this.messageType = -1;
    }
    
    public showWarningMessage(message: string) {
        this.messageType = 1;
        this.showMessage(message);
    }

    public showErrorMessage(message: string) {
        this.messageType = 2;
        this.showMessage(message);
    }

    public showSuccessMessage(message: string) {
        this.messageType = 0;
        this.showMessage(message);
    }

    private showMessage(message: string): void {
        this.messageSubject.next(message);
    }

    public getCurrentMessageType(): number {
        return this.messageType;
    }
}