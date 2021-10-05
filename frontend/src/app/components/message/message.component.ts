import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit, OnDestroy {
  public message: string | undefined;
  public messageType: number;
  private messageSubscription: Subscription;

  constructor(public readonly messageService: MessageService) {
    this.message = undefined;
    this.messageType = 0;
    this.messageSubscription = this.messageService.messageSubject.subscribe((message => {
      this.message = message;
      this.messageType = messageService.getCurrentMessageType();
      setTimeout(() => {
        this.hide();
      }, 5000);
    }))
   }

  public ngOnInit(): void {
  }

  public ngOnDestroy(): void {
    this.messageSubscription.unsubscribe();
  }

  public hide(): void {
    this.message = undefined;
  }

}
