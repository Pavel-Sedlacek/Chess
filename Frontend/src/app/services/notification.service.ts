import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {Notification} from '../../data/notification';

@Injectable({
              providedIn: 'root'
            })
export class NotificationService {


  private notificationSource = new BehaviorSubject<Notification>(null);
  activeNotification = this.notificationSource.asObservable();

  constructor() {
  }

  pushNotification(message: string, status: boolean): void {
    this.notificationSource.next({message, status});
  }

  close(): void {
    this.notificationSource.next(null);
  }
}

// TODO Karle vsude kde se nejak operuje s apickem, tak pouzij tohle na vypisovani vysledku

// vzdycky actualni, AKA nemusis ji aktualizovat,  a je stejna pro vsechny komponenty

/*
example use:
  constructor(private readonly nejakaJinaServica: NejakaJinaServica, private readonly notificationService: NotificationService){}

  funkce(): any {
    this.nejakaJinaServica.post({username:"Aloij", password:"123").subscribe(data =>
      u => this.notificationService.push(u, true)
      e => this.notificationService.push(e, false)
    });
  }
 */
