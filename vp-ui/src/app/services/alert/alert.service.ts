import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

export enum AlertType {
  SUCCESS = 'success',
  ERROR = 'error',
  INFO = 'info',
  WARNING = 'warning'
}

export type AlertOptions = {
  duration?: number,
  type?: AlertType,
  persistent?: boolean
}

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  private shown: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private message: BehaviorSubject<String> = new BehaviorSubject<String>("");

  // Default options
  private options: BehaviorSubject<AlertOptions> = new BehaviorSubject<AlertOptions>({
    duration: 5000,
    type: AlertType.INFO,
    persistent: false
  });

  constructor() {  }

  init() {
    this.shown.next(false);
    this.message.next("");
  }

  show(message: String, options?: AlertOptions): void {
    this.message.next(message);
    this.shown.next(true);
    this.options.next({
      ...this.options,
      ...options
    });
    if (!this.options.getValue().persistent) setTimeout(() => this.shown.next(false), this.options.getValue().duration);
  }

  hide() {
    this.shown.next(false);
  }


  get isShown() {
    return this.shown;
  }

  get getMessage() {
    return this.message;
  }

  get getOptions() {
    return this.options;
  }
}
